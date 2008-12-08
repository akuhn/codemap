package ch.akuhn.util;

import java.io.IOException;

public class PrintOn implements Appendable {

    private Appendable buf;

    public PrintOn() {
        this(new StringBuilder());
    }

    public PrintOn(Appendable buf) {
        this.buf = buf;
    }

    public final PrintOn append(char c) {
        try {
            buf.append(c);
        } catch (IOException ex) {
            throw Throw.exception(ex);
        }
        return this;
    }

    public final PrintOn append(CharSequence string) {
        try {
            buf.append(string);
        } catch (IOException ex) {
            throw Throw.exception(ex);
        }
        return this;
    }

    public final PrintOn append(CharSequence string, int start, int end) {
        try {
            buf.append(string, start, end);
        } catch (IOException ex) {
            throw Throw.exception(ex);
        }
        return this;
    }

    public final PrintOn cr() {
        return append('\n');
    }

    public final PrintOn print(double value) {
        return append(Double.toString(value));
    }

    public final PrintOn print(int value) {
        return append(Integer.toString(value));
    }

    public final PrintOn print(Object object) {
        return append(String.valueOf(object));
    }

    public final PrintOn space() {
        return append(' ');
    }

    public final PrintOn tab() {
        return append('\t');
    }

    @Override
    public String toString() {
        return buf.toString();
    }

}
