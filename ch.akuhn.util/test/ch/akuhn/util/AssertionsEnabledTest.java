
package ch.akuhn.util;

import org.junit.Test;

public class AssertionsEnabledTest {

    @Test(expected = AssertionError.class)
    public void assetionsEnabled() {
        assert false;
    }
}
