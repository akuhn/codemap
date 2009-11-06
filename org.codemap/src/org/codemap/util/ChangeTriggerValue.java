package org.codemap.util;

import ch.akuhn.values.ReferenceValue;
import ch.akuhn.values.Value;

public class ChangeTriggerValue {
    
    private ReferenceValue<Object> trigger = new ReferenceValue<Object>();

    public void setChanged() {
        trigger.setValue(new Object());
    }

    public Value<?> value() {
        return trigger;
    }
}
