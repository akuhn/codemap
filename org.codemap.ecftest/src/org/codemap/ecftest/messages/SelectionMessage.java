package org.codemap.ecftest.messages;

import java.util.Collection;

import org.codemap.ecftest.StringShare;
import org.eclipse.ecf.core.identity.ID;

public class SelectionMessage extends Message {
    
    private static final long serialVersionUID = -6238144999021023659L;
    public final Collection<String> selection;
    
    public SelectionMessage(ID senderID, ID receiverID, Collection<String> selection) {
        super(senderID, receiverID);
        this.selection = selection;
    }

    @Override
    public String toString() {
        return super.toString() + " with selection: " + selection;
    }

    @Override
    public void applyOn(StringShare share) {
        System.out.println(this);
    }

}
