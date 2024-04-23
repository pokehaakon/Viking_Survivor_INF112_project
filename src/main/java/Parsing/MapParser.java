package Parsing;


import GameObjects.ObjectTypes.EnemyType;
import Tools.Tuple;
import org.javatuples.Pair;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static Tools.EnumTools.HashMapTool.mapFromPairs;
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
        return mapFromPairs(many(iTry(() -> {
            many(() -> choose(this::parseEmptyLine, this::parseComment));
            parseLiteral('!');
            Optional<String> key = test(iparseUntilLiteral('=')).map(String::strip);
            parseLiteral('=');
            Optional<String> value = test(iparseUntilLiteral('\n')).map(String::strip);

            if (key.isEmpty() && value.isEmpty()) error();
            if (key.isEmpty() || value.isEmpty())
                throw new ParserException(this, "Error while reading MapFile Header", this.stream);
            return Tuple.of(key.get(), value.get());
        })));
    }
    public List<Pair<Long, List<SpawnFrame>>>  parseTimeFrames() {
        many(() -> choose(this::parseEmptyLine, this::parseComment));

        //parses frame header
        List<Pair<Long, List<SpawnFrame>>> pairs = many(iTry(() -> {
            long frame = choose(
                    () -> {
                        long minutes = Long.parseLong(numbers());
                        parseLiteral(':');
                        long seconds = Long.parseLong(numbers());
                        parseLiteral(':');
                        return 60 * (seconds + 60 * minutes);
                    },
                    () -> {
                        long frameNum = Long.parseLong(numbers());
                        parseStringLiteral("f:");
                        return frameNum;
                    }
            );
            //Void(this::parseComment);
            //Void(this::parseEmptyLine);

            List<SpawnFrame> body = parseFrameBody();
            return Tuple.of(frame, body);
        }));

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

    public List<SpawnFrame> parseFrameBody() throws ParsingException {
        return some(() -> {
            many(ichoose(this::parseEmptyLine, this::parseComment));

            choose(
                    iparseLiteral('\t'),
                    iparseStringLiteral("    ")
            ); //support space and tap tabulation

            iundo(this::letter); //next should be a letter

            EnemyType spawnable = test(iparseStringLiteral(enumToStrings(EnemyType.class)))
                    .map(EnemyType::valueOf)
                    .orElseThrow(() -> new ParserException(this, "Could not find the SpawnType", this.stream));

//            List<EnemyType> spawnable = some(() -> {
//                shouldError(iundo(iparseLiteral(';')));
//
//                Optional<String> enemyString = test(iparseStringLiteral(enumToStrings(EnemyType.class)));
//                if(enemyString.isEmpty())
//                    throw new ParserException(this, "Could not find the EnemyType", this.stream);
//                space();
//                return enemyString.map(EnemyType::valueOf).get();
//            });

            shouldError(iparseLiteral('\n'));
            parseLiteral(';');

            space();
            SpawnType spawnType = test(iparseStringLiteral(enumToStrings(SpawnType.class)))
                    .map(SpawnType::valueOf)
                    .orElseThrow(() -> new ParserException(this, "Could not find the SpawnType", this.stream));
            space();

            List<String> args = some(() -> {
                space();
                return parseUntilLiteral(' ', '\n', '#');
            });

            return new SpawnFrame(spawnable, spawnType, args);
        });
    }

    /**
     * Parses everything between a '#' and newline
     * @return the string of the comment
     */
    public String parseComment() throws ParsingException {
        return Try(() -> {
            Void(this::space);
            parseLiteral('#');
            String opt = parseUntilLiteral('\n');
            Void(iparseLiteral('\n'));
            return opt;
        });
    }

}






























