package Parsing;

import com.badlogic.gdx.Gdx;

import java.util.ArrayList;
import java.util.List;

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


    public String letter() throws ParsingException {
        return parseLiteralFromFunction(Character::isLetter);
    }

    public String letters() throws ParsingException {
        return parseStringFromFunction(Character::isLetter);
    }

    public String number() throws ParsingException {
        return parseLiteralFromFunction(Character::isDigit);
    }

    public String numbers() throws ParsingException {
        return parseStringFromFunction(Character::isDigit);
    }

    public String space() {

        return String.join("", many(() -> parseLiteral(' ', '\t')));
    }

    public String skipLine() throws ParsingException {
        String s = orElse(iparseLiteral('\n'), "");
        return parseLiteral('\n') + s;
    }

    /**
     * Parses a line with just spaces, tabs and ending with a newline
     * @return
     */
    public String parseEmptyLine() throws ParsingException {
        return Try(() -> {
            String s = String.join("", many(iparseLiteral(' ', '\t')));
            return s + parseLiteral('\n');
        });
    }

    public String parseNewLineLiteral() throws ParsingException {
        return parseLiteral('\n', '\r');
    }
}
