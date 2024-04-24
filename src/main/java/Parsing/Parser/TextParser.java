package Parsing.Parser;

import Parsing.Stream.CharArrayStream;
import com.badlogic.gdx.Gdx;

import java.util.ArrayList;
import java.util.List;

public class TextParser extends GenericParser<Character, String> {

    public TextParser(String filename) {
        this((Gdx.files.internal(filename).readString().replace('\r', '\n') + "\n").toCharArray());
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

    /**
     * Parses one letter
     * @return the parsed letter
     */
    public String letter() throws ParsingException {
        return parseLiteralFromFunction(Character::isLetter);
    }

    /**
     * Parses a string of letters
     * @return the parsed string
     */
    public String letters() throws ParsingException {
        return parseStringFromFunction(Character::isLetter);
    }

    /**
     * Parses one number
     * @return the parsed number
     */
    public String number() throws ParsingException {
        return parseLiteralFromFunction(Character::isDigit);
    }

    /**
     * Parses a string of numbers
     * @return the parsed numbers
     */
    public String numbers() throws ParsingException {
        return parseStringFromFunction(Character::isDigit);
    }


    /**
     * Parses tabs and spaces
     * @return the parsed tabs and spaces
     */
    public String space() {
        return String.join("", many(() -> parseLiteral(' ', '\t')));
    }

    /**
     * Parses spaces, then a newline char, errors if there is no newline
     * @return the parsed string
     */
    public String skipLine() throws ParsingException {
        return space() + parseLiteral('\n');
    }

    /**
     * Tries to parse a line with just spaces, tabs and ending with a newline
     * @return the parsed string
     */
    public String parseEmptyLine() throws ParsingException {
        return Try(() -> space() + parseLiteral('\n'));
    }

    /**
     * Parses either '\n' or '\r'
     * @return the parsed char
     */
    public String parseNewLineLiteral() throws ParsingException {
        return parseLiteral('\n', '\r');
    }
}
