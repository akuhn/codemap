package org.codemap.kdtree;

 /**
  * KeyDuplicateException is thrown when the <TT>KDTree.insert</TT> method
  * is invoked on a key already in the KDTree.
  *
  * @author      Simon Levy
  * @version     %I%, %G%
  * @since JDK1.2 
  */

public class KeyDuplicateException extends KDException {

    protected KeyDuplicateException(String string) {
	super("Key already in tree: " + string);
    }

    // arbitrary; every serializable class has to have one of these
    public static final long serialVersionUID = 1L;
}
