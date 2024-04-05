package Parsing;


import com.badlogic.gdx.Gdx;

import java.text.ParseException;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class MapParser extends Parser {
    private Map<String, String> defines;
    private Map<Long, List<SpawnFrame>> timeFrames;
    public MapParser(String filename) {
        super(filename, (s) -> new CharArrayStream(Gdx.files.internal(filename).readString()));
        //parseMapText();
    }

    public MapParser(char[] text) {
        super(new CharArrayStream(text));
        //parseMapText();
    }

    public void doParse() {
        if (defines == null) return;
        parseMapText();
    }

    private void parseMapText() {
        defines = parseDefines();
        timeFrames = parseTimeFrames();
    }

    public Map<String, String> parseDefines() {
        Map<String, String> defines = new HashMap<>();
        many(() -> Try(() -> {
            parseCharLiteral('!');
            Optional<String> key = strip(this::letters, ' ', '\t');
            //String key = parseUntilLiteral('=', ' ', '\t').get();
            next();
            Optional<String> value = strip(this::letters, ' ', '\t');
            //String value = parseUntilLiteral('\n').get();
            next();
            if (key.isEmpty() && value.isEmpty()) return Optional.empty();
            if (key.isEmpty() || value.isEmpty())
                throw new Parser.ParserException("Error while reading MapFile Header", this.stream);
            defines.put(key.get(), value.get());
            return Optional.of(key.get() + ":" + value.get() + "\n");
        }));
        return defines;
    }

    public Map<Long, List<SpawnFrame>> parseTimeFrames() {
        Map<Long, List<SpawnFrame>> timedEvents = new HashMap<>();
        many(() -> choose(this::parseEmptyLine, this::parseComment));
        many(() -> Try(() -> {
            StringBuilder s = new StringBuilder();
            AtomicLong frame = new AtomicLong();
            String frameString = choose(
                () -> {
                    String minutes = numbers().get();
                    parseCharLiteral(':');
                    String seconds = numbers().get();
                    parseCharLiteral(':');
                    frame.set(60 * (Integer.parseInt(seconds) + 60L * Integer.parseInt(minutes)));
                    return Optional.of(minutes + ':' + seconds + ':');
                },
                () -> {
                    String frameSting = numbers().get();
                    parseStringLiteral("f:");
                    frame.set(Integer.parseInt(frameSting));
                    return Optional.of(frameSting + ':');
                }
            ).get();
            s.append(frameString);
            parseComment().ifPresent(s::append);
            parseEmptyLine().ifPresent(s::append);

            List<SpawnFrame> body = parseFrameBody();
            timedEvents.put(frame.get(), body);

            return Optional.of(s.toString());
        }));

        return timedEvents;
    }

    public record SpawnFrame(List<String> spawnable, String spawnType, List<String> args) { }

    public List<SpawnFrame> parseFrameBody() {
        List<SpawnFrame> spawnFrames = new ArrayList<>();
        some(() -> {

            many(() -> choose(this::parseEmptyLine, this::parseComment));
            choose(() -> parseCharLiteral('\t'), () -> parseStringLiteral("    ")); //support space and tap tabulation
            if(undo(this::letter).isEmpty()) return Optional.empty(); //next should be a letter

            List<String> spawnable = some(() -> {
                Optional<String> opt = parseUntilLiteral(' ', ';', '\n');
                space();
                return opt;
            }).get();
            if(parseCharLiteral('\n').isPresent()) return Optional.empty();
            parseCharLiteral(';');
            space();

            List<String> args = some(() -> {
                Optional<String> opt = parseUntilLiteral(' ', '\n', '#');
                space();
                return opt;
            }).get();

            spawnFrames.add(new SpawnFrame(
                    spawnable,
                    args.get(0),
                    args.subList(1, args.size())
            ));

            return Optional.of("a");
        });

        return spawnFrames;
    }
}






























