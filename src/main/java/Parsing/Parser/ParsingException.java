package Parsing.Parser;

public class ParsingException extends Exception {
    public final String msg;
    public ParsingException(String msg) {
        super(msg);
        this.msg = msg;
    }
}
