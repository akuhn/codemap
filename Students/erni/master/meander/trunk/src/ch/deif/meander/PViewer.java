package ch.deif.meander;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

@SuppressWarnings("serial")
public class PViewer extends JFrame {

    public PViewer(MapVisualization viz) {
        super("Map Viewer");

        setLayout(new BorderLayout());
        getContentPane().add(viz, BorderLayout.CENTER);
        viz.init();
        pack();
        setVisible(true);
        setSize(viz.map.getParameters().width, viz.map.getParameters().height);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }


    
}
