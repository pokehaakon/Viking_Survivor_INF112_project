package Parsing;

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
    T next();

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
     * Gets debug information about the current char
     * @return
     */
    String getDebugInfo();
}
