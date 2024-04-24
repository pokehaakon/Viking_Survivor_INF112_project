package Parsing.Parser;

import Parsing.Stream.Streamable;

public class ParserException extends RuntimeException {
    public String message;
    public ParserException(GenericParser<?, ?> parser, String msg) {
        super(
                msg + "\n\n" +
                        "Parse Error at: l" +
                        parser.stream.getLine() + " c" +
                        parser.stream.getLinePos() + "\n\n" +
                        parser.stream.getDebugInfo()
        );

        message = msg;
    }
}
