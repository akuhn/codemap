package sandbox;

import java.io.File;

public class Main {
    
    public static void main(String... args){
        File file = new File("direct.csv");
        System.out.println(file.getAbsolutePath());
        System.out.println(file.exists());
    }

}
