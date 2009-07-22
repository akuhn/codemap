package ch.akuhn.foreach;

public class Times {

    public static Iterable<?> repeat(int times) {
        return For.range(times);
    }
    
}
