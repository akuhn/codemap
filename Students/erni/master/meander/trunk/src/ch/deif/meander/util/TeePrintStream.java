package ch.deif.meander.util;

import java.io.PrintStream;
import java.util.Locale;

public class TeePrintStream extends PrintStream {

    private PrintStream delegate, stream;

    public TeePrintStream(PrintStream stream, PrintStream delegate) {
        super(stream);
        this.delegate = delegate;
        this.stream = stream;
    }
    
    @Override
    public PrintStream append(char c) {
        delegate.append(c);
        return stream.append(c);
    }

    @Override
    public PrintStream append(CharSequence csq, int start, int end) {
        if (true) throw new Error();
        return delegate.append(csq, start, end);
    }

    @Override
    public PrintStream append(CharSequence csq) {
        if (true) throw new Error();
        return delegate.append(csq);
    }

    @Override
    public boolean checkError() {
        if (true) throw new Error();
        return delegate.checkError();
    }

    @Override
    public void close() {
        // GOTCHA dont close the delegate
        stream.close();
        super.close();
    }

    @Override
    public void flush() {
        if (true) throw new Error();
        delegate.flush();
    }

    @Override
    public PrintStream format(Locale l, String format, Object... args) {
        if (true) throw new Error();
        return delegate.format(l, format, args);
    }

    @Override
    public PrintStream format(String format, Object... args) {
        if (true) throw new Error();
        return delegate.format(format, args);
    }

    @Override
    public void print(boolean b) {
        if (true) throw new Error();
        delegate.print(b);
    }

    @Override
    public void print(char c) {
        delegate.print(c);
        stream.print(c);
    }

    @Override
    public void print(char[] s) {
        if (true) throw new Error();
        delegate.print(s);
    }

    @Override
    public void print(double d) {
        if (true) throw new Error();
        delegate.print(d);
    }

    @Override
    public void print(float f) {
        stream.print(f);
        delegate.print(f);
    }

    @Override
    public void print(int i) {
        delegate.print(i);
        stream.print(i);
    }

    @Override
    public void print(long l) {
        if (true) throw new Error();
        delegate.print(l);
    }

    @Override
    public void print(Object obj) {
        if (true) throw new Error();
        delegate.print(obj);
    }

    @Override
    public void print(String s) {
        delegate.print(s);
        stream.print(s);
    }

    @Override
    public PrintStream printf(Locale l, String format, Object... args) {
        if (true) throw new Error();
        return delegate.printf(l, format, args);
    }

    @Override
    public PrintStream printf(String format, Object... args) {
        if (true) throw new Error();
        return delegate.printf(format, args);
    }

    @Override
    public void println() {
        if (true) throw new Error();
        delegate.println();
    }

    @Override
    public void println(boolean x) {
        if (true) throw new Error();
        delegate.println(x);
    }

    @Override
    public void println(char x) {
        if (true) throw new Error();
        delegate.println(x);
    }

    @Override
    public void println(char[] x) {
        if (true) throw new Error();
        delegate.println(x);
    }

    @Override
    public void println(double x) {
        if (true) throw new Error();
        delegate.println(x);
    }

    @Override
    public void println(float x) {
        if (true) throw new Error();
        delegate.println(x);
    }

    @Override
    public void println(int x) {
        if (true) throw new Error();
        delegate.println(x);
    }

    @Override
    public void println(long x) {
        if (true) throw new Error();
        delegate.println(x);
    }

    @Override
    public void println(Object x) {
        if (true) throw new Error();
        delegate.println(x);
    }

    @Override
    public void println(String x) {
        if (true) throw new Error();
        delegate.println(x);
    }

    @Override
    public void write(byte[] buf, int off, int len) {
        delegate.write(buf, off, len);
        stream.write(buf, off, len);
    }

    @Override
    public void write(int b) {
        if (true) throw new Error();
        delegate.write(b);
    }
    
    
    
}
