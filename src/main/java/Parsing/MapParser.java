package Parsing;


import Actors.Enemy.EnemyType;
import org.apache.maven.surefire.shared.lang3.tuple.ImmutablePair;
import org.apache.maven.surefire.shared.lang3.tuple.Pair;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class MapParser extends TextParser {
    private Map<String, String> defines;
    private Map<Long, List<SpawnFrame>> timeFrames;
    public MapParser(String filename) {
        super(filename);
    }

    public MapParser(char[] text) {
        super(text);
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
        many(() -> choose(this::parseEmptyLine, this::parseComment));
        List<Pair<Long, List<SpawnFrame>>> pairs = many(() -> Try(() -> {
            int frame = choose(
                    () -> {
                        int minutes = numbers().map(Integer::parseInt).get();
                        parseLiteral(':');
                        int seconds = numbers().map(Integer::parseInt).get();
                        parseLiteral(':');
                        return Optional.of(60 * (seconds + 60 * minutes));
                    },
                    () -> {
                        int frameNum = numbers().map(Integer::parseInt).get();
                        parseStringLiteral("f:");
                        return Optional.of(frameNum);
                    }
            ).get();
            parseComment();
            parseEmptyLine();

            List<SpawnFrame> body = parseFrameBody();
            return Optional.of(
                    (Pair<Long, List<SpawnFrame>>) new ImmutablePair<Long, List<SpawnFrame>>((long)frame, body)
            );
        })).get();
        Map<Long, List<SpawnFrame>> map = new HashMap<>();
        for(Pair<Long, List<SpawnFrame>> p : pairs) {
            map.put(p.getLeft(), p.getRight());
        }
        return map;
    }

    public List<SpawnFrame> parseFrameBody() {
        return some(() -> {
            many(() -> choose(this::parseEmptyLine, this::parseComment));

            choose(() -> parseLiteral('\t'), () -> parseStringLiteral("    ")); //support space and tap tabulation
            if(undo(this::letter).isEmpty()) return Optional.empty(); //next should be a letter

            List<EnemyType> spawnable = some(() -> {
                if (undo(() -> parseLiteral(';')).isPresent()) return Optional.empty();
                Optional<String> opt = parseStringLiteral(EnemyType.stringValues());
                if(opt.isEmpty())
                    throw new ParserException(this, "Could not find the EnemyType", this.stream);
                space();
                return opt.map(EnemyType::valueOf);
            }).get();
            if(parseLiteral('\n').isPresent()) return Optional.empty();
            parseLiteral(';');

            space();
            SpawnType spawnType = parseStringLiteral(SpawnType.stringValues())
                    .map(SpawnType::valueOf)
                    .orElseThrow(() -> new ParserException(this, "Could not find the SpawnType", this.stream));
            space();

            List<String> args = some(() -> {
                Optional<String> opt = parseUntilLiteral(' ', '\n', '#');
                space();
                return opt;
            }).get();

            return Optional.of(new SpawnFrame(spawnable, spawnType, args));
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






























