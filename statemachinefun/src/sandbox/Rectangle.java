package sandbox;


//import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.*;

import glapp.GLApp;
import glapp.GLImage;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import org.eclipse.swt.opengl.GLCanvas;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.GLU;

public class Rectangle implements Animation {

    private GLCanvas canvas;
    private int textureHandle;

    @Override
    public void initGL(GLCanvas c) {
        canvas = c;
        canvas.setCurrent();
        try {
            GLContext.useContext(canvas);
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
        glClearColor(0, 0, 0, 0);
        glShadeModel(GL_FLAT);
        glEnable(GL_DEPTH_TEST);
        textureHandle = GLApp.makeTexture("images/eye.jpg");        
//        makeCheckImage();
    }
    
    private void makeCheckImage() {
        int size = 256;
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        for(int i = 0; i < size; i++) {
            for(int j=0; j < size; j++) {
                boolean black = ((i&0x8)==0)^((j&0x8)==0);
                int rgb = black ? 0: 255;
                img.setRGB(i, j, rgb);
            }
        }
        GLImage glImage = new GLImage();
        assert glImage.makeGLImage(img, false, false);
        textureHandle = GLApp.makeTexture(glImage);
    }

    private Runnable painter = new Runnable() {
        @Override
        public void run() {
            glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

            // select model view for subsequent transforms
            glMatrixMode(GL11.GL_MODELVIEW);
            glLoadIdentity();
            
            glEnable(GL_TEXTURE_2D);
            glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_REPLACE);
            glBindTexture(GL_TEXTURE_2D, textureHandle);
            glBegin(GL_QUADS);
                GL11.glTexCoord2f(0f, 0f);
                glVertex2d(0.25, 0.25);
                GL11.glTexCoord2f(1f, 0f);
                glVertex2d(0.75, 0.25);
                GL11.glTexCoord2f(1f, 1f);
                glVertex2d(0.75, 0.75);
                GL11.glTexCoord2f(0f, 1f);
                glVertex2d(0.25, 0.75);        
            glEnd();
            glFlush();
            canvas.swapBuffers();
            glDisable(GL_TEXTURE_2D);
        }
    };

    @Override
    public void repaint(GLCanvas canvas) {
        canvas.getDisplay().asyncExec(painter);
    }

    @Override
    public void resize(GLCanvas canvas) {
        org.eclipse.swt.graphics.Rectangle bounds = canvas.getBounds();
        glViewport(0, 0, bounds.width, bounds.height);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, 1, 0, 1, -1, 1);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
    }

}
