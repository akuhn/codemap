package ch.akuhn.hapax.util;

public interface ResourceStream {

    public int nextInt();

    public void close();

    public double nextDouble();

    public ResourceStream put(double value);
    
    public ResourceStream put(int value);

}
