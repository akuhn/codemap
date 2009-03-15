package ch.deif.meander.ui;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;

import processing.core.PApplet;
import ch.deif.meander.Map;
import ch.deif.meander.MapVisualization;
import ch.deif.meander.MaxDistNearestNeighbor;
import ch.deif.meander.NearestNeighbor;

public class Applet {

    @SuppressWarnings("serial")
    public static class MapViz extends PApplet {

        private MapVisualization viz;
        private int width;
        private int height;
        private Map map;
        private Collection<Point> points;
        private boolean preSelect = false;

        public MapViz(MapVisualization viz) {
            this.viz = viz;
            map = viz.map;
            points = new ArrayList<Point>();
        }

        @Override
        public void setup() {
            width = viz.map.getParameters().width;
            height = viz.map.getParameters().height;
            frameRate(10);
            smooth();
            size(width, height);
        }

        @Override
        public void draw() {
            viz.draw(g);
            noFill();
            strokeWeight(2);

            if (preSelect) {
                stroke(Color.BLUE.getRGB());
                Point current = new Point(mouseX, mouseY);
                Point preSelect = new MaxDistNearestNeighbor(map, width / 10)
                        .forLocation(current);
                if (preSelect != null) {
                    ellipse(preSelect.x, preSelect.y, 3, 3);
                }
            }

            stroke(Color.RED.getRGB());
            for (Point each : points) {
                ellipse(each.x, each.y, 7, 7);
            }
        }

        @Override
        public void keyTyped(KeyEvent e) {
            super.keyTyped(e);
            // System.out.println(e.getKeyChar());
            if (e.getKeyChar() == 's') {
                preSelect = !preSelect;
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
            Point point = e.getPoint();
            if (!e.isControlDown()) {
                points.clear();
            }
            if (e.getButton() == MouseEvent.BUTTON1) {
                // button1 is 1st mouse button
                Point nearest = new MaxDistNearestNeighbor(map, width / 10)
                        .forLocation(point);
                if (nearest != null) {
                    points.add(nearest);
                    System.out.println(nearest);
                }
            } else if (e.getButton() == MouseEvent.BUTTON3) {
                // button3 is 2nd mouse button
                Point nearest = new NearestNeighbor(map).forLocation(point);
                points.add(nearest);
                System.out.println(nearest);
            }
        }

    }

    /**
     * Example Papplet class found at: http://abandonedart.org/?p=275
     */
    @SuppressWarnings("serial")
    public static class GlitchSticks extends PApplet {
        // Jan 2009
        // http://www.abandonedart.org
        // http://www.actionscripter.co.uk

        // ================================= global vars

        float _x = 0;
        float _y = 0;
        float _angle = 0;
        float _lasty = 250;
        float _lastx = 250;

        float _a = 5;
        float _b = 3;

        // ================================= init

        public void setup() {
            frameRate(60);
            size(500, 300);
            background(255);
            smooth();
        }

        // ================================= frame loop

        public void draw() {
            _angle += 0.5;
            if (_angle > 360) {
                _angle -= 360;
            }

            float preva = _a;
            float prevb = _b;
            _a = mouseX / 10;
            _b = mouseY / 10;

            if ((_a != preva) || (_b != prevb)) {
                strokeCap(PROJECT);
                strokeWeight((int) random(55) + 5);
                stroke(random(255));
            } else {
                strokeCap(ROUND);
                strokeWeight(1);
                stroke(255, 150);
            }

            _x = sin(_a * radians(_angle) + PI / 2) * 280;
            _y = sin(_b * radians(_angle)) * 180;
            _x += 250;
            _y += 150;

            line(_x, _y, _lastx, _lasty);

            _lastx = _x;
            _lasty = _y;
        }

        // ================================= interaction

        public void mousePressed() {
            background(255);
        }

    }

}
