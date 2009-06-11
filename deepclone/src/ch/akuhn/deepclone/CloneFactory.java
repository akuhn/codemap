package ch.akuhn.deepclone;

import java.util.IdentityHashMap;
import java.util.Map;

/** Deep-clones any object. Uses unsafe reflection to clone any objects, no matter whether cloneable or not.
 * Does not call any methods, neither constructors nor accessors nor <code>clone</code> methods.
 *<p>
 * <i>Fluent naming convention:</i> Instances of this class should be named <tt>deep</tt>, in order to complete the fluent naming
 * of the <tt>deep.clone()</tt> method.
 * 
 * @author Adrian Kuhn
 *
 */
public class CloneFactory {
    
    private Map<Object,Object> done = new IdentityHashMap<Object,Object>();
    private DeepCloneStrategyCache cache = new DeepCloneStrategyCache();
    
    private Object clone0(Object object) throws Exception {
	if (object == null) return null;
	Object clone = done.get(object);
	if (clone != null) return clone;
	clone = cache.lookup(object).makeClone(object, this);
	done.put(object, clone);
	return clone;
    }

    @SuppressWarnings("unchecked")
    public <T> T clone(T original) throws DeepCloneException {
	try {
	    return (T) clone0(original);
	} catch (DeepCloneException ex) {
	    throw ex;
        } catch (Throwable ex) {
	    throw new DeepCloneException(ex);
        }
    }
    
    public static <T> T deepClone(T object) {
	return new CloneFactory().clone(object);
    }

}
