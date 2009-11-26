package edu.berkeley.guir.prefuse.demos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import edu.berkeley.guir.prefuse.Display;
import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.action.ActionMap;
import edu.berkeley.guir.prefuse.action.ActionSwitch;
import edu.berkeley.guir.prefuse.action.RepaintAction;
import edu.berkeley.guir.prefuse.action.filter.GraphFilter;
import edu.berkeley.guir.prefuse.activity.ActionList;
import edu.berkeley.guir.prefuse.activity.ActivityMap;
import edu.berkeley.guir.prefuse.graph.Graph;
import edu.berkeley.guir.prefuse.graph.GraphLib;
import edu.berkeley.guir.prefusex.controls.AnchorUpdateControl;
import edu.berkeley.guir.prefusex.controls.DragControl;
import edu.berkeley.guir.prefusex.distortion.BifocalDistortion;
import edu.berkeley.guir.prefusex.distortion.Distortion;
import edu.berkeley.guir.prefusex.distortion.FisheyeDistortion;
import edu.berkeley.guir.prefusex.layout.GridLayout;

/**
 * Demonstration illustrating the use of distortion transformations on
 *  a visualization.
 *
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class DistortionDemo extends JFrame {

    private ItemRegistry registry;
    private ActivityMap  activityMap = new ActivityMap();
    private ActionMap    actionMap   = new ActionMap();
    
    public static void main(String argv[]) {
        new DistortionDemo();
    } //
    
    public DistortionDemo() {
        super("Distortion Demo");
        
        Graph g = GraphLib.getGrid(20,20);
        registry = new ItemRegistry(g);

        Display display = new Display();
        display.setItemRegistry(registry);
        display.setSize(600,600);
        display.setBorder(BorderFactory.createEmptyBorder(50,50,50,50));
        display.addControlListener(new DragControl(false));
        
        ActionList filter = new ActionList(registry);
        filter.add(new GraphFilter());
        filter.add(new GridLayout());
        filter.add(new RepaintAction());
        
        ActionList distort = new ActionList(registry);
        Distortion[] acts = new Distortion[] {
            (Distortion)actionMap.put("distort1",new BifocalDistortion()),
            (Distortion)actionMap.put("distort2",new FisheyeDistortion())
        };
        distort.add(actionMap.put("switch",new ActionSwitch(acts, 0)));
        distort.add(new RepaintAction());
        activityMap.put("distortion",distort);
        
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().add(display, BorderLayout.CENTER);
        getContentPane().add(new SwitchPanel(), BorderLayout.SOUTH);
        pack();
        setVisible(true);
        
        // run filter and layout
        filter.runNow();
        
        // enable distortion mouse-over
        AnchorUpdateControl auc = new AnchorUpdateControl(acts,distort);
        display.addMouseListener(auc);
        display.addMouseMotionListener(auc);
    } //
    
    class SwitchPanel extends JPanel implements ActionListener {
        public static final String BIFOCAL = "Bifocal";
        public static final String FISHEYE = "Fisheye";
        public static final String SIZES   = "Transform Sizes";
        public SwitchPanel() {
            setBackground(Color.WHITE);
            initUI();
        } //
        private void initUI() {
            JRadioButton bb = new JRadioButton(BIFOCAL);
            JRadioButton fb = new JRadioButton(FISHEYE);
            bb.setActionCommand(BIFOCAL);
            fb.setActionCommand(FISHEYE);
            bb.setSelected(true);
            
            JCheckBox cb = new JCheckBox(SIZES);
            cb.setActionCommand(SIZES);
            cb.setSelected(true);
            
            bb.setBackground(Color.WHITE);
            fb.setBackground(Color.WHITE);
            cb.setBackground(Color.WHITE);
            
            Font f = new Font("SanSerif",Font.PLAIN,24);
            bb.setFont(f);
            fb.setFont(f);
            cb.setFont(f);
            
            bb.addActionListener(this);
            fb.addActionListener(this);
            cb.addActionListener(this);
            
            ButtonGroup bg = new ButtonGroup();
            bg.add(bb); this.add(bb);
            this.add(Box.createHorizontalStrut(20));
            bg.add(fb); this.add(fb);
            this.add(Box.createHorizontalStrut(20));
            this.add(cb);
        } //
        public void actionPerformed(ActionEvent e) {
            String cmd = e.getActionCommand();
            if ( BIFOCAL == cmd ) {
                ((ActionSwitch)actionMap.get("switch")).setSwitchValue(0);
                activityMap.scheduleNow("distortion");
            } else if ( FISHEYE == cmd ) {
                ((ActionSwitch)actionMap.get("switch")).setSwitchValue(1);
                activityMap.scheduleNow("distortion");
            } else if ( SIZES == cmd ) {
                boolean s = ((JCheckBox)e.getSource()).isSelected();
                ((Distortion)actionMap.get("distort1")).setSizeDistorted(s);
                ((Distortion)actionMap.get("distort2")).setSizeDistorted(s);
                activityMap.scheduleNow("distortion");
            }
        } //
    } // end of inner class SwitchPanel
    
} // end of class DistortionDemo
