package example;

import java.io.IOException;


import edu.mit.tedlab.Svdlibc;

public class ReadFromStdin {

    public static void main(String... args) {
        new Svdlibc().run("edu.mit.tedlab.Svdlibc -r dt -v 3 -".split(" "), null);
    }    
    
}
