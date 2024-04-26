package Parsing.Stream;

import Parsing.Parser.ParsingException;

import java.util.Arrays;
import java.util.List;

public class CharArrayStream implements Streamable<Character> {
    private final char[] text;
    private int head = 0;
    private int lineCount = 0;
    private int colCount = 0;
    private char currentChar;
    public CharArrayStream(String s) {
        text = s.replace("\r", "").toCharArray();
        currentChar = text[head];
    }
    public CharArrayStream(char[] s) {
        text = s;
        currentChar = text[head];
    }

    private CharArrayStream(char[] text, int head, int lineCount, int colCount, char currentChar) {
        this.text = text;
        this.head = head;
        this.lineCount = lineCount;
        this.colCount = colCount;
        this.currentChar = currentChar;
    }
    @Override
    public Streamable<Character> copy() {
        return new CharArrayStream(text, head, lineCount, colCount, currentChar);
    }

    @Override
    public Character getCurrent() {
        if (atEOF()) return 0;
        return currentChar;
    }

    @Override
    public Character next() throws ParsingException {
        if (atEOF()) throw new ParsingException("Reached EOF");
        if (currentChar == '\n') {
            lineCount++;
            colCount = 0;
        } else {
            colCount++;
        }
        var temp = currentChar;
//        head++;
//        if (text.length == head) {
//            currentChar = 0;
//        } else {
//            currentChar = text[head];
//        }
        currentChar = text.length == ++head ? 0 : text[head];
        //currentChar = text.length != head + 1 ? text[++head] : 0;
        return temp;
    }

    @Override
    public boolean atEOF() {
        return text.length == head;
    }

    @Override
    public void reset() {
        head = 0;
        lineCount = 0;
        colCount = 0;
        currentChar = text[head];
    }

    @Override
    public int getLine() {
        return lineCount;
    }

    @Override
    public int getLinePos() {
        return colCount;
    }

    @Override
    public String getDebugInfo() {
        StringBuilder b = new StringBuilder();
        for (int i = head - colCount; i < text.length; i++) {
            if (text[i] == '\n') break;
            b.append(text[i]);
        }
        b.append('\n');
        for (int i = 0; i < colCount; i++)
            b.append(' ');
        b.append('^');

    return b.toString() + (atEOF() ? "\n: atEOF" : "");
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof CharArrayStream otherStream) {
            return this == other
                    || head == otherStream.head
                    && Arrays.equals(otherStream.getText(), this.text);
        }
        return false;
    }

    public char[] getText() {
        return text;
    }
}
