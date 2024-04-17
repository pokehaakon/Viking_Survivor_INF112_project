package Parsing;


import GameObjects.ObjectTypes.EnemyType;
import Tools.Tuple;
import org.apache.maven.surefire.shared.lang3.tuple.ImmutablePair;
import org.javatuples.Pair;

import java.util.*;

import static Tools.EnumTools.enumToStrings;


public class MapParser extends TextParser {
    private Map<String, String> defines;
    private List<Pair<Long, List<SpawnFrame>>> timeFrames;

    public MapParser(String filename) {
        super(filename);
    }

    public MapParser(char[] text) {
        super(text);
    }


    public void doParse() {
        if (defines != null) return;
        parseMapText();
    }

    private void parseMapText() {
        defines = parseDefines();
        timeFrames = parseTimeFrames();
    }

    public List<Pair<Long, List<SpawnFrame>>> getTimeFrames() {
        return timeFrames;
    }

    public Map<String, String> getDefines() {
        return defines;
    }

    public Map<String, String> parseDefines() {
        Map<String, String> defines = new HashMap<>();
        many(() -> Try(() -> {
            many(() -> choose(this::parseEmptyLine, this::parseComment));

            if (parseLiteral('!').isEmpty()) return Optional.empty();
            Optional<String> key = parseUntilLiteral('=').map(String::strip);
            if (parseLiteral('=').isEmpty()) return Optional.empty();
            Optional<String> value = parseUntilLiteral('\n').map(String::strip);

            if (key.isEmpty() && value.isEmpty()) return Optional.empty();
            if (key.isEmpty() || value.isEmpty())
                throw new ParserException(this, "Error while reading MapFile Header", this.stream);
            defines.put(key.get(), value.get());
            return Optional.of(key.get() + ":" + value.get() + "\n");
        }));
        return defines;
    }
    public List<Pair<Long, List<SpawnFrame>>>  parseTimeFrames() {
        many(() -> choose(this::parseEmptyLine, this::parseComment));

        List<Pair<Long, List<SpawnFrame>>> pairs = many(() -> Try(() -> {
            long frame = choose(
                    () -> {
                        int minutes = numbers().map(Integer::parseInt).get();
                        if (parseLiteral(':').isEmpty()) return Optional.empty();
                        int seconds = numbers().map(Integer::parseInt).get();
                        if (parseLiteral(':').isEmpty()) return Optional.empty();
                        return Optional.of(60 * (seconds + 60 * minutes));
                    },
                    () -> {
                        int frameNum = numbers().map(Integer::parseInt).get();
                        if (parseStringLiteral("f:").isEmpty()) return Optional.empty();
                        return Optional.of(frameNum);
                    }
            ).get();
            parseComment();
            parseEmptyLine();

            List<SpawnFrame> body = parseFrameBody();
            return Optional.of(Tuple.of(frame, body));
        })).get();

        long lastFrame = -1;
        for(Pair<Long, List<SpawnFrame>> p : pairs) {
            if (lastFrame < p.getValue0()) {
                lastFrame = p.getValue0();
                continue;
            }
            throw new ParserException(this, "error at frame with value: " + p.getValue0() + "f, (value is higher than previous value)" , this.stream);
        }
        return pairs;
    }

    public List<SpawnFrame> parseFrameBody() {
        return some(() -> {
            many(() -> choose(this::parseEmptyLine, this::parseComment));

            choose(() -> parseLiteral('\t'), () -> parseStringLiteral("    ")); //support space and tap tabulation
            if(undo(this::letter).isEmpty()) return Optional.empty(); //next should be a letter

            List<EnemyType> spawnable = some(() -> {
                if (undo(() -> parseLiteral(';')).isPresent()) return Optional.empty();
                Optional<String> opt = parseStringLiteral(enumToStrings(EnemyType.class));
                if(opt.isEmpty())
                    throw new ParserException(this, "Could not find the EnemyType", this.stream);
                space();
                return opt.map(EnemyType::valueOf);
            }).get();
            if(parseLiteral('\n').isPresent()) return Optional.empty();
            parseLiteral(';');

            space();
            SpawnType spawnType = parseStringLiteral(enumToStrings(SpawnType.class))
                    .map(SpawnType::valueOf)
                    .orElseThrow(() -> new ParserException(this, "Could not find the SpawnType", this.stream));
            space();

            List<String> args = some(() -> {
                space();
                Optional<String> opt = parseUntilLiteral(' ', '\n', '#');
                return opt;
            }).get();

            return Optional.of(new SpawnFrame(spawnable.get(0), spawnType, args));
        }).get();
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

}






























