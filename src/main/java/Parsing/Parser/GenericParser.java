package Parsing.Parser;

import Parsing.Stream.Streamable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;


public abstract class GenericParser<StreamType, ReturnType> {
    private final Function<List<StreamType>, ReturnType> wrapper;
    private final Function<String, List<StreamType>> stringToList;
    public Streamable<StreamType> stream;

    /**
     * Create a new GenericParser
     *
     * @param stream       a stream of {@link StreamType}
     * @param wrapper      the wrapper function to transform lists of {@link StreamType} into {@link ReturnType}
     * @param stringToList function used to interpret a {@link String} as a list of {@link StreamType}
     */
    public GenericParser(
            Streamable<StreamType> stream,
            Function<List<StreamType>, ReturnType> wrapper,
            Function<String, List<StreamType>> stringToList) {
        this.stream = stream;
        this.wrapper = wrapper;
        this.stringToList = stringToList;
    }

    /**
     * Wraps the StreamType literals into ReturnType
     *
     * @param cs array of literals
     * @return the value of the wrapper
     */
    @SafeVarargs
    private ReturnType wrap(StreamType... cs) {
        return wrapper.apply(List.of(cs));
    }
//    private ReturnType wrap(Iterable<StreamType> itr) {return wrapper.apply(iterableToList(itr));}

    /**
     * Wraps the list of StreamType literals into ReturnType
     *
     * @param ls list of literals
     * @return
     */
    private ReturnType wrap(List<StreamType> ls) {
        return wrapper.apply(ls);
    }

//    private <T> List<T> iterableToList(Iterable<T> itr) {
//        List<T> ls = new ArrayList<>();
//        for (T c : itr) ls.add(c);
//        return ls;
//    }

    /**
     * Gives next character, fail if at EOF
     *
     * @return the next character
     */
    public ReturnType next() throws ParsingException {
        //if (stream.atEOF()) throw new ParsingException();
        return wrap(stream.next());
    }

    /**
     * Gives next 'n' characters, fails if it hits EOF
     *
     * @param n number of characters to get
     * @return the next 'n' characters
     */
    public ReturnType next(int n) throws ParsingException {
        //if (stream.atEOF()) throw new ParsingException();
        List<StreamType> b = new ArrayList<>();
        for (int i = 0; i < n; i++) b.add(stream.next());
        return wrap(b);
    }

    /**
     * resets the parser to the original state
     */
    public void reset() {
        stream.reset();
    }

    /**
     * Tries the given parser, if the parser fails, resets the stream and fails
     *
     * @param parser the parser to try
     * @return the return value of the parser
     */
    public <T> T Try(ThrowingSupplier<T> parser) throws ParsingException {
        Streamable<StreamType> oldStream = stream.copy();
        try {
            return parser.get();
        } catch (ParsingException e) {
            stream = oldStream;
            throw e;
        }

    }

    /**
     * Create a ThrowingSupplier of {@link #Try(ThrowingSupplier parser)} with 'parser' as input
     *
     * @param parser the parser to input
     * @return () -> Try(parser)
     */
    public final <T> ThrowingSupplier<T> iTry(ThrowingSupplier<T> parser) {
        return () -> Try(parser);
    }

    /**
     * Tries the parsers in order until one succeeds
     *
     * @param parsers array of the parsers to try
     * @return the return value of the first succeeding parser
     */
    @SafeVarargs
    public final <T> T choose(ThrowingSupplier<T>... parsers) throws ParsingException {
        List<String> messages = new ArrayList<>();
        for (ThrowingSupplier<T> p : parsers) {
            try {
                return Try(p);
            } catch (ParsingException e) {
                messages.add(e.msg);
            }
        }
        return error("failed choosing: \n" + String.join(" ,", messages));
    }


    /**
     * Create a ThrowingSupplier of {@link #choose(ThrowingSupplier... parsers)} with 'parsers' as input
     *
     * @param parsers the parsers to input
     * @return () -> choose(parsers)
     */
    @SafeVarargs
    public final <T> ThrowingSupplier<T> ichoose(ThrowingSupplier<T>... parsers) {
        return () -> choose(parsers);
    }

