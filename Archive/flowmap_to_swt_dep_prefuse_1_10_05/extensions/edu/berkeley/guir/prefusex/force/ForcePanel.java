package edu.berkeley.guir.prefusex.force;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * A Swing user interface component for configuring the parametes of the
 * Force functions in the given ForceSimulator. Useful for exploring
 * different parameterizations when crafting a visualization.
 *
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class ForcePanel extends JPanel {
    
    private ForceConstantAction action = new ForceConstantAction();
    private ForceSimulator fsim;
    
    public ForcePanel(ForceSimulator fsim) {
        this.fsim = fsim;
        this.setBackground(Color.WHITE);
        initUI();
    } //
    
    private void initUI() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        Force[] forces = fsim.getForces();
        for ( int i=0; i<forces.length; i++ ) {
            Force f = forces[i];
            Box v = new Box(BoxLayout.Y_AXIS);
            for ( int j=0; j<f.getParameterCount(); j++ ) {
                v.add(createField(f,j));
            }
            String name = f.getClass().getName();
            name = name.substring(name.lastIndexOf(".")+1);
            v.setBorder(BorderFactory.createTitledBorder(name));
            this.add(v);
        }
        this.add(Box.createVerticalGlue());
    } //
    
    private Box createField(Force f, int param) {
        Box h = new Box(BoxLayout.X_AXIS);
        
        float curVal = f.getParameter(param);           
        
        JLabel     label = new JLabel(f.getParameterName(param));
        label.setPreferredSize(new Dimension(100,20));
        label.setMaximumSize(new Dimension(100,20));
        
        JTextField text  = new JTextField(
                String.valueOf(curVal));
        text.setPreferredSize(new Dimension(200,20));
        text.setMaximumSize(new Dimension(200,20));
        text.putClientProperty("force", f);
        text.putClientProperty("param", new Integer(param));
        text.addActionListener(action);
        h.add(label);
        h.add(Box.createHorizontalStrut(10));
        h.add(Box.createHorizontalGlue());
        h.add(text);
        h.setPreferredSize(new Dimension(300,30));
        h.setMaximumSize(new Dimension(300,30));
        return h;
    } //
    
    private class ForceConstantAction extends AbstractAction {
        public void actionPerformed(ActionEvent arg0) {
            JTextField text = (JTextField)arg0.getSource();
            float val = Float.parseFloat(text.getText());
            Force f = (Force)text.getClientProperty("force");
            Integer param = (Integer)text.getClientProperty("param");
            f.setParameter(param.intValue(), val);
        }
    } // end of inner class ForceAction
    
} // end of class ForcePanel
