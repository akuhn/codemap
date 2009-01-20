//  Copyright (c) 2008 Adrian Kuhn <akuhn(a)iam.unibe.ch>
//  
//  This file is part of Pimon.
//  
//  Pimon is free software: you can redistribute it and/or modify it under the
//  terms of the GNU Affero General Public License as published by the Free
//  Software Foundation, either version 3 of the License, or (at your option)
//  any later version.
//  
//  Pimon is distributed in the hope that it will be useful, but WITHOUT ANY
//  WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
//  FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
//  details.
//  
//  You should have received a copy of the GNU Affero General Public License
//  along with Pimon. If not, see <http://www.gnu.org/licenses/>.
//  
package ch.deif.meander;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Scrollable;
import javax.swing.WindowConstants;

@SuppressWarnings("serial")
public class ImageViewer extends JPanel implements Scrollable {

    private Image visual;

    public ImageViewer() {
        setVisualization(null);
        initComponents();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (visual == null) return;
        g.drawImage(visual, 0, 0, null);
    }

    public void setVisualization(Image visual) {
        this.visual = visual;
        int width = visual == null ? 800 : getVisualizationWidth();
        int height = visual == null ? 600 : getVisualizationHeight();
        Dimension size = new Dimension(width, height);
        setPreferredSize(size);
        revalidate();
        repaint();
    }

    private int getVisualizationWidth() {
        return visual.getWidth(null);
    }

    private void initComponents() {
        jScrollBar1 = new javax.swing.JScrollBar();
        this.addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent e) {

            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }

        });
    }

    public Dimension getPreferredScrollableViewportSize() {
        int width = visual == null ? 800 : getVisualizationWidth();
        int height = visual == null ? 128 : getVisualizationHeight();
        return new Dimension(Math.min(800, width), height);
    }

    private int getVisualizationHeight() {
        return visual.getHeight(null);
    }

    public int getScrollableBlockIncrement(Rectangle visibleRect, int orgetTimeSeriesWidthn, int direction) {

        return getVisualizationWidth() / 2;

    }

    public boolean getScrollableTracksViewportHeight() {
        return false;
    }

    public boolean getScrollableTracksViewportWidth() {
        return false;
    }

    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 10;
    }

    @SuppressWarnings("unused")
    private javax.swing.JScrollBar jScrollBar1;

    public void showFrame() {
        showFrame(null);
    }

    public void showFrame(String title) {
        JFrame frame = new JFrame();
        frame.setTitle((title == null ? "" : title + " - ") + visual);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        JScrollPane pane = new JScrollPane();
        pane.setViewportView(this);
        frame.getContentPane().add(pane, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }

    public static ImageViewer show(Image visual) {
        ImageViewer viewer = new ImageViewer();
        viewer.setVisualization(visual);
        viewer.showFrame();
        return viewer;
    }

}
