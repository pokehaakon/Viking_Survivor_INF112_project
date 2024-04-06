package Parsing;

import com.badlogic.gdx.Gdx;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TextParser extends GenericParser<Character, String> {

    public TextParser(String filename) {
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
    }

    public TextParser(char[] text) {
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
}
