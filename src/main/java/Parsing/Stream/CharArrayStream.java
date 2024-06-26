package Parsing.Stream;

import Parsing.Parser.ParsingException;

import java.util.Arrays;

public class CharArrayStream implements Streamable<Character> {
    private final char[] text;
    private final String name;
    private int head = 0;
    private int lineCount = 0;
    private int colCount = 0;
    private char currentChar;
    public CharArrayStream(String s, String name) {
        text = s.replace("\r", "").toCharArray();
        currentChar = text[head];
        this.name = name;
    }
    public CharArrayStream(char[] s, String name) {
        text = s;
        currentChar = text[head];
        this.name = name;
    }

    private CharArrayStream(char[] text, int head, int lineCount, int colCount, char currentChar, String name) {
        this.text = text;
        this.head = head;
        this.lineCount = lineCount;
        this.colCount = colCount;
        this.currentChar = currentChar;
        this.name = name;
    }
    @Override
    public Streamable<Character> copy() {
        return new CharArrayStream(text, head, lineCount, colCount, currentChar, name);
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
        currentChar = text.length == ++head ? 0 : text[head];
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
        b.append(" ".repeat(Math.max(0, colCount)));
        b.append('^');

    return b + (atEOF() ? "\n: atEOF" : "") + "\n in file: " + name;
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
