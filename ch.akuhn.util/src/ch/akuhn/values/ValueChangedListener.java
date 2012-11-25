package ch.akuhn.values;

import java.util.EventListener;
import java.util.EventObject;

public interface ValueChangedListener extends EventListener {

    public void valueChanged(EventObject event);
    
}
