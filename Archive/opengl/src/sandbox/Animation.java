package sandbox;

import org.eclipse.swt.opengl.GLCanvas;

public interface Animation {

	void initGL(GLCanvas canvas);

    void resize(GLCanvas canvas);

    void repaint(GLCanvas canvas);

}
