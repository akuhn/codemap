package example;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;

import edu.mit.tedlab.Svdlibc;

public class Main {

    public static final String MATRIX = "12 9 "
        + "1 0 0 1 0 0 0 0 0 "
        + "1 0 1 0 0 0 0 0 0 " 
        + "1 1 0 0 0 0 0 0 0 "
        + "0 1 1 0 1 0 0 0 0 " 
        + "0 1 1 2 0 0 0 0 0 " 
        + "0 1 0 0 1 0 0 0 0 " 
        + "0 1 0 0 1 0 0 0 0 " 
        + "0 0 1 1 0 0 0 0 0 " 
        + "0 1 0 0 0 0 0 0 1 "  
        + "0 0 0 0 0 1 1 1 0 " 
        + "0 0 0 0 0 0 1 1 1 " 
        + "0 0 0 0 0 0 0 1 1 "; 
    
    public static void main(String... args) throws IOException {
        new Main().run();
    }
    
    public void run() throws IOException {
        InputStream stdin = System.in;
        PrintStream stdout = System.out;
        System.setIn(new ByteArrayInputStream(MATRIX.getBytes()));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));
        Svdlibc lib = new Svdlibc();
        lib.run("edu.mit.tedlab.Svdlibc -r dt -v 3 -".split(" "), null);
        System.setIn(stdin);
        System.setOut(stdout);
        System.out.println(baos);
    }
    
}
