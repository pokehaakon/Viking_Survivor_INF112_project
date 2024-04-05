package Parsing;

import Parsing.CharArrayStream;
import Parsing.Parser;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

//    @BeforeEach
//    void setUp() {
//    }

    public static Parser parserFromString(String s) {
        return new Parser(new CharArrayStream(s));
    }

    public static <T> void assertPresence(Supplier<Optional<T>> sup, T expected) {
        Optional<T> opt = sup.get();
        assertTrue(opt.isPresent());
        assertEquals(expected, opt.get());
    }

    public static <T> void assertEmpty(Supplier<Optional<T>> sup) {
        assertTrue(sup.get().isEmpty());
    }

    @Test
    void parseComment() {
        Parser m = parserFromString("""
            #this is a comment
            # this is another comment, starting with a space
            #\tthis is another comment, starting with a tab
            this is not a comment
            """);

        assertPresence(m::parseComment, "this is a comment");
        assertPresence(m::parseComment, " this is another comment, starting with a space");
        assertPresence(m::parseComment, "\tthis is another comment, starting with a tab");
        assertEmpty(m::parseComment);
    }

    @Test
    void Try() {
        //covered with other tests
    }

    @Test
    void choose() {
        Parser m = parserFromString("""
            #comment
            123
            #123
            """);

        Supplier<Optional<String>>[] suppliers = new Supplier[] {m::parseComment, m::numbers};

        assertPresence(() -> m.choose(suppliers), "comment");
        assertPresence(() -> m.choose(suppliers), "123");
        assertEmpty(() -> m.choose(suppliers));
        m.next();
        assertPresence(() -> m.choose(suppliers), "123");

    }

    @Test
    void parseUntilLiteral() {
        String text = """
            this should all be read by
            the first parser until it hits m
            now we test the array version which stops at m
            and stops at q this parses until EOF
            """;
        Parser m = parserFromString(text);

        Optional<String> opt;

        assertPresence(
                () -> m.parseUntilLiteral('m'),
                "this should all be read by\nthe first parser until it hits "
        );
        m.next(); //skips the 'm'
        assertPresence(
                () -> m.parseUntilLiteral('m', 'q'),
                "\nnow we test the array version which stops at "
        );
        m.next(); //skips the 'm'
        assertPresence(
                () -> m.parseUntilLiteral('m', 'q'),
                "\nand stops at "
        );
        assertPresence(
                () -> m.parseUntilLiteral('w'),
                "q this parses until EOF\n"
        );
        m.reset();
        assertPresence(
                () -> m.parseUntilLiteral('A', 'B'),
                text
        );
    }

    @Test
    void parseCharLiteral() {
        Parser m = parserFromString("thiss");

        assertPresence(() -> m.parseCharLiteral('t'),"t");
        assertPresence(() -> m.parseCharLiteral('h'),"h");
        assertEmpty(() -> m.parseCharLiteral('h'));
        assertPresence(() -> m.parseCharLiteral('i', 's'),"i");
        assertPresence(() -> m.parseCharLiteral('i', 's'),"s");
        assertEmpty(() -> m.parseCharLiteral('a', 'b'));
    }

    @Test
    void parseStringLiteral() {
        Parser m = parserFromString("""
            this should have been read by the first parser, and these the second ones
            and this should not be parsed
            """);

        String[] strings = new String[] {
                " have been read by the first parser,",
                " and these the second ones"
        };

        assertPresence(() -> m.parseStringLiteral("this should"), "this should");
        assertPresence(() -> m.parseStringLiteral(strings), strings[0]);
        assertPresence(() -> m.parseStringLiteral(strings), strings[1]);
        assertEmpty(() -> m.parseStringLiteral(strings));
    }


    @Test
    void parseEmptyLine() {
        Parser m = parserFromString("""
                \t  \t
                    \t
                \t not empty line!
                """);

        assertPresence(m::parseEmptyLine, "\t  \t\n");
        assertPresence(m::parseEmptyLine, "    \t\n");
        assertEmpty(m::parseEmptyLine);
    }

    @Test
    void many() {
        Parser m = parserFromString("""
            #this is a comment
            # this is another comment, starting with a space
            """);

        Supplier<Optional<String>> supplier = m::parseComment;

        Optional<List<String>> oComments = m.many(supplier);
        assertTrue(oComments.isPresent());
        List<String> comments = oComments.get();
        assertEquals(2, comments.size());

        assertEquals(comments.get(0), "this is a comment");
        assertEquals(comments.get(1), " this is another comment, starting with a space");

        oComments = m.many(supplier);
        assertTrue(oComments.isPresent());
        assertEquals(0, oComments.get().size());
    }

    @Test
    void some() {
        Parser m = parserFromString("""
            #this is a comment
            # this is another comment, starting with a space
            """);
        Optional<String> opt;

        Supplier<Optional<String>> supplier = m::parseComment;

        Optional<List<String>> oComments = m.some(supplier);
        assertTrue(oComments.isPresent());
        List<String> comments = oComments.get();
        assertEquals(2, comments.size());

        assertEquals(comments.get(0), "this is a comment");
        assertEquals(comments.get(1), " this is another comment, starting with a space");

        oComments = m.some(supplier);
        assertTrue(oComments.isEmpty());
    }

    @Test
    void letter() {
        Parser m = parserFromString("a.1b");

        assertPresence(m::letter, "a");
        assertEmpty(m::letter);
        m.next();
        assertEmpty(m::letter);
        m.next();
        assertPresence(m::letter, "b");
    }

    @Test
    void letters() {
        Parser m = parserFromString("abc.1bc");

        assertPresence(m::letters, "abc");
        assertEmpty(m::letters);
        m.next();
        assertEmpty(m::letters);
        m.next();
        assertPresence(m::letters, "bc");
    }

    @Test
    void number() {
        Parser m = parserFromString("12a.9");

        assertPresence(m::number, "1");
        assertPresence(m::number, "2");
        assertEmpty(m::number);
        m.next();
        assertEmpty(m::number);
        m.next();
        assertPresence(m::number, "9");
    }

    @Test
    void numbers() {
        Parser m = parserFromString("123a,23");

        assertPresence(m::numbers, "123");
        assertEmpty(m::numbers);
        m.next();
        assertEmpty(m::numbers);
        m.next();
        assertPresence(m::numbers, "23");
    }

    @Test
    void parseCharFromFunction() {
        //covered by other tests
    }

    @Test
    void parseStringFromFunction() {
        //covered by other tests
    }

}