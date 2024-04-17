package Parsing;

public class CharArrayStream implements Streamable<Character> {
    private final char[] text;
    private int head = 0;
    private int lineCount = 0;
    private int colCount = 0;
    public CharArrayStream(String s) {
        text = s.replace("\r", "").toCharArray();
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
    public Streamable<Character> copy() {
        return new CharArrayStream(text, head, lineCount, colCount);
    }

    @Override
    public Character getCurrent() {
        if (atEOF()) return 0;
        return text[head];
    }

    @Override
    public Character next() {
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
