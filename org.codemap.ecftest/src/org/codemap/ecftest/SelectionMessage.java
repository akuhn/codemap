package org.codemap.ecftest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.sync.SerializationException;

public class SelectionMessage implements Serializable {

    private static final long serialVersionUID = 5539065281698328158L;
    
    public final ID senderID;
    public final ID receiverID;
    public final String fromUsername;

    public SelectionMessage(ID senderID, String fromUser, ID receiverID) {
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.fromUsername = fromUser;
    }
    
    /**
     * Deserialize in to SelectionMessage.
     * @param bytes
     * @return SelectionMessage
     * @throws SerializationException
     */
    public static SelectionMessage deserialize(byte[] bytes) throws SerializationException {
        try {
            final ByteArrayInputStream bins = new ByteArrayInputStream(bytes);
            final ObjectInputStream oins = new ObjectInputStream(bins);
            return (SelectionMessage) oins.readObject();
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

}
