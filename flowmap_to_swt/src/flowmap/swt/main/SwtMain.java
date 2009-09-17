package flowmap.swt.main;

/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

/* 
 * Canvas snippet: paint a circle in a canvas
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */

import java.awt.Dimension;
import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import edu.stanford.hci.flowmap.db.CSVQueryRecord;
import edu.stanford.hci.flowmap.main.Globals;
import edu.stanford.hci.flowmap.main.Options;

public class SwtMain extends Canvas {
    
    private SWTMapDisplayPanel display;

    public SwtMain(Composite parent) {
        super(parent, SWT.NONE);
        
        CSVQueryRecord queryRecord = new CSVQueryRecord(new File("direct.csv"));
        
        Dimension screenDimension = Globals.getScreenDimension();
        setSize(screenDimension.width, screenDimension.height);
//        display = new SWTMapDisplayPanel(this);
        
        this.addPaintListener(display=new SWTMapDisplayPanel(this, queryRecord));
        
//        this.addPaintListener(new PaintListener() {
//            public void paintControl(PaintEvent event) {
//                Rectangle rect = SwtMain.this.getShell().getClientArea();
//                event.gc.drawOval(0, 0, rect.width - 1, rect.height - 1);
//            }
//        });
    }

    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setLayout(new FillLayout());
        new SwtMain(shell);

        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
        display.dispose();
        // quit the bad way as i can't remember how to shut down normally
        // TODO: how ^^ ?
        System.exit(0);
    }
}
