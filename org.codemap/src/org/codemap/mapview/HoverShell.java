package org.codemap.mapview;

import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class HoverShell {
    
    private Shell shell;
    private Text textLabel;
    private final Canvas canvas;
    private boolean showHover;
    
    public HoverShell(Canvas parent) {
        canvas = parent;
        makeHoverShell();
        addMouseListeners();
    }

    private void addMouseListeners() {
        canvas.addMouseTrackListener(new MouseTrackListener() {
            
            @Override
            public void mouseHover(MouseEvent e) {
                maybeShowHover();
            }
            
            @Override
            public void mouseExit(MouseEvent e) {
                hideHover();
            }
            
            @Override
            public void mouseEnter(MouseEvent e) {
                updateShellLocation(canvas.toDisplay(e.x, e.y));
            }
        });
        
        canvas.addMouseMoveListener(new MouseMoveListener() {
            
            @Override
            public void mouseMove(MouseEvent e) {
                hideHover();
                updateShellLocation(canvas.toDisplay(e.x, e.y));
            }
        });
    }

    protected void hideHover() {
        shell.setVisible(false);
    }

    protected void maybeShowHover() {
        shell.setVisible(showHover);
    }

    protected void updateShellLocation(Point point) {
        updateShellLocation(point.x, point.y);
    }

    protected void updateShellLocation(int x, int y) {
        shell.setLocation(x, y+15);
    }

    private void makeHoverShell() {
        Device device = canvas.getDisplay();
        Color backgroundColor = device.getSystemColor(SWT.COLOR_INFO_BACKGROUND);
        Color foregroundColor = device.getSystemColor(SWT.COLOR_INFO_FOREGROUND);
        
        shell = new Shell(canvas.getShell(), SWT.NO_FOCUS | SWT.ON_TOP | SWT.TOOL);
        shell.setBackground(backgroundColor);
        int border = ((canvas.getShell().getStyle() & SWT.NO_TRIM) == 0) ? 0 : 1;
        GridLayoutFactory.fillDefaults().margins(border, border).applyTo(shell);
        shell.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        shell.setVisible(false);
        
        Composite shellComposite = new Composite(shell, SWT.NONE);
        shellComposite.setBackground(backgroundColor);
        GridLayoutFactory.fillDefaults().margins(0, 2).applyTo(shellComposite);
        shellComposite.setLayoutData(new GridData(GridData.FILL_BOTH | GridData.VERTICAL_ALIGN_BEGINNING));
        textLabel = new Text(shellComposite, SWT.WRAP | SWT.MULTI | SWT.READ_ONLY);
        textLabel.setLayoutData(new GridData(GridData.FILL_BOTH));
        textLabel.setBackground(backgroundColor);
        textLabel.setForeground(foregroundColor);
        textLabel.setEditable(false);
        
        shell.addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent e) {
                onTextShellDispose(e);
            }
        });
    }

    protected void onTextShellDispose(DisposeEvent e) {
        // TODO: not sure what to do here, probably nothing at all
    }

    public void setText(String name) {
        if (name == null || name.equals("")) {
            showHover = false;
            return;
        }
        showHover = true;
        textLabel.setText(name);
        shell.pack(true);
    }    

}
