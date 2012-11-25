package org.codemap.communication.messages;

import org.codemap.communication.SelectionShare;
import org.eclipse.ecf.core.identity.ID;

public class StopMessage extends Message {

    private static final long serialVersionUID = 8171675195852556727L;

    public StopMessage(ID senderID, ID receiverID) {
        super(senderID, receiverID);
    }
    
    @Override
    public String toString() {
        return "(Stop)" + super.toString();
    }

    @Override
    public void applyOn(SelectionShare share) {
        share.handleRemoteStop(this);
        System.out.println(this);
    }

}
