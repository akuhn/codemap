package ch.deif.meander.mds;

public class MDSParameters {
    
    public boolean debug = false;
    
    public int cycles = 50;
    
    public double learning_rate = 1;
    public double start_annealing_ratio = .5;    
    public double m_exponent = 8.0;
    
    public void useOriginal() {
        debug = true;
        cycles = 10;
        m_exponent = 1.0;
    }

}
