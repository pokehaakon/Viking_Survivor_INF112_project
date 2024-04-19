package Parsing;

public interface ThrowingSupplier<T> {
    T get() throws ParsingException;
}
