package sandbox;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.opengl.GLCanvas;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

public class RotatingSquare implements Animation {

	private GLCanvas canvas;

	@Override
	public void initGL(GLCanvas c) {
		this.canvas = c;
        canvas.setCurrent();
        try {
            GLContext.useContext(canvas);
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
        
	    GL11.glMatrixMode(GL11.GL_PROJECTION);
	    GL11.glLoadIdentity();
	    Rectangle bounds = canvas.getBounds();
	    GL11.glOrtho(0.0, bounds.width, 0.0, bounds.width, -1.0, 1.0);
	    GL11.glMatrixMode(GL11.GL_MODELVIEW);
	    GL11.glLoadIdentity();
	    GL11.glViewport(0, 0, bounds.width, bounds.height);
	    animationLoop();
		
	}

	public void animationLoop() {
		canvas.getDisplay().timerExec(0, squarePainter);
	}
	
	private Runnable squarePainter = new Runnable() {

		private float angle;

		@Override
		public void run() {
			if (canvas.isDisposed())
				return;

			canvas.setCurrent();
			try {
				GLContext.useContext(canvas);
			} catch (LWJGLException e) {
				e.printStackTrace();
			}

			// clear the screen
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_STENCIL_BUFFER_BIT);

			// center square according to screen size
			GL11.glPushMatrix();

			// rotate square according to angle
			GL11.glRotatef(angle, 0, 0, 1.0f);

			// render the square
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2i(-50, -50);
			GL11.glVertex2i(50, -50);
			GL11.glVertex2i(50, 50);
			GL11.glVertex2i(-50, 50);
			GL11.glEnd();

			GL11.glPopMatrix();
			// Rotate the square
			angle += 2.0f % 360;
			canvas.swapBuffers();
			canvas.getDisplay().timerExec(50, this);
		}
	};

    @Override
    public void repaint(GLCanvas canvas) {
        // ignore as we loop all the time
    }

    @Override
    public void resize(GLCanvas canvas) {
        // ignore as we loop all the time
    }	

}
