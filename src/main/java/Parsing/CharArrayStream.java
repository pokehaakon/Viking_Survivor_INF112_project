package Parsing;

public class CharArrayStream implements Streamable {
    private final char[] text;
    private int head = 0;
    private int lineCount = 0;
    private int colCount = 0;
    public CharArrayStream(String s) {
        text = s.toCharArray();
    }
    public CharArrayStream(char[] s) {
        text = s;
    }

    private CharArrayStream(char[] text, int head, int lineCount, int colCount) {
        this.text = text;
        this.head = head;
        this.lineCount = lineCount;
        this.colCount = colCount;
    }
    @Override
    public Streamable copy() {
        return new CharArrayStream(text, head, lineCount, colCount);
    }

    @Override
    public char getCurrent() {
        if (atEOF()) return 0;
        return text[head];
    }

    @Override
    public char next() {
        if (text[head] == '\n') {
            lineCount++;
            colCount = 0;
        } else {
            colCount++;
        }
        return text[head++];
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
        return String.valueOf(getCurrent());
    }
}
