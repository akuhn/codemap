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

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import edu.stanford.hci.flowmap.db.QueryRecord;

public class SwtMain extends Canvas {
    
    public SwtMain(Composite parent) {
        super(parent, SWT.NONE);
        
        QueryRecord queryRecord;
//        queryRecord = new CSVQueryRecord(new File("direct.csv"));
        queryRecord = new FakeQueryRecord();
        
        Dimension screenDimension = Globals.getScreenDimension();
        setSize(screenDimension.width, screenDimension.height);
        this.addPaintListener(new SWTMapDisplayPanel(queryRecord));
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
