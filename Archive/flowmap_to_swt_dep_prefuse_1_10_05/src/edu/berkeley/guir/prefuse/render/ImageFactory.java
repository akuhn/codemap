package edu.berkeley.guir.prefuse.render;

import java.awt.Component;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import edu.berkeley.guir.prefuse.VisualItem;
import edu.berkeley.guir.prefuse.graph.Entity;

/**
 * <p>Controls loading and management of images. Includes a size-configurable
 * LRU cache for managing loaded images. Also supports optional image scaling
 * of loaded images to cut down on memory and visualization operation costs.
 * </p>
 * 
 * <p>By default images are loaded upon first request. Use the
 * <code>preloadImages()</code> method to load images before they are
 * requested.</p>
 * 
 * @author newbergr
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class ImageFactory {
	
	private int m_imageCacheSize = 500;
	private int m_maxImageWidth  = 100;
	private int m_maxImageHeight = 100;
    private boolean m_asynch = true;
	
	//a nice LRU cache courtesy of java 1.4
	private Map imageCache =
		new LinkedHashMap((int) (m_imageCacheSize + 1 / .75F), .75F, true) {
			public boolean removeEldestEntry(Map.Entry eldest) {
				return size() > m_imageCacheSize;
			}
		};
	private Map loadMap = new HashMap(50);
    private Map loadingMap = new HashMap(50);
    private Set loadingSet = new HashSet(50);

	private final Component component = new Component() {};
	private final MediaTracker tracker = new MediaTracker(component);
	private int nextTrackerID = 0;

	/**
	 * Constructor. Assumes no scaling of loaded images.
	 */
	public ImageFactory() {
		this(-1,-1);
	} //
	
	/**
	 * Constructor. This instance will scale loaded images if they exceed the
	 * threshold arguments.
	 * @param maxImageWidth the maximum width of input images (-1 means no limit)
	 * @param maxImageHeight the maximum height of input images (-1 means no limit)
	 */
	public ImageFactory(int maxImageWidth, int maxImageHeight) {
		setMaxImageDimensions(maxImageWidth, maxImageHeight);
	} //

	/**
	 * Sets the maximum image dimensions of loaded images, images larger than
	 * these limits will be scaled to fit within bounds.
	 * @param width the maximum width of input images (-1 means no limit)
	 * @param height the maximum height of input images (-1 means no limit)
	 */
	public void setMaxImageDimensions(int width, int height) {
		m_maxImageWidth  = width;
		m_maxImageHeight = height;
	} //

	/**
	 * Sets the capacity of this factory's image cache
	 * @param size the new size of the image cache
	 */
	public void setImageCacheSize(int size) {
		m_imageCacheSize = size;
	} //

	/**
	 * Get the image associated with the given location string. If the image
	 * has already been loaded, it simply will return the image, otherwise it
	 * will load it from the specified location.
	 * 
	 * The imageLocation argument must be a valid resource string.
	 * 
	 * @param imageLocation the image location as a resource string.
	 * @return the corresponding image, if available
	 */
	public Image getImage(String imageLocation) {
		Image image = (Image) imageCache.get(imageLocation);
		if (image == null && !loadMap.containsKey(imageLocation)) {
			URL imageURL = getImageURL(imageLocation);
			if ( imageURL == null ) {
			    System.err.println("Null image: " + imageLocation);
				return null;
			}
			image = Toolkit.getDefaultToolkit().createImage(imageURL);

			// if set for synchronous mode, block for image to load.
            if ( !m_asynch ) {
                waitForImage(image);
                addImage(imageLocation, image);
            } else {
                int id = ++nextTrackerID;
                tracker.addImage(image, id);
                loadMap.put(imageLocation, new LoadMapEntry(id,image));    
            }
		} else if ( image == null && loadMap.containsKey(imageLocation) ) {
            LoadMapEntry entry = (LoadMapEntry)loadMap.get(imageLocation);
            if ( tracker.checkID(entry.id, true) ) {
                addImage(imageLocation, entry.image);
                loadMap.remove(imageLocation);
                tracker.removeImage(entry.image, entry.id);
            }
        } else {
            return image;
        }
		return (Image) imageCache.get(imageLocation);
	} //
    
    /**
     * Adds an image associated with a locaiton string to this factory's cache.
     * The image will be scaled as dictated by this factory's setting.
     * @param location the location string uniquely identifying the image
     * @param image the actual image
     * @return the final image added to the cache. This may be a scaled
     *  version of the original input image.
     */
    public Image addImage(String location, Image image) {
        if ( m_maxImageWidth > -1 || m_maxImageHeight > -1 ) {
            image = getScaledImage(image);
            image.getWidth(null); // trigger image load
        }
        imageCache.put(location, image);
        return image;
    } //
    
	/**
	 * Wait for an image to load.
	 * @param image the image to wait for
	 */
	protected void waitForImage(Image image) {
		int id = ++nextTrackerID;
		tracker.addImage(image, id);
		try {
			tracker.waitForID(id, 0);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		tracker.removeImage(image, id);
	} //

	/**
	 * Returns the URL for a location specified as a resource string.
	 * @param location the resource location string
	 * @return the corresponding URL
	 */
	protected URL getImageURL(String location) {
        URL url = null;
        if ( location.startsWith("http:/") ||
             location.startsWith("ftp:/")  ||
             location.startsWith("file:/") ) {
            try {
                url = new URL(location);
            } catch ( Exception e ) {
                e.printStackTrace();
            }
        } else {
            url = ImageFactory.class.getResource(location);
            if ( url==null && !location.startsWith("/") )
                url = ImageFactory.class.getResource("/"+location);
        }
        return url;
	} //
    
	/**
	 * Scales an image to fit within the current size thresholds.
	 * @param img the image to scale
	 * @return the scaled image
	 */
	protected Image getScaledImage(Image img) {		
		// resize image, if necessary, to conserve memory
		//  and reduce future scaling time
		int w = img.getWidth(null) - m_maxImageWidth;
		int h = img.getHeight(null) - m_maxImageHeight;

		if ( w > h && w > 0 && m_maxImageWidth > -1 ) {
			Image scaled = img.getScaledInstance(m_maxImageWidth, -1, Image.SCALE_SMOOTH);
			img.flush(); //waitForImage(scaled);
			return scaled;
		} else if ( h > 0 && m_maxImageHeight > -1 ) {
			Image scaled = img.getScaledInstance(-1, m_maxImageHeight, Image.SCALE_SMOOTH);
			img.flush(); //waitForImage(scaled);				
			return scaled;
		} else {
			return img;
		}
	} //
	
	/**
	 * Preloads images for use in a visualization. Images to load are
	 * determined by taking objects from the given iterator and retrieving
	 * the attribute of the specified value. The items in the iterator must
	 * be instances of either <code>Entity</code> or <code>VisualItem</code>.
	 * Images are loaded in the order specified by the iterator until the
	 * the iterator is empty or the maximum image cache size is met. Thus
	 * higher priority images should appear sooner in the iteration.
	 * @param iter an Iterator of <code>Entity</code> and/or 
	 *  <code>VisualItem</code> instances
	 * @param attr the attribute that contains the image location
	 */
	public void preloadImages(Iterator iter, String attr) {
        boolean synch = m_asynch;
        m_asynch = false;
        
		String loc = null;
		while ( iter.hasNext() && imageCache.size() <= m_imageCacheSize ) {
			// get the string describing the image location
			Object o = iter.next();
			if ( o instanceof Entity ) {
				loc = ((Entity)o).getAttribute(attr);
			} else if ( o instanceof VisualItem ) {
				loc = ((VisualItem)o).getAttribute(attr);
			}
			if ( loc != null ) {
				getImage(loc);
			}
		}
        m_asynch = synch;
	} //
	
    private class LoadMapEntry {
        public int id;
        public Image image;
        public LoadMapEntry(int id, Image image) {
            this.id = id;
            this.image = image;
        }
    } //
    
} // end of class ImageFactory
