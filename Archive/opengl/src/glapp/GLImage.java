package glapp;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import javax.imageio.*;

/**
 * Loads an image from file, stores pixels as ARGB int array, and RGBA ByteBuffer for
 * use in OpenGL.  Can convert images to power-of-2 dimensions for textures.
 * <P>
 * Static functions are included to load, flip and convert pixel arrays.
 * <P>
 * napier at potatoland dot org 
 */

public class GLImage {
    public int h = 0;
    public int w = 0;
    public ByteBuffer pixelBuffer = null;  // store pixels as bytes in GL_RGBA format
    public int[] pixels = null;            // store pixels as ARGB integers

    // Used by GLApp.drawImage() to hold image as a texture
    // The texture will be allocated only if needed (see GLApp.drawImage()
    public int textureHandle;              // this image stored as a texture
    public int textureW;                   // the power-of-two dimensions that can hold this image (ie. an image 250x200 would have textureSize 256)
    public int textureH;


    public GLImage() {
    }

    /**
     * Load pixels from an image file.  Flip Y axis.  Convert to RGBA format.
     * @param imgName
     */
    public GLImage(String imgName)
    {
		BufferedImage img = loadJavaImage(imgName);
        if (makeGLImage(img,true,false)) {
			GLApp.msg("GLImage(String): loaded " + imgName + ", width=" + w + " height=" + h);
		}
    }

    /**
     * Load pixels from an image file.  Convert to RGBA format.
     * @param imgName
     */
    public GLImage(String imgName, boolean flipYaxis, boolean convertPow2)
    {
		BufferedImage img = loadJavaImage(imgName);
        if (makeGLImage(img,flipYaxis,convertPow2)) {
			GLApp.msg("GLImage(String,bool,bool): loaded " + imgName + ", width=" + w + " height=" + h);
		}
	}

    /**
     * Create GLImage from image file bytes (the contents of a jpg, gif or png file). Flip Y axis.
     * @param pixels
     * @param w
     * @param h
     */
    public GLImage(byte[] bytes) {
        BufferedImage img = makeBufferedImage(bytes);
        if (makeGLImage(img,true,false)) {
			GLApp.msg("GLImage(byte[]): loaded image from bytes[" + bytes.length + "]");
		}
		else {
			GLApp.err("GLImage(byte[]): could not create Image from bytes[" + bytes.length + "]");
		}
    }

    /**
     * Create GLImage from image file bytes (the contents of a jpg, gif or png file).
     * @param pixels
     * @param w
     * @param h
     */
    public GLImage(byte[] bytes, boolean flipYaxis, boolean convertPow2) {
        BufferedImage img = makeBufferedImage(bytes);
        if (makeGLImage(img,flipYaxis,convertPow2)) {
			GLApp.msg("GLImage(byte[],bool,bool): loaded image from bytes[" + bytes.length + "]");
		}
		else {
			GLApp.err("GLImage(byte[],bool,bool): could not create Image from bytes[" + bytes.length + "]");
		}
    }

    /**
     * Make a BufferedImage from the contents of an image file.  
     * @param imageFileContents  byte array containing the guts of a JPG, GIF, or PNG
     */
    public BufferedImage makeBufferedImage(byte[] imageFileContents) {
    	BufferedImage bi = null;
    	try {
        	InputStream in = new ByteArrayInputStream(imageFileContents);
            bi = javax.imageio.ImageIO.read(in);
        }
    	catch (IOException ioe) {
    		GLApp.err("GLImage.makeBufferedImage(): " + ioe);
    	}  
        return bi;
    }
    
    /**
     * Create GLImage from pixels passed in a ByteBuffer.  This is a non-standard approach
     * that may give unpredictable results.
     * @param pixels
     * @param w
     * @param h
     */
    public GLImage(ByteBuffer gl_pixels, int w, int h) {
		if (gl_pixels != null) {
       		this.pixelBuffer = gl_pixels;
        	this.pixels = null;
        	this.h = h;
        	this.w = w;
		}
    }

    /**
     * return true if image has been loaded successfully
     * @return
     */
    public boolean isLoaded()
    {
        return (pixelBuffer != null);
    }

    /**
     * Flip the image pixels vertically
     */
    public void flipPixels()
    {
        pixels = flipPixels(pixels, w, h);
    }

    /**
     * Load an image from the given filename.  If convertToPow2 is true then convert
     * the image to a power of two.  Store pixels as ARGB ints in the pixels array
     * and as RGBA bytes in the pixelBuffer ByteBuffer.  Hold onto image width/height.
     * @param imgName
     */
    public boolean makeGLImage(BufferedImage tmpi, boolean flipYaxis, boolean convertToPow2) {
        if (tmpi != null) {
            if (flipYaxis) {
	            tmpi = flipY(tmpi);
			}
            if (convertToPow2) {
            	tmpi = convertToPowerOf2(tmpi);
            }
            w = tmpi.getWidth(null);
            h = tmpi.getHeight(null);
            pixels = getImagePixels(tmpi);  // pixels in default Java ARGB format
            pixelBuffer = convertImagePixelsRGBA(pixels,w,h,false);  // convert to bytes in RGBA format
            textureW = GLApp.getPowerOfTwoBiggerThan(w); // the texture size big enough to hold this image
            textureH = GLApp.getPowerOfTwoBiggerThan(h); // the texture size big enough to hold this image
            //GLApp.msg("GLImage: loaded " + imgName + ", width=" + w + " height=" + h);
            return true;
        }
        else {
            //GLApp.err("GLImage: FAILED TO LOAD IMAGE " + imgName);
            pixels = null;
            pixelBuffer = null;
            h = w = 0;
            return false;
        }
    }


