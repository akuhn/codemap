package org.codemap.mapview;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.ACC;
import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleControlAdapter;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.WorkbenchMessages;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class TextFilter extends Composite {
    
    /**
     * Image descriptor for enabled clear button.
     */
    private static final String CLEAR_ICON = "org.eclipse.ui.internal.dialogs.CLEAR_ICON"; //$NON-NLS-1$

    /**
     * Image descriptor for disabled clear button.
     */
    private static final String DISABLED_CLEAR_ICON= "org.eclipse.ui.internal.dialogs.DCLEAR_ICON"; //$NON-NLS-1$
    
    /**
     * Get image descriptors for above buttons.
     */    
    static {
        ImageDescriptor descriptor = AbstractUIPlugin.imageDescriptorFromPlugin(PlatformUI.PLUGIN_ID, "$nl$/icons/full/etool16/clear_co.gif"); //$NON-NLS-1$
        if (descriptor != null) {
            JFaceResources.getImageRegistry().put(CLEAR_ICON, descriptor);
        }
        descriptor = AbstractUIPlugin.imageDescriptorFromPlugin(PlatformUI.PLUGIN_ID, "$nl$/icons/full/dtool16/clear_co.gif"); //$NON-NLS-1$
        if (descriptor != null) {
            JFaceResources.getImageRegistry().put(DISABLED_CLEAR_ICON, descriptor);
        }
    }    
    
    private static Boolean useNativeSearchField;
    
    private static boolean useNativeSearchField(Composite composite) {
        if (useNativeSearchField == null) {
            useNativeSearchField = Boolean.FALSE;
            Text testText = null;
            try {
                testText = new Text(composite, SWT.SEARCH | SWT.ICON_CANCEL);
                useNativeSearchField = new Boolean((testText.getStyle() & SWT.ICON_CANCEL) != 0);
            } finally {
                if (testText != null) {
                    testText.dispose();
                }
            }
                
        }
        return useNativeSearchField.booleanValue();
    }    
    
    private Label clearButtonControl;
    protected String initialText = "";
    
    private Text filterText;    

    public TextFilter(Composite composite) {
        super(composite, SWT.BORDER);
        setBackground(composite.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
        setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        GridLayout filterLayout= new GridLayout(2, false);
        filterLayout.marginHeight= 0;
        filterLayout.marginWidth= 0;
        setLayout(filterLayout);
        setFont(composite.getFont());

//        createFilterControls(filterComposite);
        createClearTextNew(composite);
        setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));          
    }
    
    private void createClearTextNew(Composite parent) {
        // only create the button if the text widget doesn't support one
        // natively
        final Image inactiveImage = JFaceResources.getImageRegistry().getDescriptor(DISABLED_CLEAR_ICON).createImage();
        final Image activeImage = JFaceResources.getImageRegistry().getDescriptor(CLEAR_ICON).createImage();
        final Image pressedImage = new Image(parent.getDisplay(), activeImage, SWT.IMAGE_GRAY);
        
        final Label clearButton = new Label(parent, SWT.NONE);
        clearButton.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
        clearButton.setImage(inactiveImage);
        clearButton.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
        clearButton.setToolTipText("please enter some text");
        clearButton.addMouseListener(new MouseAdapter() {
            private MouseMoveListener fMoveListener;

            public void mouseDown(MouseEvent e) {
                clearButton.setImage(pressedImage);
                fMoveListener= new MouseMoveListener() {
                    private boolean fMouseInButton= true;

                    public void mouseMove(MouseEvent e) {
                        boolean mouseInButton= isMouseInButton(e);
                        if (mouseInButton != fMouseInButton) {
                            fMouseInButton= mouseInButton;
                            clearButton.setImage(mouseInButton ? pressedImage : inactiveImage);
                        }
                    }
                };
                clearButton.addMouseMoveListener(fMoveListener);
            }

            public void mouseUp(MouseEvent e) {
                if (fMoveListener != null) {
                    clearButton.removeMouseMoveListener(fMoveListener);
                    fMoveListener= null;
                    boolean mouseInButton= isMouseInButton(e);
                    clearButton.setImage(mouseInButton ? activeImage : inactiveImage);
                    if (mouseInButton) {
//                        clearText();
//                        filterText.setFocus();
                    }
                }
            }
            
            private boolean isMouseInButton(MouseEvent e) {
                Point buttonSize = clearButton.getSize();
                return 0 <= e.x && e.x < buttonSize.x && 0 <= e.y && e.y < buttonSize.y;
            }
        });
        clearButton.addMouseTrackListener(new MouseTrackListener() {
            public void mouseEnter(MouseEvent e) {
                clearButton.setImage(activeImage);
            }

            public void mouseExit(MouseEvent e) {
                clearButton.setImage(inactiveImage);
            }

            public void mouseHover(MouseEvent e) {
            }
        });
        clearButton.addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent e) {
                inactiveImage.dispose();
                activeImage.dispose();
                pressedImage.dispose();
            }
        });
        clearButton.getAccessible().addAccessibleListener(
            new AccessibleAdapter() {
                public void getName(AccessibleEvent e) {
                    e.result= WorkbenchMessages.FilteredTree_AccessibleListenerClearButton;
                }
        });
        clearButton.getAccessible().addAccessibleControlListener(
            new AccessibleControlAdapter() {
                public void getRole(AccessibleControlEvent e) {
                    e.detail= ACC.ROLE_PUSHBUTTON;
                }
        });
        clearButtonControl= clearButton;
    }
    
    /**
     * Creates the text control for entering the filter text. Subclasses may
     * override.
     * 
     * @param parent
     *            the parent composite
     * @return the text widget
     * 
     * @since 3.3
     */
    protected Text doCreateFilterText(Composite parent) {
        if (useNativeSearchField(parent)) {
            return new Text(parent, SWT.SINGLE | SWT.BORDER | SWT.SEARCH | SWT.ICON_CANCEL);
        }
        return new Text(parent, SWT.SINGLE);
    }    
    
    /**
     * Creates the filter text and adds listeners. This method calls
     * {@link #doCreateFilterText(Composite)} to create the text control.
     * Subclasses should override {@link #doCreateFilterText(Composite)} instead
     * of overriding this method.
     * 
     * @param parent
     *            <code>Composite</code> of the filter text
     */
    protected void createFilterText(Composite parent) {
        filterText = doCreateFilterText(parent);
        GridData gridData= new GridData(SWT.FILL, SWT.CENTER, true, false);
        // if the text widget supported cancel then it will have it's own
        // integrated button. We can take all of the space.
        if ((filterText.getStyle() & SWT.ICON_CANCEL) != 0)
            gridData.horizontalSpan = 2;
        filterText.setLayoutData(gridData);
    }    

}
