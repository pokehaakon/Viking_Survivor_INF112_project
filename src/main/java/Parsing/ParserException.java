package Parsing;

public class ParserException extends RuntimeException {
    public ParserException(GenericParser<?, ?> parser, String msg, Streamable<?> s) {
        super(
                msg + "\n\n" +
                        "Parse Error at: l" +
                        parser.stream.getLine() + " c" +
                        parser.stream.getLinePos() + "\n\n" +
                        parser.stream.getDebugInfo()
        );
    }
}
