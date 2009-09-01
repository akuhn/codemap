package org.codemap.ecftest;

import org.eclipse.ecf.core.identity.ID;

public class StartMessage extends Message {

    private static final long serialVersionUID = 6915879353034965689L;
    
    public StartMessage(ID senderID, ID receiverID) {
        super(senderID, receiverID);
    }

    @Override
    public String toString() {
        return "(Start)" + super.toString();
    }

    @Override
    public void applyOn(StringShare share) {
        share.handleRemoteStart(this);
        System.out.println(this);
    }
    
}
