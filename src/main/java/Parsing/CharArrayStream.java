package Parsing;

import Coordinates.SwarmCoordinates;

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
        if (text.length == head + 1) throw new ParsingException();
        if (currentChar == '\n') {
            lineCount++;
            colCount = 0;
        } else {
            colCount++;
        }
        var temp = currentChar;
        currentChar = text[++head];
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
        return b.toString();
    }
}
