package Parsing.Parser;

import Parsing.Parser.ParsingException;

public interface ThrowingSupplier<T> {
    T get() throws ParsingException;
}
