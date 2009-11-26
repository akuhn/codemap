package sandbox;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.opengl.GLCanvas;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.GLU;

public class StaticTorus implements Animation {

	private GLCanvas canvas;

	@Override
	public void initGL(GLCanvas c) {
		canvas = c;
	}

	private Runnable staticgridpainter = new Runnable() {
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
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glClearColor(.3f, .5f, .8f, 1.0f);
			GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);

			GL11.glClearDepth(1.0);
			GL11.glLineWidth(2);
			GL11.glEnable(GL11.GL_DEPTH_TEST);

			GL11.glLoadIdentity();
			GL11.glTranslatef(0.0f, 0.0f, -10.0f);

			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
			GL11.glColor3f(0.9f, 0.9f, 0.9f);
			draw(1, 1.9f + ((float) Math.sin((0.004f * 1))), 15, 15);
			canvas.swapBuffers();
			canvas.getDisplay().timerExec(50, this);
		}
		
		private void draw(float r, float R, int nsides, int rings) {
			float ringDelta = 2.0f * (float) Math.PI / rings;
			float sideDelta = 2.0f * (float) Math.PI / nsides;
			float theta = 0.0f, cosTheta = 1.0f, sinTheta = 0.0f;
			for (int i = rings - 1; i >= 0; i--) {
				float theta1 = theta + ringDelta;
				float cosTheta1 = (float) Math.cos(theta1);
				float sinTheta1 = (float) Math.sin(theta1);
				GL11.glBegin(GL11.GL_QUAD_STRIP);
				float phi = 0.0f;
				for (int j = nsides; j >= 0; j--) {
					phi += sideDelta;
					float cosPhi = (float) Math.cos(phi);
					float sinPhi = (float) Math.sin(phi);
					float dist = R + r * cosPhi;
					GL11.glNormal3f(cosTheta1 * cosPhi, -sinTheta1 * cosPhi, sinPhi);
					GL11.glVertex3f(cosTheta1 * dist, -sinTheta1 * dist, r * sinPhi);
					GL11.glNormal3f(cosTheta * cosPhi, -sinTheta * cosPhi, sinPhi);
					GL11.glVertex3f(cosTheta * dist, -sinTheta * dist, r * sinPhi);
				}
				GL11.glEnd();
				theta = theta1;
				cosTheta = cosTheta1;
				sinTheta = sinTheta1;
			}
		}		
	};

    @Override
    public void repaint(GLCanvas canvas) {
        canvas.getDisplay().asyncExec(staticgridpainter);
    }

    @Override
    public void resize(GLCanvas canvas) {
        Rectangle bounds = canvas.getBounds();
        float fAspect = (float) bounds.width / (float) bounds.height;
        canvas.setCurrent();
        try {
            GLContext.useContext(canvas);
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
        GL11.glViewport(0, 0, bounds.width, bounds.height);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GLU.gluPerspective(45.0f, fAspect, 0.5f, 400.0f);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();        
        canvas.getDisplay().asyncExec(staticgridpainter);
    }	
	
}
