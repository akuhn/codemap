package ch.deif.meander;


/**
 * Custom color class used to provide in-place modification of
 * color values for performance reasons
 */
public class MColor {
	  
    public static MColor GREEN() {
    	return new MColor(0, 255, 0);
    }
    
    public static MColor BLUE() {
    	return new MColor(0, 0, 255);
    }
    
    public static MColor YELLOW() {
    	return new MColor(255, 255, 0);
    }
	
	public int r;
	public int g;
	public int b;
	
	// 0 <= DEFAULT_FACTOR <= 1
	private static final double DEFAULT_FACTOR = 0.7;
	
	public MColor(int red, int green, int blue) {
		if ( red < 0 || red > 255 || green < 0 || green > 255 || blue < 0 || blue > 255 ) {
			throw new IllegalArgumentException("Invalid rgb range");			
		}
		r = red;
		g = green;
		b = blue;
	}
	
	/**
	 * handles an 32bit rgb
	 * mem layout: 8 bit alpha, 8 bit red, 8 bit g, 8 bit b
	 * aaaaaaaarrrrrrrrggggggggbbbbbbbb
	 * 
	 * alpha is all set to 1 if there is no alpha
	 * r needs 16 bit shift
	 * g needs 8 bit shift
	 * b needs no shift
	 */
	public int rgb() {
	    return 	0xFF000000 | r << 16 | g << 8 | b;	
	}
	
	public void darker() {
		this.darkerInternal(DEFAULT_FACTOR);
	}
	
	public void darker(double factor) {
		if ( factor < 0 || factor > 1) {
			throw new IllegalArgumentException("Invalid factor, must be in range 0 to 1");
		}
		this.darkerInternal(factor);
	}
	
	private void darkerInternal(double factor) {
		r = (int) Math.max(r*factor, 0);
		g = (int) Math.max(g*factor, 0);
		b = (int) Math.max(b*factor, 0);		
	}

	public void shade(double hillshade) {
		// TODO Auto-generated method stub
		
	}

}
