package Parsing;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class GenericParser<StreamType, ReturnType> {
    protected Streamable<StreamType> stream;
    private final Function<List<StreamType>, ReturnType> wrapper;

    private final Function<String, List<StreamType>> stringToList;

    public interface ThrowingSupplier<T> {
        T get() throws ParsingException;
    }

    public GenericParser(
            String filename,
            Function<String, Streamable<StreamType>> createStream,
            Function<List<StreamType>, ReturnType> wrapper,
            Function<String, List<StreamType>> stringToList) {
        this.stream = createStream.apply(filename);
        this.wrapper = wrapper;
        this.stringToList = stringToList;

    }

    public GenericParser(
            Streamable<StreamType> stream,
            Function<List<StreamType>, ReturnType> wrapper,
            Function<String, List<StreamType>> stringToList) {
        this.stream = stream;
        this.wrapper = wrapper;
        this.stringToList = stringToList;
    }

    @SafeVarargs
    private ReturnType wrap(StreamType... cs) {
        return wrapper.apply(List.of(cs));
    }
    private ReturnType wrap(Iterable<StreamType> itr) {return wrapper.apply(iterableToList(itr));}

    private ReturnType wrap(List<StreamType> ls) {
        return wrapper.apply(ls);
    }

    private <T> List<T> iterableToList(Iterable<T> itr) {
        List<T> ls = new ArrayList<>();
        for (T c : itr) ls.add(c);
        return ls;
    }

    /**
     * Gives next Character, unless at EOF
     * @return the next character
     */
    public ReturnType next() throws ParsingException {
        //if (stream.atEOF()) throw new ParsingException();
        return wrap(stream.next());
    }

    /**
     * Gives next 'n' characters, returns Empty if it hits EOF
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
     * Tries the given parser, if it parses nothing resets as if noting happened
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

    public <T> ThrowingSupplier<T> iTry(ThrowingSupplier<T> parser) {
        return () -> Try(parser);
    }

    /**
     * Tries the parsers in order until one succeeds, resets between tries
     * @param parsers array of the parsers to try
     * @return the return value of the succeeding parser
     */
    @SafeVarargs
    public final <T> T choose(ThrowingSupplier<T>... parsers) throws ParsingException {
        for (ThrowingSupplier<T> p : parsers) {
            try {
                return Try(p);
            } catch (ParsingException ignored){}
        }

        throw new ParsingException();
    }

    @SafeVarargs
    public final <T> ThrowingSupplier<T> ichoose(ThrowingSupplier<T>... parsers) {
        return () -> choose(parsers);
    }

    @SafeVarargs
    public final ReturnType parseUntilLiteral(StreamType... ms) throws ParsingException {
        if (stream.atEOF()) throw new ParsingException();
        for (StreamType m : ms)
            if (stream.getCurrent().equals(m)) throw new ParsingException();

        List<StreamType> b = new ArrayList<>();
        while (!stream.atEOF()) {
            StreamType r = stream.getCurrent();
            for (StreamType m : ms)
                if (r.equals(m)) return wrap(b);
            stream.next();
            b.add(r);
        }
        return wrap(b);
    }

    @SafeVarargs
    public final ThrowingSupplier<ReturnType> iparseUntilLiteral(StreamType... ms) {
        return () -> parseUntilLiteral(ms);
    }

    public ReturnType parseLiteral(StreamType c) throws ParsingException {
        if (!stream.getCurrent().equals(c)) throw new ParsingException();
        stream.next();
        return wrap(c);
    }

    public ThrowingSupplier<ReturnType> iparseLiteral(StreamType c) {
        return () -> parseLiteral(c);
    }

    @SafeVarargs
    public final ReturnType parseLiteral(StreamType... chars) throws ParsingException {
        StreamType m = stream.getCurrent();
        for (StreamType c : chars) {
            if (!m.equals(c)) continue;
            stream.next();
            return wrap(c);
        }
        throw new ParsingException();
    }

    @SafeVarargs
    public final ThrowingSupplier<ReturnType> iparseLiteral(StreamType... chars) {
        return () -> parseLiteral(chars);
    }

    @SafeVarargs
    public final ReturnType parseStringLiteral(List<StreamType>... strings) throws ParsingException {
        for (List<StreamType> s : strings) {
            Streamable<StreamType> cStream = stream.copy();
            //char[] cString = s.toCharArray();
            boolean found = true;
            for (StreamType streamType : s) {
                if (streamType.equals(cStream.next())) continue;
                found = false;
                break;
            }
            if (!found) continue;
            stream = cStream;
            return wrap(s);
        }
        throw new ParsingException();
    }

    @SafeVarargs
    public final ThrowingSupplier<ReturnType> iparseStringLiteral(List<StreamType>... strings) {
        return () -> parseStringLiteral(strings);
    }

    @SafeVarargs
    public final ReturnType parseStringLiteral(Iterable<StreamType>... strings) throws ParsingException {
        List<StreamType>[] arr = new List[strings.length];
        int i = 0;
        for(Iterable<StreamType> itr : strings) {
            arr[i++] = iterableToList(itr);
        }
        return parseStringLiteral(arr);
    }

    @SafeVarargs
    public final ThrowingSupplier<ReturnType> iparseStringLiteral(Iterable<StreamType>... strings) {
        return () -> parseStringLiteral(strings);
    }

    public final ReturnType parseStringLiteral(String... strings) throws ParsingException {
        if (stringToList == null) throw new RuntimeException("String parsing not defined for this parser");
        List<StreamType>[] arr = new List[strings.length];
        int i = 0;
        for(String str : strings) {
            arr[i++] = stringToList.apply(str);
        }
        return parseStringLiteral(arr);
    }

    public final ThrowingSupplier<ReturnType> iparseStringLiteral(String... strings) {
        return () -> parseStringLiteral(strings);
    }

    /**
     * Runs the given parser zero or more times
     * @param parser the parser to apply
     * @return
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

    public <T> Supplier<List<T>> imany(ThrowingSupplier<T> parser) {
        return () -> many(parser);
    }

    /**
     * Runs the given parser one or more times
     * @param parser
     * @return
     */
    public <T> List<T> some(ThrowingSupplier<T> parser) throws ParsingException {
        return Try(() ->  {
            List<T> out = new ArrayList<>();
            //first
            out.add(parser.get());

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

    public <T> ThrowingSupplier<List<T>> isome(ThrowingSupplier<T> parser) {
        return () -> some(parser);
    }

    public ReturnType parseLiteralFromFunction(Function<StreamType, Boolean> f) throws ParsingException {
        if (!f.apply(stream.getCurrent())) {
            throw new ParsingException();
        }
        return wrap(stream.next());
    }

    public ThrowingSupplier<ReturnType> iparseLiteralFromFunction(Function<StreamType, Boolean> f) {
        return () -> parseLiteralFromFunction(f);
    }

    public ReturnType parseStringFromFunction(Function<StreamType, Boolean> f) throws ParsingException {
        List<StreamType> b = new ArrayList<>();
        while (!stream.atEOF()) {
            if (!f.apply(stream.getCurrent())) break;
            b.add(stream.getCurrent());
            stream.next();
        }
        if (b.isEmpty()) throw new ParsingException();
        return wrap(b);
    }

    public ThrowingSupplier<ReturnType> iparseStringFromFunction(Function<StreamType, Boolean> f) {
        return () -> parseStringFromFunction(f);
    }

    @SafeVarargs
    public final <T> T strip(ThrowingSupplier<T> parser, StreamType... cs) throws ParsingException {
        many(() -> parseLiteral(cs));
        T opt = parser.get();
        many(() -> parseLiteral(cs));
        return opt;
    }

    @SafeVarargs
    public final <T> ThrowingSupplier<T> istrip(ThrowingSupplier<T> parser, StreamType... cs) {
        return () -> strip(parser, cs);
    }

    /**
     * Tries the given parser and returns its value, resets after
     * @param parser the parser to try
     * @return the return value of the parser
     */
    public <T> T undo(ThrowingSupplier<T> parser) throws ParsingException {
        Streamable<StreamType> oldStream = stream.copy();
        try {
            return parser.get();
        } catch (ArrayIndexOutOfBoundsException | NoSuchElementException e) {
            throw new ParsingException();
        } finally {
            stream = oldStream;
        }
    }

    public <T> ThrowingSupplier<T> iundo(ThrowingSupplier<T> parser)  {
        return () -> undo(parser);
    }

    public <T> T orElse(ThrowingSupplier<T> parser, T other) {
        try {
            return parser.get();
        } catch (ParsingException ignored) {}
        return other;
    }

    public <T> Supplier<T> iorElse(ThrowingSupplier<T> parser, T other) {
        return () -> orElse(parser, other);
    }

    public <T> T error() throws ParsingException {
        throw new ParsingException();
    }

    public <T> Optional<T> test(ThrowingSupplier<T> parser) {
        try {
            return Optional.of(Try(parser));
        } catch (ParsingException e) {
            return Optional.empty();
        }
    }

    public <T> void shouldError(ThrowingSupplier<T> parser) throws ParsingException {
        try {
            parser.get();
        } catch (ParsingException e) {
            return;
        }
        error();
    }

    public <T> void Void(ThrowingSupplier<T> parser) {
        try {parser.get();} catch (ParsingException ignored) {}
    }

}