    /**
     * Parses literals until it hits one of the literals in 'literals', fails if the first literal is in 'literals', or if the start is EOF
     *
     * @param literals the literals to parse to
     * @return the wrapped value of the parsed literals
     */
    @SafeVarargs
    public final ReturnType parseUntilLiteral(StreamType... literals) throws ParsingException {
        if (stream.atEOF()) error("Reached EOF");
        for (StreamType m : literals)
            if (stream.getCurrent().equals(m))
                error("Did not parse any input, with literals: " + Arrays.toString(literals));

        List<StreamType> b = new ArrayList<>();
        while (!stream.atEOF()) {
            StreamType r = stream.getCurrent();
            for (StreamType m : literals)
                if (r.equals(m)) return wrap(b);
            stream.next();
            b.add(r);
        }
        return wrap(b);
    }

    /**
     * Create a ThrowingSupplier of {@link #parseUntilLiteral(StreamType... literals)} with 'literals' as input
     *
     * @param literals the literals to input
     * @return () -> parseUntilLiteral(ms)
     */
    @SafeVarargs
    public final ThrowingSupplier<ReturnType> iparseUntilLiteral(StreamType... literals) {
        return () -> parseUntilLiteral(literals);
    }

    /**
     * Parses literal 'literal', fails if 'literal' is not parsed
     *
     * @param literal the literal to parse
     * @return the wrapped value of the parsed literal
     */
    public ReturnType parseLiteral(StreamType literal) throws ParsingException {
        if (!stream.getCurrent().equals(literal)) error("Failed parsing literal: " + literal);
        stream.next();
        return wrap(literal);
    }

    /**
     * Create a ThrowingSupplier of {@link #parseLiteral(StreamType literal)} with 'literal' as input
     *
     * @param literal the literal to input
     * @return () -> parseLiteral(literal)
     */
    public ThrowingSupplier<ReturnType> iparseLiteral(StreamType literal) {
        return () -> parseLiteral(literal);
    }


    /**
     * Parses one of literals in an array of literals, fails if nothing is parsed
     *
     * @param literals the array of literals
     * @return the wrapped value of the parsed literal
     */
    @SafeVarargs
    public final ReturnType parseLiteral(StreamType... literals) throws ParsingException {
        StreamType m = stream.getCurrent();
        for (StreamType literal : literals) {
            if (!m.equals(literal)) continue;
            stream.next();
            return wrap(literal);
        }
        return error("Failed parsing any literal: " + Arrays.toString(literals));
    }

    /**
     * Create a ThrowingSupplier of {@link #parseLiteral(StreamType... literals)} with 'literals' as input
     *
     * @param literals the array of literals
     * @return () -> parseLiteral(literals)
     */
    @SafeVarargs
    public final ThrowingSupplier<ReturnType> iparseLiteral(StreamType... literals) {
        return () -> parseLiteral(literals);
    }

    /**
     * Parses one of the literal strings in 'literalStrings' in order, fail if no string is parsed
     *
     * @param literalStrings array of Lists of StreamType
     * @return the wrapped value of the parsed literal string
     */
    @SafeVarargs
    public final ReturnType parseStringLiteral(List<StreamType>... literalStrings) throws ParsingException {
        for (List<StreamType> literalString : literalStrings) {
            Streamable<StreamType> cStream = stream.copy();
            //char[] cString = s.toCharArray();
            boolean found = true;
            for (StreamType literal : literalString) {
                if (literal.equals(cStream.next())) continue;
                found = false;
                break;
            }
            if (!found) continue;
            stream = cStream;
            return wrap(literalString);
        }
        return error("Failed to parse any string: " + Arrays.toString(literalStrings));
    }

    /**
     * Create a ThrowingSupplier of {@link #parseStringLiteral(List... literalStrings)} with 'literalStrings' as input
     *
     * @param literalStrings the array to input
     * @return () -> parseStringLiteral(literalStrings)
     */
    @SafeVarargs
    public final ThrowingSupplier<ReturnType> iparseStringLiteral(List<StreamType>... literalStrings) {
        return () -> parseStringLiteral(literalStrings);
    }


    /**
     * Parses one of the strings in 'strings' in order, fail if no string is parsed
     * Special method for when StreamType 'stringToList' exists.
     *
     * @param strings array of strings
     * @return the parsed string
     */
    public final ReturnType parseStringLiteral(List<String> strings) throws ParsingException {
        if (stringToList == null) throw new RuntimeException("String parsing not defined for this parser");
        List<StreamType>[] arr = new List[strings.size()];
        int i = 0;
        for (String str : strings) {
            arr[i++] = stringToList.apply(str);
        }
        return parseStringLiteral(arr);
    }

