package org.codemap.util;

import java.util.ArrayList;
import java.util.List;

public class GoodColorGenerator {
    
    private List<MColor> cols;
    private int size;
    private int currentPos;
    
    protected static final float SATURATION = 0.5f;
    protected static final float BRIGHTNESS = 0.9f;

    public GoodColorGenerator(int n) {
        cols = new ArrayList<MColor>(n);
        for(int i = 0; i < n; i++) {
            cols.add(i, HSBtoRGB((float) i / (float) n, SATURATION, BRIGHTNESS));
        }
        size = n;
        currentPos = 0;
    }
    
    public MColor next() {
        MColor next = cols.get(currentPos);
        currentPos = (currentPos+1) % size;
        return next;
    }
    
    /**
     * copy-paste from java.awt.Color
     */
    protected MColor HSBtoRGB(float hue, float saturation, float brightness) {
        int r = 0, g = 0, b = 0;
        if (saturation == 0) {
            r = g = b = (int) (brightness * 255.0f + 0.5f);
        } else {
            float h = (hue - (float)Math.floor(hue)) * 6.0f;
            float f = h - (float)java.lang.Math.floor(h);
            float p = brightness * (1.0f - saturation);
            float q = brightness * (1.0f - saturation * f);
            float t = brightness * (1.0f - (saturation * (1.0f - f)));
            switch ((int) h) {
            case 0:
            r = (int) (brightness * 255.0f + 0.5f);
            g = (int) (t * 255.0f + 0.5f);
            b = (int) (p * 255.0f + 0.5f);
            break;
            case 1:
            r = (int) (q * 255.0f + 0.5f);
            g = (int) (brightness * 255.0f + 0.5f);
            b = (int) (p * 255.0f + 0.5f);
            break;
            case 2:
            r = (int) (p * 255.0f + 0.5f);
            g = (int) (brightness * 255.0f + 0.5f);
            b = (int) (t * 255.0f + 0.5f);
            break;
            case 3:
            r = (int) (p * 255.0f + 0.5f);
            g = (int) (q * 255.0f + 0.5f);
            b = (int) (brightness * 255.0f + 0.5f);
            break;
            case 4:
            r = (int) (t * 255.0f + 0.5f);
            g = (int) (p * 255.0f + 0.5f);
            b = (int) (brightness * 255.0f + 0.5f);
            break;
            case 5:
            r = (int) (brightness * 255.0f + 0.5f);
            g = (int) (p * 255.0f + 0.5f);
            b = (int) (q * 255.0f + 0.5f);
            break;
            }
        }
        return new MColor(r, g, b);
    }    
}