	/**
	 * Load a BufferedImage from the given image file name.  File can be in the local filesytem,
	 * in the applet folder, or in a jar.
	 */
    public BufferedImage loadJavaImage(String imgName) {
    	BufferedImage tmpi = null;
    	try {
    		tmpi = ImageIO.read(GLApp.getInputStream(imgName));
    	}
    	catch (Exception e) {
    		GLApp.err("GLImage.loadJavaImage() exception: FAILED TO LOAD IMAGE " + e);
    	}
    	return tmpi;
	}

    /**
     * Return the Image pixels in default Java int ARGB format.
     * @return
     */
    public static int[] getImagePixels(Image image)
    {
    	int[] pixelsARGB = null;
        if (image != null) {
        	int imgw = image.getWidth(null);
        	int imgh = image.getHeight(null);
        	pixelsARGB = new int[ imgw * imgh];
            PixelGrabber pg = new PixelGrabber(image, 0, 0, imgw, imgh, pixelsARGB, 0, imgw);
            try {
                pg.grabPixels();
            }
            catch (Exception e) {
            	GLApp.err("Pixel Grabbing interrupted!");
                return null;
            }
        }
        return pixelsARGB;
    }

    /**
     * return int array containing pixels in ARGB format (default Java byte order).
     */
    public int[] getPixelInts()
    {
        return pixels;
    }

    /**
     * return ByteBuffer containing pixels in RGBA format (commmonly used in OpenGL).
     */
    public ByteBuffer getPixelBytes()
    {
        return pixelBuffer;
    }

    //========================================================================
    //
    // Static convertion functions to prepare pixels for use in OpenGL
    //
    //========================================================================

    /**
     * Flip an array of pixels vertically
     * @param imgPixels
     * @param imgw
     * @param imgh
     * @return int[]
     */
    public static int[] flipPixels(int[] imgPixels, int imgw, int imgh)
    {
        int[] flippedPixels = null;
        if (imgPixels != null) {
            flippedPixels = new int[imgw * imgh];
            for (int y = 0; y < imgh; y++) {
                for (int x = 0; x < imgw; x++) {
                    flippedPixels[ ( (imgh - y - 1) * imgw) + x] = imgPixels[ (y * imgw) + x];
                }
            }
        }
        return flippedPixels;
    }

    /**
     * Copy ARGB pixels to a ByteBuffer without changing the ARGB byte order. If used to make a
     * texture, the pixel format is GL12.GL_BGRA.  With this format we can leave pixels in ARGB
     * order (faster), but unfortunately I had problems building mipmaps in BGRA format
     * (GLU.gluBuild2DMipmaps() did not recognize GL_UNSIGNED_INT_8_8_8_8 and
     * GL_UNSIGNED_INT_8_8_8_8_REV types so screwed up the BGRA/ARGB byte order on Mac).
     *
     * @return ByteBuffer
     */
    public static ByteBuffer convertImagePixelsARGB(int[] jpixels, int imgw, int imgh, boolean flipVertically) {
    	// flip Y axis
        if (flipVertically) {
            jpixels = flipPixels(jpixels, imgw, imgh); // flip Y axis
        }
        // put int pixels into Byte Buffer
    	ByteBuffer bb = GLApp.allocBytes(jpixels.length * 4);  // 4 bytes per pixel
    	bb.asIntBuffer().put(jpixels);
        return bb;
    }

    /**
     * Convert ARGB pixels to a ByteBuffer containing RGBA pixels.  The GL_RGBA format is
     * a default format used in OpenGL 1.0, but requires that we move the Alpha byte for
     * each pixel in the image (slow).  Would be better to use OpenGL 1.2 GL_BGRA format
     * and leave pixels in the ARGB format (faster) but this pixel format caused problems
     * when creating mipmaps (see note above).
     * .<P>
     * If flipVertically is true, pixels will be flipped vertically (for OpenGL coord system).
     * @return ByteBuffer
     */
    public static ByteBuffer convertImagePixelsRGBA(int[] jpixels, int imgw, int imgh, boolean flipVertically) {
        byte[] bytes;     // will hold pixels as RGBA bytes
        if (flipVertically) {
            jpixels = flipPixels(jpixels, imgw, imgh); // flip Y axis
        }
        bytes = convertARGBtoRGBA(jpixels);
        return allocBytes(bytes);  // convert to ByteBuffer and return
    }