    /**
     * Create a ThrowingSupplier of {@link #parseStringLiteral(List strings)} with 'strings' as input
     *
     * @param strings the array to input
     * @return () -> parseStringLiteral(strings)
     */
    public final ThrowingSupplier<ReturnType> iparseStringLiteral(List<String> strings) {
        return () -> parseStringLiteral(strings);
    }

    /**
     * Parses one of the strings in 'strings' in order, fail if no string is parsed
     * Special method for when StreamType 'stringToList' exists.
     *
     * @param strings array of strings
     * @return the parsed string
     */
    public final ReturnType parseStringLiteral(String... strings) throws ParsingException {
        if (stringToList == null) throw new RuntimeException("String parsing not defined for this parser");
        List<StreamType>[] arr = new List[strings.length];
        int i = 0;
        for (String str : strings) {
            arr[i++] = stringToList.apply(str);
        }
        return parseStringLiteral(arr);
    }

    /**
     * Create a ThrowingSupplier of {@link #parseStringLiteral(String... strings)} with 'strings' as input
     *
     * @param strings the array to input
     * @return () -> parseStringLiteral(strings)
     */
    public final ThrowingSupplier<ReturnType> iparseStringLiteral(String... strings) {
        return () -> parseStringLiteral(strings);
    }

    /**
     * Runs the given parser zero or more times until it fails, and accumulate the return values of the parser
     *
     * @param parser the parser to apply
     * @return list of the return values of the parser
     */
    public <T> List<T> many(ThrowingSupplier<T> parser) {
        List<T> out = new ArrayList<>();

        while (true) {
            try {
                out.add(parser.get());
            } catch (ParsingException ignored) {
                break;
            }
        }

        return out;
    }

    /**
     * Create a Supplier of {@link #many(ThrowingSupplier parser)} with 'parser' as input
     *
     * @param parser the parser to input
     * @return () -> many(parser)
     */
    public <T> Supplier<List<T>> imany(ThrowingSupplier<T> parser) {
        return () -> many(parser);
    }

    /**
     * Runs the given parser one or more times until it fails, and accumulate the return values of the parser.
     * Fail if parser cannot parse at least one time.
     *
     * @param parser the parser to apply
     * @return list of the return values of the parser
     */
    public <T> List<T> some(ThrowingSupplier<T> parser) throws ParsingException {
        return Try(() -> {
            List<T> out = new ArrayList<>();
            //first
            try {
                out.add(parser.get());
            } catch (ParsingException e) {
                error("Failed to parse 'some' with error: \n" + e.msg);
            }

            //others
            while (true) {
                try {
                    out.add(parser.get());
                } catch (ParsingException ignored) {
                    break;
                }
            }

            return out;
        });
    }

    /**
     * Create a ThrowingSupplier of {@link #some(ThrowingSupplier parser)} with 'parser' as input
     *
     * @param parser the parser to input
     * @return () -> some(parser)
     */
    public <T> ThrowingSupplier<List<T>> isome(ThrowingSupplier<T> parser) {
        return () -> some(parser);
    }

    /**
     * Parses literal 'l' where 'f(l) => true', fails if no 'f(l) => false'
     *
     * @param f the function to use
     * @return the parsed literal
     */
    public ReturnType parseLiteralFromFunction(Function<StreamType, Boolean> f) throws ParsingException {
        if (!f.apply(stream.getCurrent())) {
            error("Could not parse any input with function: " + f);
        }
        return wrap(stream.next());
    }

    /**
     * Create a ThrowingSupplier of {@link #parseLiteralFromFunction(Function f) parseLiteralFromFunction(Function&lt;Streamtype, Boolean&gt; f)} with 'f' as input
     *
     * @param f the function to input
     * @return () -> parseLiteralFromFunction(f)
     */
    public ThrowingSupplier<ReturnType> iparseLiteralFromFunction(Function<StreamType, Boolean> f) {
        return () -> parseLiteralFromFunction(f);
    }

    /**
     * Parses literals 'l' where 'f(l) => true', fails if no literal is parsed
     *
     * @param f the function to use
     * @return wrapped value of the parsed literals
     */
    public ReturnType parseStringFromFunction(Function<StreamType, Boolean> f) throws ParsingException {
        List<StreamType> b = new ArrayList<>();
        while (!stream.atEOF()) {
            if (!f.apply(stream.getCurrent())) break;
            b.add(stream.getCurrent());
            stream.next();
        }
        if (b.isEmpty()) error("Could not parse any input with function: " + f);
        return wrap(b);
    }

