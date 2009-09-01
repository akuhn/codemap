package org.codemap.communication.messages;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.codemap.communication.StringShare;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.sync.SerializationException;

/**
 * TODO check if we should implement IModelChangeMessage, like
 * @see org.eclipse.ecf.docshare.messages.Message
 * 
 * @author deif
 */
public abstract class Message implements Serializable {

    private static final long serialVersionUID = 5539065281698328158L;
    
    public final ID senderID;
    public final ID receiverID;

    public Message(ID senderID, ID receiverID) {
        this.senderID = senderID;
        this.receiverID = receiverID;
    }
    
    /**
     * Deserialize in to SelectionMessage.
     * @param bytes
     * @return SelectionMessage
     * @throws SerializationException
     */
    public static Message deserialize(byte[] bytes) throws SerializationException {
        try {
            final ByteArrayInputStream bins = new ByteArrayInputStream(bytes);
            final ObjectInputStream oins = new ObjectInputStream(bins);
            return (Message) oins.readObject();
        } catch (final Exception e) {
            throw new SerializationException("could not deserialize message", e);
        }
    }    
    
    public byte[] serialize() throws SerializationException {
        try {
            final ByteArrayOutputStream bos = new ByteArrayOutputStream();
            final ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(this);
            return bos.toByteArray();
        } catch (final Exception e) {
            throw new SerializationException("could not serialize message.", e);
        }
    }

    @Override
    public String toString() {
        return "message from: " + senderID.getName() + " to: " + receiverID.getName();
    }

    public abstract void applyOn(StringShare share);

}
