package ch.deif.meander.main;

public class DebugLog implements Log {

    @Override
    public void print(Object... args) {
        StringBuffer buf = new StringBuffer();
        for (Object object : args) {
            buf.append(object.toString());
        }
        System.out.println(buf);
    }
}