    /**
     * Create a ThrowingSupplier of {@link #parseStringFromFunction(Function f) parseLiteralFromFunction(Function&lt;Streamtype, Boolean&gt; f)} with 'f' as input
     *
     * @param f the function to input
     * @return () -> parseStringFromFunction(f)
     */
    public ThrowingSupplier<ReturnType> iparseStringFromFunction(Function<StreamType, Boolean> f) {
        return () -> parseStringFromFunction(f);
    }

    /**
     * First runs 'parseLiteral(literals)' then runs 'parser' then runs 'parseLiteral(literals)', fails if 'parser' fails
     *
     * @param parser   the parser to use
     * @param literals the literals to strip
     * @return the return value of the parser
     */
    @SafeVarargs
    public final <T> T strip(ThrowingSupplier<T> parser, StreamType... literals) throws ParsingException {
        many(() -> parseLiteral(literals));
        T opt = parser.get();
        many(() -> parseLiteral(literals));
        return opt;
    }

    /**
     * Create a ThrowingSupplier of {@link #strip(ThrowingSupplier parser, StreamType... literals)} with 'parser' and 'literals' as input
     *
     * @param parser   the parser to input
     * @param literals the literals to input
     * @return () -> strip(parser, literals)
     */
    @SafeVarargs
    public final <T> ThrowingSupplier<T> istrip(ThrowingSupplier<T> parser, StreamType... literals) {
        return () -> strip(parser, literals);
    }

    /**
     * Tries the given parser and returns its value, resets after even if it fails
     *
     * @param parser the parser to try
     * @return the return value of the parser
     */
    public <T> T undo(ThrowingSupplier<T> parser) throws ParsingException {
        Streamable<StreamType> oldStream = stream.copy();
        try {
            return parser.get();
        } finally {
            stream = oldStream;
        }
    }

    /**
     * Create a ThrowingSupplier of {@link #undo(ThrowingSupplier parser)} with 'parser' as input
     *
     * @param parser the parser to input
     * @return () -> undo(parser)
     */
    public <T> ThrowingSupplier<T> iundo(ThrowingSupplier<T> parser) {
        return () -> undo(parser);
    }

    /**
     * If the given parser fails, return 'other' else return the value of the parser
     *
     * @param parser the parser to run
     * @param other  the default value
     * @return
     */
    public <T> T orElse(ThrowingSupplier<T> parser, T other) {
        try {
            return parser.get();
        } catch (ParsingException ignored) {
        }
        return other;
    }

    /**
     * Create a Supplier of {@link #orElse(ThrowingSupplier parser, T other)} with 'parser' and 'other' as input
     *
     * @param parser the parser to input
     * @param other  default value to input
     * @return () -> orElse(parser, other)
     */
    public <T> Supplier<T> iorElse(ThrowingSupplier<T> parser, T other) {
        return () -> orElse(parser, other);
    }

    /**
     * Throws a new ParsingException
     *
     * @return never returns
     * @throws ParsingException
     */
    public <T> T error(String msg) throws ParsingException {
        throw new ParsingException(msg);
    }

    /**
     * Tests the parser, if it fails returns Optional.empty(), else it returns Optional of the parsed value
     *
     * @param parser the parser to test
     * @return Optional of parser result
     */
    public <T> Optional<T> test(ThrowingSupplier<T> parser) {
        try {
            return Optional.of(Try(parser));
        } catch (ParsingException e) {
            return Optional.empty();
        }
    }

    /**
     * Calls {@link #error(String msg)} if 'parser' does not fail.
     *
     * @param parser the parser to test
     */
    public <T> void shouldError(ThrowingSupplier<T> parser) throws ParsingException {
        try {
            parser.get();
        } catch (ParsingException e) {
            return;
        }
        error("Did not error when expected by parser: " + parser);
    }

    /**
     * Runs 'parser' and voids the result and any {@link ParsingException} thrown by the parser
     *
     * @param parser the parser to run
     */
    public <T> void Void(ThrowingSupplier<T> parser) {
        try {
            parser.get();
        } catch (ParsingException ignored) {
        }
    }

    public void parseEOF() throws ParsingException {
        if (!stream.atEOF()) error("Expected to parse EOF");
    }

}
