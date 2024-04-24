package Parsing.ObjectDefineParser;

public class Variable {
    Object value;

    private Variable(Object value) {
        this.value = value;
    }

    public Object get() {
        return value;
    }

    public static Variable of(Object value) {
        return new Variable(value);
    }

    @Override
    public String toString() {
        return "Variable(" + value + ")";
    }

}
