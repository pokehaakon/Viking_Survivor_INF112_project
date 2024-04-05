package Parsing;

import com.badlogic.gdx.Gdx;
import org.apache.maven.surefire.shared.io.output.ThresholdingOutputStream;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Parser {
    public class ParserException extends RuntimeException {
        public ParserException (String msg, Streamable s) {
            super(
                    msg + "\n\n" +
                    "Parse Error at: l" +
                    stream.getLine() + " c" +
                    stream.getLinePos() + "\n" +
                    stream.getDebugInfo()
            );
        }
    }
    protected Streamable stream;

    public Parser(String filename, Function<String, Streamable> createStream) {
        this.stream = createStream.apply(filename);
    }

    public Parser(Streamable stream) {
        this.stream = stream;
    }

    /**
     * Gives next Character, unless at EOF
     * @return the next character
     */
    public Optional<String> next() {
        if (stream.atEOF()) return Optional.empty();
        return Optional.of(String.valueOf(stream.next()));
    }

    /**
     * Gives next 'n' characters, returns Empty if it hits EOF
     * @param n number of characters to get
     * @return the next 'n' characters
     */
    public Optional<String> next(int n) {
        if (stream.atEOF()) return Optional.empty();
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < n; i++) b.append(stream.next());
        return Optional.of(b.toString());
    }

    /**
     * resets the parser to the original state
     */
    public void reset() {
        stream.reset();
    }

    /**
     * Parses everything between a '#' and newline
     * @return the string of the comment
     */
    public Optional<String> parseComment() {
        return Try(() -> {
            space();
            if (parseCharLiteral('#').isEmpty()) return Optional.empty();
            Optional<String> opt = parseUntilLiteral('\n');
            Try(() -> parseCharLiteral('\n'));
            return opt;
        });
    }

    /**
     * Tries the given parser, if it parses nothing resets as if noting happened
     * @param parser the parser to try
     * @return the return value of the parser
     */
    public <T> Optional<T> Try(Supplier<Optional<T>> parser) {
        Streamable oldStream = stream.copy();
        try {
            Optional<T> opt = parser.get();
            if (opt.isPresent()) return opt;
            stream = oldStream;
            return opt;
        } catch (ArrayIndexOutOfBoundsException | NoSuchElementException e) {
            stream = oldStream;
            return Optional.empty();
        }
    }

    /**
     * Tries the parsers in order until one succeeds, resets between tries
     * @param parsers array of the parsers to try
     * @return the return value of the succeeding parser
     */
    public <T> Optional<T> choose(Supplier<Optional<T>>... parsers) {
        for (Supplier<Optional<T>> p : parsers) {
            Optional <T> opt = Try(p);
            if (opt.isPresent()) return opt;
        }

        return Optional.empty();
    }

    public Optional<String> parseUntilLiteral(char m) {
        if (stream.getCurrent() == m) return Optional.empty();

        StringBuilder b = new StringBuilder();
        while (!stream.atEOF()) {
            char r = stream.getCurrent();
            if (r == m) return Optional.of(b.toString());
            stream.next();
            b.append(r);
        }
        //reached EOF
        return Optional.of(b.toString());
    }

    public Optional<String> parseUntilLiteral(char... ms) {
        for (char m : ms)
            if (stream.getCurrent() == m) return Optional.empty();

        StringBuilder b = new StringBuilder();
        while (!stream.atEOF()) {
            char r = stream.getCurrent();
            for (char m : ms)
                if (r == m) return Optional.of(b.toString());
            stream.next();
            b.append(r);
        }
        return Optional.of(b.toString());
    }

    public Optional<String> parseCharLiteral(char c) {
        if (c != stream.getCurrent()) return Optional.empty();
        stream.next();
        return Optional.of(String.valueOf(c));
    }

    public Optional<String> parseCharLiteral(char... chars) {
        char m = stream.getCurrent();
        for (char c : chars) {
            if (c != m) continue;
            stream.next();
            return Optional.of(String.valueOf(c));
        }
        return Optional.empty();
    }

    public Optional<String> parseStringLiteral(String string) {
        return parseStringLiteral(new String[]{string});
    }

    public Optional<String> parseStringLiteral(String... strings) {
        for (String s : strings) {
            Streamable cStream = stream.copy();
            char[] cString = s.toCharArray();
            boolean found = true;
            for (int i = 0; i < cString.length; i++){
                if (cString[i] == cStream.next()) continue;
                found = false;
                break;
            }
            if (!found) continue;
            stream = cStream;
            return Optional.of(s);
        }
        return Optional.empty();
    }

    /**
     * Parses a line with just spaces, tabs and ending with a newline
     * @return
     */
    public Optional<String> parseEmptyLine() {
        return Try(() -> {
            StringBuilder b = new StringBuilder();
            for (String s : many(() -> parseCharLiteral(new char[] {' ', '\t'})).get()) {
                b.append(s);
            }
            Optional<String> opt = parseCharLiteral('\n');
            if (opt.isEmpty()) return Optional.empty();
            opt.ifPresent(b::append);
            return Optional.of(b.toString());
        });
    }

    /**
     * Runs the given parser zero or more times
     * @param parser the parser to apply
     * @return
     */
    public  Optional<List<String>> many(Supplier<Optional<String>> parser) {
        List<String> out = new ArrayList<>();

        Optional<String> parsedString;
        while ((parsedString = parser.get()).isPresent()){
            out.add(parsedString.get());
        }

        return Optional.of(out);
    }

    /**
     * Runs the given parser one or more times
     * @param parser
     * @return
     */
    public Optional<List<String>> some(Supplier<Optional<String>> parser) {
        return Try(() ->  {
            List<String> out = new ArrayList<>();
            Optional<String> parsedString = parser.get();
            if (parsedString.isEmpty()) {
                return Optional.empty();
            }
            out.add(parsedString.get());
            while ((parsedString = parser.get()).isPresent()){
                out.add(parsedString.get());
            }
            return Optional.of(out);
        });
    }

    public Optional<String> letter() {
        return parseCharFromFunction(Character::isLetter);
    }

    public Optional<String> letters() {
        return parseStringFromFunction(Character::isLetter);
    }

    public Optional<String> number() {
        return parseCharFromFunction(Character::isDigit);
    }

    public Optional<String> numbers() {
        return parseStringFromFunction(Character::isDigit);
    }

    public Optional<String> parseCharFromFunction(Function<Character, Boolean> f) {
        if (!f.apply(stream.getCurrent())) {
            return Optional.empty();
        }
        return Optional.of(String.valueOf(stream.next()));
    }

    public Optional<String> parseStringFromFunction(Function<Character, Boolean> f) {
        StringBuilder b = new StringBuilder();
        while (!stream.atEOF()) {
            if (!f.apply(stream.getCurrent())) break;
            b.append(stream.getCurrent());
            stream.next();
        }
        if (b.isEmpty()) return Optional.empty();
        return Optional.of(b.toString());
    }

    public Optional<String> space() {
        Optional<List<String>> opt = many(() -> parseCharLiteral(' ', '\t'));
        return opt.map(strings -> String.join("", strings));
    }

    public Optional<String> skipLine() {
        String s = parseUntilLiteral('\n').orElseGet(() -> "");
        if(parseCharLiteral('\n').isPresent()) return Optional.of(s + "\n");
        return Optional.empty();
    }

    public Optional<String> strip(Supplier<Optional<String>> parser, char... cs) {
        many(() -> parseCharLiteral(cs));
        Optional<String> opt = parser.get();
        many(() -> parseCharLiteral(cs));
        return opt;

    }

    /**
     * Tries the given parser and returns its value, resets after
     * @param parser the parser to try
     * @return the return value of the parser
     */
    public <T> Optional<T> undo(Supplier<Optional<T>> parser) {
        Streamable oldStream = stream.copy();
        try {
            return parser.get();
        } catch (ArrayIndexOutOfBoundsException | NoSuchElementException e) {
            return Optional.empty();
        } finally {
            stream = oldStream;
        }
    }
}
