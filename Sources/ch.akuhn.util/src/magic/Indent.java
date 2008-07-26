package magic;

/** Indents lines while printing them. 
 * 
 * @author Adrian Kuhn
 *
 */

public class Indent {

    private String s;
    private final String tab;
    
    public Indent() {
        this("\t");
    }
    
    public Indent(String tab) {
        this.tab = tab;
        this.s = "";
    }
    
    public void inc() {
        s += tab;
    }
    
    public void dec() {
        if (done()) throw new IllegalStateException();
        s = s.substring(0, s.length() - tab.length());
    }
    
    public String toString() {
        return s;
    }
    
    public boolean done() {
        return s.length() == 0;
    }
    
}
