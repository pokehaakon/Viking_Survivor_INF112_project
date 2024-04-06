package Parsing;


import com.badlogic.gdx.Gdx;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class MapParser extends GenericParser<Character, String> {
    private Map<String, String> defines;
    private Map<Long, List<SpawnFrame>> timeFrames;
    public MapParser(String filename) {
        super(
                filename,
                (s) -> new CharArrayStream(Gdx.files.internal(filename).readString()),
                (l) -> {
                    StringBuilder b = new StringBuilder();
                    for (Character c : l) {
                        b.append(c);
                    }
                    return b.toString();
                },
                (s) -> {
                    List<Character> cs = new ArrayList<>(s.length());
                    for(int i = 0; i < s.length(); i++) {
                        cs.add(s.charAt(i));
                    }
                    return cs;
                }
        );
        parseMapText();
    }

    public MapParser(char[] text) {
        super(
            new CharArrayStream(text),
            (l) -> {
                StringBuilder b = new StringBuilder();
                for (Character c : l) {
                    b.append(c);
                }
                return b.toString();
            },
            (s) -> {
                List<Character> cs = new ArrayList<>(s.length());
                for(int i = 0; i < s.length(); i++) {
                    cs.add(s.charAt(i));
                }
                return cs;
            }
        );
        parseMapText();
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
            parseLiteral('!');
            Optional<String> key = strip(this::letters, ' ', '\t');
            //String key = parseUntilLiteral('=', ' ', '\t').get();
            next();
            Optional<String> value = strip(this::letters, ' ', '\t');
            //String value = parseUntilLiteral('\n').get();
            next();
            if (key.isEmpty() && value.isEmpty()) return Optional.empty();
            if (key.isEmpty() || value.isEmpty())
                throw new ParserException(this, "Error while reading MapFile Header", this.stream);
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
                    parseLiteral(':');
                    String seconds = numbers().get();
                    parseLiteral(':');
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
            choose(() -> parseLiteral('\t'), () -> parseStringLiteral("    ")); //support space and tap tabulation
            if(undo(this::letter).isEmpty()) return Optional.empty(); //next should be a letter

            List<String> spawnable = some(() -> {
                Optional<String> opt = parseUntilLiteral(' ', ';', '\n');
                space();
                return opt;
            }).get();
            if(parseLiteral('\n').isPresent()) return Optional.empty();
            parseLiteral(';');
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

    /**
     * Parses a line with just spaces, tabs and ending with a newline
     * @return
     */
    public Optional<String> parseEmptyLine() {
        return Try(() -> {
            StringBuilder b = new StringBuilder();
            for (String s : many(() -> parseLiteral(' ', '\t')).get()) {
                b.append(s);
            }
            Optional<String> opt = parseLiteral('\n');
            if (opt.isEmpty()) return Optional.empty();
            opt.ifPresent(b::append);
            return Optional.of(b.toString());
        });
    }

    /**
     * Parses everything between a '#' and newline
     * @return the string of the comment
     */
    public Optional<String> parseComment() {
        return Try(() -> {
            space();
            if (parseLiteral('#').isEmpty()) return Optional.empty();
            Optional<String> opt = parseUntilLiteral('\n');
            Try(() -> parseLiteral('\n'));
            return opt;
        });
    }

    public Optional<String> letter() {
        return parseLiteralFromFunction(Character::isLetter);
    }

    public Optional<String> letters() {
        return parseStringFromFunction(Character::isLetter);
    }

    public Optional<String> number() {
        return parseLiteralFromFunction(Character::isDigit);
    }

    public Optional<String> numbers() {
        return parseStringFromFunction(Character::isDigit);
    }

    public Optional<String> space() {
        Optional<List<String>> opt = many(() -> parseLiteral(' ', '\t'));
        return opt.map(strings -> String.join("", strings));
    }

    public Optional<String> skipLine() {
        String s = parseUntilLiteral('\n').orElseGet(() -> "");
        if(parseLiteral('\n').isPresent()) return Optional.of(s + "\n");
        return Optional.empty();
    }
}






























