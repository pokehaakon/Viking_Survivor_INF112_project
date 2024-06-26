package Parsing.Stream;

import Parsing.Parser.ParsingException;

public interface Streamable<T> {
    /**
     * @return copy of stream
     */
    Streamable<T> copy();

    /**
     * @return the char at the current position
     */
    T getCurrent();

    /**
     * returns the char at the current position, and then increments to the next element
     * @return the char at the current position
     */
    T next() throws ParsingException;

    /**
     *
     * @return returns true if stream is empty
     */
    boolean atEOF();

    /**
     * resets the stream to the original position
     */
    void reset();

    /**
     * @return the line number of the current position
     */
    int getLine();

    /**
     * @return the position of the current position within the current line
     */
    int getLinePos();

    /**
     * Gets debug information about the current state of the stream
     * @return
     */
    String getDebugInfo();
}
