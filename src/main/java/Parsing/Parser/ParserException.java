package Parsing.Parser;

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

    public ParserException(ParsingException e, GenericParser<?, ?> parser, String msg) {
        super(msg + "\n" + e.msg + "\n"
                + parser.stream.getLine() + " c"
                + parser.stream.getLinePos() + "\n\n"
                + parser.stream.getDebugInfo());

        message = msg + "\n\n" + e.msg;
    }
}
