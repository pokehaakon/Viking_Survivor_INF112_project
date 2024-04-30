package Parsing.Parser;

public interface ThrowingSupplier<T> {
    T get() throws ParsingException;
}
