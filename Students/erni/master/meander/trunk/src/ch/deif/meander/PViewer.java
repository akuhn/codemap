package ch.deif.meander;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import processing.core.PApplet;

@SuppressWarnings("serial")
public class PViewer extends JFrame {

    public PViewer(Map map) {
        super("Map Viewer");

        setLayout(new BorderLayout());
        PApplet embed = createPApplet(map);
        getContentPane().add(embed, BorderLayout.CENTER);
        embed.init();
        pack();
        setVisible(true);
        setSize(map.getWidth(), map.getHeight());
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    public PApplet createPApplet(final Map map) {
        return new PApplet() {

            @Override
            public void draw() {
                map.drawOn(this);
            }

            @Override
            public void setup() {
                //noLoop();
            }    
        };
    }
    
}