    /**
     * Convert pixels from java default ARGB int format to byte array in RGBA format.
     * @param jpixels
     * @return
     */
    public static byte[] convertARGBtoRGBA(int[] jpixels)
    {
        byte[] bytes = new byte[jpixels.length*4];  // will hold pixels as RGBA bytes
        int p, r, g, b, a;
        int j=0;
        for (int i = 0; i < jpixels.length; i++) {
            p = jpixels[i];
            a = (p >> 24) & 0xFF;  // get pixel bytes in ARGB order
            r = (p >> 16) & 0xFF;
            g = (p >> 8) & 0xFF;
            b = (p >> 0) & 0xFF;
            bytes[j+0] = (byte)r;  // fill in bytes in RGBA order
            bytes[j+1] = (byte)g;
            bytes[j+2] = (byte)b;
            bytes[j+3] = (byte)a;
            j += 4;
        }
        return bytes;
    }

    //========================================================================
    // Utility functions
    //========================================================================

    /**
     * Same function as in GLApp.java.  Allocates a ByteBuffer to hold the given
     * array of bytes.
     *
     * @param bytearray
     * @return  ByteBuffer containing the contents of the byte array
     */
    public static ByteBuffer allocBytes(byte[] bytearray) {
        ByteBuffer bb = ByteBuffer.allocateDirect(bytearray.length).order(ByteOrder.nativeOrder());
        bb.put(bytearray).flip();
        return bb;
    }

    /**
     * Scale this GLImage so width and height are powers of 2.  Recreate pixels and pixelBuffer.
     */
    public void convertToPowerOf2() {
    	// make BufferedImage from original pixels
    	BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
    	image.setRGB(0, 0, w, h, pixels, 0, w);

    	// scale into new image
    	BufferedImage scaledImg = convertToPowerOf2(image);

    	// resample pixel data
    	w = scaledImg.getWidth(null);
        h = scaledImg.getHeight(null);
        pixels = getImagePixels(scaledImg);  // pixels in default Java ARGB format
        pixelBuffer = convertImagePixelsRGBA(pixels,w,h,false);  // convert to bytes in RGBA format
        textureW = GLApp.getPowerOfTwoBiggerThan(w); // the texture size big enough to hold this image
        textureH = GLApp.getPowerOfTwoBiggerThan(h); // the texture size big enough to hold this image
    }

	/**
	 * Save an array of ARGB pixels to a PNG file.
	 * If flipY is true, flip the pixels on the Y axis before saving.
	 */
	public static void savePixelsToPNG(int[] pixels, int width, int height, String imageFilename, boolean flipY) {
		if (pixels != null && imageFilename != null) {
			if (flipY) {
				// flip the pixels vertically (opengl has 0,0 at lower left, java is upper left)
				pixels = GLImage.flipPixels(pixels, width, height);
			}
			try {
				// Create a BufferedImage with the RGB pixels then save as PNG
				BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
				image.setRGB(0, 0, width, height, pixels, 0, width);
				javax.imageio.ImageIO.write(image, "png", new File(imageFilename));
			}
			catch (Exception e) {
				GLApp.err("GLImage.savePixelsToPNG(" +imageFilename+ "): exception " + e);
			}
		}
	}

    //========================================================================
    // Static functions to flip and scale images
    //========================================================================

    /**
     * Scale the given BufferedImage to width and height that are powers of two.
     * Return the new scaled BufferedImage.
     */
    public static BufferedImage convertToPowerOf2(BufferedImage bsrc) {
        // find powers of 2 equal to or greater than current dimensions
        int newW = GLApp.getPowerOfTwoBiggerThan(bsrc.getWidth());
        int newH = GLApp.getPowerOfTwoBiggerThan(bsrc.getHeight());
        if (newW == bsrc.getWidth() && newH == bsrc.getHeight()) {
        	return bsrc;    // no change necessary
        }
        else {
	        AffineTransform at = AffineTransform.getScaleInstance((double)newW/bsrc.getWidth(),(double)newH/bsrc.getHeight());
	        BufferedImage bdest = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
	        Graphics2D g = bdest.createGraphics();
	        g.drawRenderedImage(bsrc,at);
	        return bdest;
        }
    }

    /**
     * Scale the given BufferedImage to the given width and height.
     * Return the new scaled BufferedImage.
     */
    public static BufferedImage scale(BufferedImage bsrc, int width, int height) {
        AffineTransform at = AffineTransform.getScaleInstance((double)width/bsrc.getWidth(),(double)height/bsrc.getHeight());
        BufferedImage bdest = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = bdest.createGraphics();
        g.drawRenderedImage(bsrc,at);
        return bdest;
    }

    /**
     * Flip the given BufferedImage vertically.
     * Return the new flipped BufferedImage.
     */
    public static BufferedImage flipY(BufferedImage bsrc) {
        AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
        tx.translate(0, -bsrc.getHeight(null));
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return op.filter(bsrc, null);
    }

}