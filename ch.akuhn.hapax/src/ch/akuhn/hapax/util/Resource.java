package ch.akuhn.hapax.util;

public interface Resource {

    public abstract ResourceStream open();

    public abstract ResourceStream writeStream();

}
