package sandbox;

import static org.lwjgl.opengl.GL11.*;

import org.eclipse.swt.opengl.GLCanvas;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

public class Rectangle implements Animation {

    private GLCanvas canvas;

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
    }
    
    private void paint() {
        canvas.getDisplay().asyncExec(painter);
    }
    
    private Runnable painter = new Runnable() {
        @Override
        public void run() {
            glClear(GL_COLOR_BUFFER_BIT);
            glColor3f(1, 1, 1);
            glBegin(GL_POLYGON);
            glVertex2d(0.25, 0.25);
            glVertex2d(0.75, 0.25);
            glVertex2d(0.75, 0.75);
            glVertex2d(0.25, 0.75);
            glEnd();
            glFlush();
            canvas.swapBuffers();
        }
    };

    @Override
    public void repaint(GLCanvas canvas) {
        paint();
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
//        paint();
    }

}
