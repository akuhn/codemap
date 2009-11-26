package sandbox;

/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

/*
 * SWT OpenGL snippet: use LWJGL to draw to an SWT GLCanvas
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 * 
 * @since 3.2
 */
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.GLContext;

public class GLMain {

	public static void main(String[] args) {
		final Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
        shell.setText("SWT/LWJGL Example");
        shell.setSize(640, 480);		
		Composite comp = new Composite(shell, SWT.NONE);
		comp.setLayout(new FillLayout());

		GLData data = new GLData();
		data.doubleBuffer = true;
		final GLCanvas canvas = new GLCanvas(comp, SWT.NONE, data);

        final Animation animation = new Rectangle();
//		final Animation animation = new StaticTorus();
//		final Animation animation = new RotatingSquare();
		animation.initGL(canvas);
		
		canvas.addListener(SWT.Resize, new Listener() {
		    @Override
			public void handleEvent(Event event) {
				animation.resize(canvas);
			}
		});
		canvas.addPaintListener(new PaintListener() {
            @Override
            public void paintControl(PaintEvent e) {
                animation.repaint(canvas);
            }
        });

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
		System.exit(0);
	}
}
