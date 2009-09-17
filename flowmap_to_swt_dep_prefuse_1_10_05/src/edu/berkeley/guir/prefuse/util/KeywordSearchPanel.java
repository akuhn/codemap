package edu.berkeley.guir.prefuse.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import edu.berkeley.guir.prefuse.FocusManager;
import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.NodeItem;
import edu.berkeley.guir.prefuse.VisualItem;
import edu.berkeley.guir.prefuse.event.ItemRegistryListener;
import edu.berkeley.guir.prefuse.focus.FocusSet;
import edu.berkeley.guir.prefuse.focus.KeywordSearchFocusSet;
import edu.berkeley.guir.prefuse.graph.Entity;

/**
 * Provides keyword search over the currently visualized data.
 *
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> vizster(AT)jheer.org
 */
public class KeywordSearchPanel extends JPanel
    implements DocumentListener, ActionListener
{
    private KeywordSearchFocusSet searcher;
    private FocusSet focus;

    private JTextField queryF   = new JTextField(15);
    private JLabel     resultL = new JLabel();
    private JLabel     matchL  = new JLabel();
    private JLabel     searchL = new JLabel("search >> ");
    private IconButton upArrow = new IconButton(new ArrowIcon(ArrowIcon.UP),
            new ArrowIcon(ArrowIcon.UP_DEPRESSED));
    private IconButton downArrow = new IconButton(new ArrowIcon(ArrowIcon.DOWN),
            new ArrowIcon(ArrowIcon.DOWN_DEPRESSED));

    private String[] searchAttr;
    private Entity m_results[];
    private int    m_curResult;

    public KeywordSearchPanel(String[] attr, ItemRegistry registry) {
        searchAttr = attr;
        FocusManager fmanager = registry.getFocusManager();
        focus = fmanager.getDefaultFocusSet();
        FocusSet search = registry.getFocusManager()
        					.getFocusSet(FocusManager.SEARCH_KEY);
        if ( search != null ) {
            if ( search instanceof KeywordSearchFocusSet ) {
                searcher = (KeywordSearchFocusSet)search;
            } else {
                throw new IllegalStateException(
                    "Search focus set not instance of KeywordSearchFocusSet!");
            }
        } else {
            searcher = new KeywordSearchFocusSet();
            fmanager.putFocusSet(FocusManager.SEARCH_KEY, searcher);
        }
        init(registry);
    } //
    
    public KeywordSearchPanel(String[] attr, ItemRegistry registry, 
            KeywordSearchFocusSet searchSet, FocusSet focusSet) {
        searchAttr = attr;
        searcher = searchSet;
        focus = focusSet;
        init(registry);
    } //

    private void init(ItemRegistry registry) {
        // add a listener to dynamically build search index
        registry.addItemRegistryListener(new ItemRegistryListener() {
            public void registryItemAdded(VisualItem item) {
                if ( !(item instanceof NodeItem) ) return;
                for ( int i=0; i < searchAttr.length; i++ )
                    searcher.index(item.getEntity(), searchAttr[i]);
                searchUpdate();
            } //
            public void registryItemRemoved(VisualItem item) {
                if ( !(item instanceof NodeItem) ) return;
                for ( int i=0; i < searchAttr.length; i++ )
                    searcher.remove(item.getEntity(), searchAttr[i]);
                searchUpdate();
            } //
        });
        
        queryF.getDocument().addDocumentListener(this);
        queryF.setMaximumSize(new Dimension(100, 20));

        upArrow.addActionListener(this);
        upArrow.setEnabled(false);
        downArrow.addActionListener(this);
        downArrow.setEnabled(false);
        
        matchL.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if ( matchL.getText().length() > 0 ) {
                    matchL.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }
            } //
            public void mouseExited(MouseEvent e) {
                if ( matchL.getText().length() > 0 ) {
                    matchL.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            } //
            public void mouseClicked(MouseEvent e) {
                if ( matchL.getText().length() > 0 ) {
                    focus.set(m_results[m_curResult]);
                }
            } //
        });
        
        setBackground(Color.WHITE);
        initUI();
    } //
    
    private void initUI() {
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        
        Box b = new Box(BoxLayout.X_AXIS);
        b.add(resultL);
        b.add(Box.createHorizontalStrut(5));
        b.add(Box.createHorizontalGlue());
        b.add(matchL);
        b.add(Box.createHorizontalStrut(5));
        b.add(downArrow);
        b.add(upArrow);
        b.add(Box.createHorizontalStrut(5));
        b.add(searchL);
        b.add(queryF);
        
        this.add(b);
    } //

    private void searchUpdate() {
        String query = queryF.getText();
        if ( query.length() == 0 ) {
            searcher.clear();
            resultL.setText("");
            matchL.setText("");
            downArrow.setEnabled(false);
            upArrow.setEnabled(false);
            m_results = null;
        } else {
            searcher.search(query);
            int r = searcher.size();
            resultL.setText(r + " match" + (r==1?"":"es"));
            m_results = new Entity[r];
            Iterator iter = searcher.iterator();
            for ( int i=0; iter.hasNext(); i++ ) {
                m_results[i] = (Entity)iter.next();
            }
            if ( r > 0 ) {
                String label = "name";
                matchL.setText("1/"+r+": " +
                        m_results[0].getAttribute(label));
                downArrow.setEnabled(true);
                upArrow.setEnabled(true);
            } else {
                matchL.setText("");
                downArrow.setEnabled(false);
                upArrow.setEnabled(false);
            }
            m_curResult = 0;
        }
        validate();
    } //
    
    public void setBackground(Color bg) {
        super.setBackground(bg);
        if ( queryF  != null ) queryF.setBackground(bg);
        if ( resultL != null ) resultL.setBackground(bg);
        if ( matchL  != null ) matchL.setBackground(bg);
        if ( searchL != null ) searchL.setBackground(bg);
        if ( upArrow != null ) upArrow.setBackground(bg);
        if ( downArrow != null ) downArrow.setBackground(bg);
    } //
    
    public void setForeground(Color fg) {
        super.setForeground(fg);
        if ( queryF  != null ) {
            queryF.setForeground(fg);
            queryF.setCaretColor(fg);
        }
        if ( resultL != null ) resultL.setForeground(fg);
        if ( matchL  != null ) matchL.setForeground(fg);
        if ( searchL != null ) searchL.setForeground(fg);
        if ( upArrow != null ) upArrow.setForeground(fg);
        if ( downArrow != null ) downArrow.setForeground(fg);
    } //

    /**
     * @see javax.swing.event.DocumentListener#changedUpdate(javax.swing.event.DocumentEvent)
     */
    public void changedUpdate(DocumentEvent e) {
        searchUpdate();
    } //

    /**
     * @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.DocumentEvent)
     */
    public void insertUpdate(DocumentEvent e) {
        searchUpdate();
    } //

    /**
     * @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.DocumentEvent)
     */
    public void removeUpdate(DocumentEvent e) {
        searchUpdate();
    } //

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        if ( matchL.getText().length() == 0 ) return;
        if ( e.getSource() == downArrow ) {
            m_curResult = (m_curResult + 1) % m_results.length;
        } else if ( e.getSource() == upArrow ) {
            m_curResult = (m_curResult - 1) % m_results.length;
            if ( m_curResult < 0 )
                m_curResult += m_results.length;
        }
        String label = "name";
        matchL.setText((m_curResult+1)+"/"+m_results.length+": " +
                m_results[m_curResult].getAttribute(label));
        validate();
        repaint();
    }//

    public class IconButton extends JButton {
        public IconButton(Icon icon1, Icon icon2) {
            super(icon1);
            if ( icon1.getIconWidth() != icon2.getIconWidth() ||
                    icon2.getIconHeight() != icon2.getIconHeight() ) {
                throw new IllegalArgumentException("Icons must have "
                        + "matching dimensions");
            }
            setPressedIcon(icon2);
            setDisabledIcon(new ArrowIcon(ArrowIcon.DISABLED));
            setBorderPainted(false);
            setFocusPainted(false);
            setBackground(getBackground());
            Insets in = getMargin();
            in.left = 0; in.right = 0;
            setMargin(in);
        } //
    } // end of class IconButton

    public class ArrowIcon implements Icon {
        public static final int UP = 0, UP_DEPRESSED = 1;
        public static final int DOWN = 2, DOWN_DEPRESSED = 3;
        public static final int DISABLED = 4;
        private int type;
        public ArrowIcon(int type) {
            this.type = type;
        } //
        public int getIconHeight() {
            return 11;
        } //
        public int getIconWidth() {
            return 11;
        } //
        public void paintIcon(Component c, Graphics g, int x, int y) {
            if ( type >= DISABLED ) return;
            Polygon p = new Polygon();
            int w = getIconWidth();
            int h = getIconHeight();
            if ( type < DOWN ) {
                p.addPoint(x,y+h-1);
                p.addPoint(x+w-1,y+h-1);
                p.addPoint(x+(w-1)/2,y);
                p.addPoint(x,y+h);
            } else {
                p.addPoint(x,y);
                p.addPoint(x+w-1,y);
                p.addPoint(x+(w-1)/2,y+h-1);
                p.addPoint(x,y);
            }
            g.setColor((type%2!=0 ? Color.LIGHT_GRAY : getForeground()));
            g.fillPolygon(p);
            g.setColor(Color.BLACK);
            g.drawPolygon(p);
        } //
    } // end of inner class ArrowIcon
        
    
} // end of class KeywordSearchPanel
