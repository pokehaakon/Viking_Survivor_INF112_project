package Parsing;

import javax.swing.*;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public abstract class  GenericParser<StreamType, ReturnType> {
    protected Streamable<StreamType> stream;
    private final Function<List<StreamType>, ReturnType> wrapper;

    private final Function<String, List<StreamType>> stringToList;

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
    public Optional<ReturnType> next() {
        if (stream.atEOF()) return Optional.empty();
        return Optional.of(wrap(stream.next()));
    }

    /**
     * Gives next 'n' characters, returns Empty if it hits EOF
     * @param n number of characters to get
     * @return the next 'n' characters
     */
    public Optional<ReturnType> next(int n) {
        if (stream.atEOF()) return Optional.empty();
        List<StreamType> b = new ArrayList<>();
        for (int i = 0; i < n; i++) b.add(stream.next());
        return Optional.of(wrap(b));
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
    public <T> Optional<T> Try(Supplier<Optional<T>> parser) {
        Streamable<StreamType> oldStream = stream.copy();
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

    @SafeVarargs
    public final Optional<ReturnType> parseUntilLiteral(StreamType... ms) {
        for (StreamType m : ms)
            if (stream.getCurrent().equals(m)) return Optional.empty();

        List<StreamType> b = new ArrayList<>();
        while (!stream.atEOF()) {
            StreamType r = stream.getCurrent();
            for (StreamType m : ms)
                if (r.equals(m)) return Optional.of(wrap(b));
            stream.next();
            b.add(r);
        }
        return Optional.of(wrap(b));
    }

    public Optional<ReturnType> parseLiteral(StreamType c) {
        if (!stream.getCurrent().equals(c)) return Optional.empty();
        stream.next();
        return Optional.of(wrap(c));
    }

    @SafeVarargs
    public final Optional<ReturnType> parseLiteral(StreamType... chars) {
        StreamType m = stream.getCurrent();
        for (StreamType c : chars) {
            if (!m.equals(c)) continue;
            stream.next();
            return Optional.of(wrap(c));
        }
        return Optional.empty();
    }

    @SafeVarargs
    public final Optional<ReturnType> parseStringLiteral(List<StreamType>... strings) {
        for (List<StreamType> s : strings) {
            Streamable<StreamType> cStream = stream.copy();
            //char[] cString = s.toCharArray();
            boolean found = true;
            for (StreamType streamType : s) {
                if (streamType == cStream.next()) continue;
                found = false;
                break;
            }
            if (!found) continue;
            stream = cStream;
            return Optional.of(wrap(s));
        }
        return Optional.empty();
    }

    @SafeVarargs
    public final Optional<ReturnType> parseStringLiteral(Iterable<StreamType>... strings) {
        Iterable<StreamType>[] arr = new Iterable[strings.length];
        int i = 0;
        for(Iterable<StreamType> itr : strings) {
            arr[i++] = iterableToList(itr);
        }
        return parseStringLiteral(arr);
    }

    @SafeVarargs
    public final Optional<ReturnType> parseStringLiteral(String... strings) {
        if (stringToList == null) throw new RuntimeException("String parsing not defined for this parser");
        List<StreamType>[] arr = new List[strings.length];
        int i = 0;
        for(String str : strings) {
            arr[i++] = stringToList.apply(str);
        }
        return parseStringLiteral(arr);
    }

    /**
     * Runs the given parser zero or more times
     * @param parser the parser to apply
     * @return
     */
    public <T>  Optional<List<T>> many(Supplier<Optional<T>> parser) {
        List<T> out = new ArrayList<>();

        Optional<T> parsedString;
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
    public <T> Optional<List<T>> some(Supplier<Optional<T>> parser) {
        return Try(() ->  {
            List<T> out = new ArrayList<>();
            //first
            Optional<T> parsedString = parser.get();
            if (parsedString.isEmpty()) {
                return Optional.empty();
            }
            out.add(parsedString.get());

            //others
            while ((parsedString = parser.get()).isPresent()){
                out.add(parsedString.get());
            }
            return Optional.of(out);
        });
    }

    public Optional<ReturnType> parseLiteralFromFunction(Function<StreamType, Boolean> f) {
        if (!f.apply(stream.getCurrent())) {
            return Optional.empty();
        }
        return Optional.of(wrap(stream.next()));
    }

    public Optional<ReturnType> parseStringFromFunction(Function<StreamType, Boolean> f) {
        List<StreamType> b = new ArrayList<>();
        while (!stream.atEOF()) {
            if (!f.apply(stream.getCurrent())) break;
            b.add(stream.getCurrent());
            stream.next();
        }
        if (b.isEmpty()) return Optional.empty();
        return Optional.of(wrap(b));
    }

    @SafeVarargs
    public final <T> Optional<T> strip(Supplier<Optional<T>> parser, StreamType... cs) {
        many(() -> parseLiteral(cs));
        Optional<T> opt = parser.get();
        many(() -> parseLiteral(cs));
        return opt;
    }

    /**
     * Tries the given parser and returns its value, resets after
     * @param parser the parser to try
     * @return the return value of the parser
     */
    public <T> Optional<T> undo(Supplier<Optional<T>> parser) {
        Streamable<StreamType> oldStream = stream.copy();
        try {
            return parser.get();
        } catch (ArrayIndexOutOfBoundsException | NoSuchElementException e) {
            return Optional.empty();
        } finally {
            stream = oldStream;
        }
    }
}
