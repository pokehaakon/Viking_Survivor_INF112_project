package Parsing;


import GameObjects.ObjectFactory;
import Parsing.Parser.ParserException;
import Parsing.Parser.ParsingException;
import Parsing.Parser.TextParser;
import Tools.Tuple;
import com.google.common.annotations.VisibleForTesting;
import org.javatuples.Pair;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static Tools.HashMapTool.mapFromPairs;
import static Tools.EnumTools.enumToStrings;

public class MapParser extends TextParser {
    private Map<String, String> defines;
    private List<Pair<Long, List<SpawnFrame>>> timeFrames;
    private List<String> includes;

    public MapParser(String filename) {
        super(filename);
        if(!filename.endsWith(".wdef")) throw new IllegalArgumentException(filename + " must end with '.wdef'");
    }

    public MapParser(char[] text, String name) {
        super(text, name);
    }

    /**
     * Parses the 'include' block of the 'wdef'
     * @return the includes
     */
    public List<String> doParseIncludes() {
        if (includes != null) return includes;
        includes = parseIncludes();
        return includes;
    }

    /**
     * Parses the 'define' block of the 'wdef'
     * @return the defines
     */
    public Map<String, String> doParseDefines() {
        if (defines != null) return defines;
        defines = parseDefines();
        return defines;
    }

    /**
     * parses the 'timeFrame' block of the 'wdef'
     * @return the time frames
     */
    public List<Pair<Long, List<SpawnFrame>>> doParseTimeFrames() {
        if (timeFrames != null) return timeFrames;
        timeFrames = parseTimeFrames();
        return timeFrames;
    }

    public List<Pair<Long, List<SpawnFrame>>> getTimeFrames() {
        return timeFrames;
    }

    public Map<String, String> getDefines() {
        return defines;
    }

    public List<String> getIncludes() {return includes;}

    private Map<String, String> parseDefines() {
        return mapFromPairs(many(iTry(() -> {
            many(() -> choose(this::parseEmptyLine, this::parseComment));
            parseLiteral('!');
            Optional<String> key = test(iparseUntilLiteral('=')).map(String::strip);
            parseLiteral('=');
            Optional<String> value = test(iparseUntilLiteral('\n', '#')).map(String::strip);

            if (key.isEmpty() && value.isEmpty()) error("");
            if (key.isEmpty() || value.isEmpty())
                throw new ParserException(this, "Error while reading MapFile Header");
            return Tuple.of(key.get(), value.get());
        })));
    }

    private List<String> parseIncludes() {
        return many(iTry(() -> {
            many(() -> choose(this::parseEmptyLine, this::parseComment));
            parseStringLiteral("$include ");
            return parseUntilLiteral('\n', '#').strip();
        }));
    }

    private List<Pair<Long, List<SpawnFrame>>>  parseTimeFrames() {
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

            List<SpawnFrame> body = parseFrameBody();
            return Tuple.of(frame, body);
        }));

        long lastFrame = -1;
        for(Pair<Long, List<SpawnFrame>> p : pairs) {
            if (lastFrame < p.getValue0()) {
                lastFrame = p.getValue0();
                continue;
            }
            throw new ParserException(this, "error at frame with value: " + p.getValue0() + "f, (value is higher than previous value)");
        }
        return pairs;
    }

    @VisibleForTesting
    public List<SpawnFrame> parseFrameBody() throws ParsingException {
        return some(() -> {
            many(ichoose(this::parseEmptyLine, this::parseComment));

            choose(
                    iparseLiteral('\t'),
                    iparseStringLiteral("    ")
            ); //support space and tap tabulation

            iundo(this::letter); //next should be a letter

            String spawnable;
            try {
                spawnable = parseStringLiteral(ObjectFactory.getRegisteredActors());
            } catch (ParsingException e) {
                throw new ParserException(e, this, "Could not find the SpawnType");
            }

            shouldError(iparseLiteral('\n'));
            parseLiteral(';');

            space();
            SpawnType spawnType = test(iparseStringLiteral(enumToStrings(SpawnType.class)))
                    .map(SpawnType::valueOf)
                    .orElseThrow(() -> new ParserException(this, "Could not find the SpawnType"));
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






























