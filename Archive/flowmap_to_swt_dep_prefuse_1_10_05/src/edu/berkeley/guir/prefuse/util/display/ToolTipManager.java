package edu.berkeley.guir.prefuse.util.display;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import edu.berkeley.guir.prefuse.Display;

/**
 * Manages custom tool tips for a prefuse 
 * {@link edu.berkeley.guir.prefuse.Display Display} instance.
 *
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class ToolTipManager implements MouseMotionListener {

    // TODO: edit so that tool tip is presented in its own window?
    // TODO: improve thread-safety
    // TODO: review tool tip timer
    
    private Display       m_display;
    private JComponent    m_tooltip;
    private ToolTipTimer  m_toolTipTimer;
    private boolean       m_toolTipsEnabled;
    private long          m_toolTipDelay = 2000L;
    
    public ToolTipManager(Display display, JComponent tooltip) {
        m_display = display;
        m_tooltip = tooltip;
        m_tooltip.setVisible(false);
        
        m_toolTipTimer = new ToolTipTimer();
        m_toolTipsEnabled = true;
        
        new Thread(m_toolTipTimer).start();
    } //
    
    public ToolTipManager(Display display) {
        m_display = display;
        m_tooltip = new DefaultToolTipper();
        m_tooltip.setVisible(false);
        
        m_toolTipTimer = new ToolTipTimer();
        m_toolTipsEnabled = true;
        
        new Thread(m_toolTipTimer).start();
    } //
    
    /**
     * Show a tooltip at the designated location. The tooltip will appear
     * after the delay time has passed if no interruptions occur. Use the
     * setToolTipDelay() method to control delay times.
     * @param x the x-coordinate at which to show the tooltip component
     * @param y the y-coordinate at which to show the tooltip component
     */
    public void showToolTip(int x, int y) {
        m_toolTipTimer.show(x,y);
    } //
    
    /**
     * Hide the tooltip component.
     */
    public void hideToolTip() {
        m_toolTipTimer.hide();
    } //
    
    /**
     * Set the tooltip component. This determines which Swing component is used
     * to present the tooltip display. This API allows arbitrary components to
     * be displayed, allowing a great range of flexibility for tooltips.
     * @param c the component to display for tooltips
     */
    public void setToolTipComponent(JComponent c) {
        m_display.remove(m_tooltip);
        m_tooltip = c;
        m_display.add(m_tooltip, 0);
    } //
    
    /**
     * Returns the tooltip component. This is the component that is displayed
     * when showToolTip() is called.
     * @return the tooltip component
     */
    public JComponent getToolTipComponent() {
        return m_tooltip;
    } //
    
    /**
     * Returns the delay between the time showToolTip() is called and when the
     * tooltip actually appears.
     * @return the tooltip delay
     */
    public long getToolTipDelay() {
        return m_toolTipDelay;
    } //
    
    /**
     * Sets the delay between the time showToolTip() is called and when the
     * tooltip actually appears.
     * @param delay the new tooltip delay
     */
    public void setToolTipDelay(long delay) {
        m_toolTipDelay = delay;
    } //
    
    /**
     * Indicates if tooltip display is enabled or not.
     * @return true if tooltips are enabled, false otherwise
     */
    public boolean isToolTipsEnabled() {
        return m_toolTipsEnabled;
    } //
    
    /**
     * Sets if tooltips are enabled or not.
     * @param s the enabled state. Should be true to enabled tooltips,
     *  false to disable tooltips
     */
    public void setToolTipsEnabled(boolean s) {
        m_toolTipsEnabled = s;
    } //
    
    /**
     * Returns the text string currently displayed in a tooltip. This only
     * applies if the default tooltip component is used. This method will
     * throw an exception if a custom tooltip component has been set using
     * the setToolTipComponent() method.
     * @return the tooltip text
     * @throws IllegalStateException if a custom tooltip component has been set
     */
    public String getToolTipText() {
        if ( m_tooltip instanceof DefaultToolTipper ) {
            return ((DefaultToolTipper)m_tooltip).getText();
        } else {
            throw new IllegalStateException();
        }       
    } //
    
    /**
     * Sets the text string currently displayed in a tooltip. This only
     * applies if the default tooltip component is used. This method will
     * throw an exception if a custom tooltip component has been set using
     * the setToolTipComponent() method.
     * @param text the new tooltip text
     * @throws IllegalStateException if a custom tooltip component has been set
     */ 
    public void setToolTipText(String text) {
        if ( m_tooltip instanceof DefaultToolTipper ) {
            ((DefaultToolTipper)m_tooltip).setText(text);
        } else {
            throw new IllegalStateException();
        }
    } //
    
    /**
     * Performs timing for controlling the display of tooltips.
     */
    public class ToolTipTimer implements Runnable {
        int DEFAULT_X_OFFSET = 15;
        int DEFAULT_Y_OFFSET = 10;
        boolean visible = false;
        boolean show = false, squit = true, hquit = true, hide = false;
        int x, y;
        public synchronized void show(int x, int y) {
            this.squit = false;
            this.show = true;
            this.hide = false;
            this.x = x; this.y = y;
            this.notifyAll();
        } //
        public synchronized void hide() {
            this.squit = true;
            this.hide = true;
            this.show = false;
            this.notifyAll();
        } //
        public void run() {
            synchronized (this) {
                while ( true ) {
                    if ( !visible && show ) {
                        if ( m_toolTipDelay >= 10 ) {
                            try { wait(m_toolTipDelay); } catch ( Exception e) {}
                        }
                        if (!squit) { paint(); visible = true; show = false; }
                    } else if ( visible && show ) {
                        paint(); show = false;
                    } else if ( visible && hide ) {
                        try { wait(250L); } catch ( Exception e ) {}
                        if (hide) { unpaint(); visible = false; hide = false; }
                    } else if ( hide ) {
                        hide = false;
                    }
                    if ( hide == false && show == false ) {
                        try { wait(); } catch ( Exception e ) {}
                        squit = false;
                    }
                }
            }
        } //
        public void paint() {
            m_display.repaintImmediate();
            Dimension  d = m_tooltip.getPreferredSize();
            Graphics2D g = (Graphics2D)m_display.getGraphics();
            
            int offsetX = x+DEFAULT_X_OFFSET;
            int offsetY = y+DEFAULT_Y_OFFSET;
            if (offsetX+d.getWidth() > m_display.getWidth() &&
                    d.getWidth() < m_display.getWidth()) {
                offsetX = x-((int)d.getWidth())-DEFAULT_X_OFFSET;
            }
            if (offsetY+d.getHeight() > m_display.getHeight() &&
                    d.getHeight() < m_display.getHeight()) {
                offsetY = y-((int)d.getHeight())-DEFAULT_Y_OFFSET;      
            }

            // TODO: move to avoid display boundary
            SwingUtilities.paintComponent(g,m_tooltip,m_display,
                    offsetX,offsetY,d.width,d.height);
        } //
        public void unpaint() {
            m_display.repaintImmediate();
        } //
    } // end of inner class ToolTipTimer
    
    /**
     * Default class for displaying tooltips. Presents one line of text
     * in a light yellow box with a black border.
     */
    public class DefaultToolTipper extends JComponent {
        private String text = null;
        public DefaultToolTipper() {
            this.setBackground(new Color(255,255,225));
            this.setForeground(Color.BLACK);
        } //
        public Dimension getPreferredSize() {
            if ( text == null ) return new Dimension(0,0);
            Graphics g = m_display.getGraphics();
            FontMetrics fm = g.getFontMetrics();
            String s = getText();
            int w = 8;
            if (s != null) w += fm.stringWidth(s);
            int h = fm.getHeight();
            return new Dimension(w,h);
        } //
        public void paintComponent(Graphics g) {
            //if ( text == null ) return;
            Rectangle r = this.getBounds();
            g.setColor(getBackground());
            g.fillRect(0,0,r.width-1,r.height-1);
            g.setColor(getForeground());
            g.drawRect(0,0,r.width-1,r.height-1);
            FontMetrics fm = g.getFontMetrics();
            if (text != null) g.drawString(text,4,fm.getAscent());
        } //
        public String getText() {
            return text;
        } //
        public void setText(String text) {
            this.text = text;
        } //
    } // end of inner class DefaultToolTipper


    public void mouseDragged(MouseEvent e) {
        moveEvent(e);
    } //

    public void mouseMoved(MouseEvent e) {
        moveEvent(e);
    } //
    
    private void moveEvent(MouseEvent e) {
        if ( getToolTipText() != null ) {
            this.showToolTip(e.getX(), e.getY());
        } else {
            this.hideToolTip();
        }
    } //
    
} // end of class ToolTipManager
