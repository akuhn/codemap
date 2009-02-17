package example;

import ch.akuhn.hapax.util.CharStream;

public class Sandbox {

    public static void main(String[] args) {
        CharStream in = CharStream.fromString("abc");
        System.out.println(in.next());
        System.out.println(in.next());
        System.out.println(in.next());
        System.out.println(in.next());
    }
    
}
