package ch.deif.meander.ui;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import processing.core.PApplet;
import processing.core.PImage;
import ch.deif.meander.Location;
import ch.deif.meander.Map;
import ch.deif.meander.MapVisualization;
import ch.deif.meander.MaxDistNearestNeighbor;
import ch.deif.meander.NearestNeighbor;

public class Applet {

    @SuppressWarnings("serial")
    public static class MapViz extends PApplet {

        protected final int SELECTION_SIZE = 12;
        protected final int POINT_STROKE = 4;
        protected final int BOX_STROKE = 2;

        private MapVisualization viz;
        private int width;
        private int height;
        private Map map;
        private Collection<Point> points;
        private IEventHandler event;

        private boolean preSelect = false;
        private boolean changed = false;
        private PImage background;

        private Point dragStart;
        private Point dragStop;

        public MapViz(MapVisualization vizualization) {
        	event = new NullEventHandler();
            viz = vizualization;
            width = viz.map.getParameters().width;
            height = viz.map.getParameters().height;
            map = viz.map;
            // TODO check if the concurrency problem really comes from the points
            points = Collections.synchronizedSet(new HashSet<Point>());
            background = new PImage(width, height);
        }

        @Override
        public void setup() {
            frameRate(25);
            smooth();
            noFill();
            strokeWeight(POINT_STROKE);
            size(width, height);
            viz.drawOn(background);
            loadPixels();
            setNeedsRedraw();
        }

        @Override
        public void draw() {
            if (changed || preSelect) {
                changed = false;
                drawBackground();
                drawSelectedPoints();
                drawPreSelectionPoint();
                drawSelectionBox();
            }
        }

        private void drawSelectionBox() {
            if (dragStart != null && dragStop != null) {
                stroke(Color.RED.getRGB());
                strokeWeight(BOX_STROKE);
                int deltaX = dragStop.x - dragStart.x;
                int deltaY = dragStop.y - dragStart.y;
                rect(dragStart.x, dragStart.y, deltaX, deltaY);
                strokeWeight(POINT_STROKE);
            }
        }

        private void drawSelectedPoints() {
            for (Point each : points) {
                stroke(Color.RED.getRGB());
                ellipse(each.x, each.y, SELECTION_SIZE, SELECTION_SIZE);
            }
        }

        private void drawPreSelectionPoint() {
            if (preSelect) {
                stroke(Color.BLUE.getRGB());
                Point current = new Point(mouseX, mouseY);
                Point preSelect = new MaxDistNearestNeighbor(map, width / 10)
                        .forLocation(current);
                if (preSelect != null) {
                    ellipse(preSelect.x, preSelect.y, 3, 3);
                }
            }
        }

        private void drawBackground() {
            assert background.pixels != null;
            assert pixels != null;
            System.arraycopy(background.pixels, 0, pixels, 0,
                    background.pixels.length);
            updatePixels();
        }

        @Override
        public void keyTyped(KeyEvent e) {
            super.keyTyped(e);
            // System.out.println(e.getKeyChar());
            if (e.getKeyChar() == 's') {
                togglePreSelect();
                setNeedsRedraw();
            }
        }

        protected void togglePreSelect() {
            preSelect = !preSelect;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
            Point point = e.getPoint();
            if (!e.isControlDown()) {
                points.clear();
                event.onAppletSelectionCleared();
            }
            if (e.getButton() == MouseEvent.BUTTON1) {
                // button1 is 1st mouse button
                NearestNeighbor nn = new MaxDistNearestNeighbor(map, width / 10);
                Point nearest = nn.forLocation(point);
                if (nearest != null) {
                    points.add(nearest);
                    event.onAppletSelection(nn.location());
                }
            } else if (e.getButton() == MouseEvent.BUTTON3) {
                // button3 is 2nd mouse button
                NearestNeighbor nn = new NearestNeighbor(map);
                Point nearest = nn.forLocation(point);
                event.onAppletSelection(nn.location());
                points.add(nearest);
            }
            unsetSelectionBox();
            setNeedsRedraw();
            // System.out.println("Mouse clicked");
        }

        private void unsetSelectionBox() {
            dragStart = null;
            dragStop = null;
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            super.mouseDragged(e);
            if (dragStart == null) {
                dragStart = e.getPoint();
            }
            dragStop = e.getPoint();
            setNeedsRedraw();
        }

        public void mouseReleased(MouseEvent e) {
            super.mouseReleased();
            if (dragStart != null && dragStop != null) {
                // make sure that start is top-left and stop is bottom-right
                int minX = Math.min(dragStart.x, dragStop.x);
                int minY = Math.min(dragStart.y, dragStop.y);
                int maxX = Math.max(dragStart.x, dragStop.x);
                int maxY = Math.max(dragStart.y, dragStop.y);
                dragStart = new Point(minX, minY);
                dragStop = new Point(maxX, maxY);
                List<Location> selected = new ArrayList<Location>();
                // System.out.println("start: " + dragStart.x + " " +
                // dragStart.y);
                // System.out.println("stop: " + dragStop.x + " " + dragStop.y);
                points.clear();
                for (Location each : map.locations()) {
                    int x = (int) Math.round(each.x * map.height);
                    int y = (int) Math.round(each.y * map.height);
                    if (x < dragStop.x && x > dragStart.x && y < dragStop.y
                            && y > dragStart.y) {
                        selected.add(each);
                        points.add(new Point(x, y));
                    }
                }
                unsetSelectionBox();
                event.onAppletSelection(selected);
                setNeedsRedraw();
            }
        }

        public void registerHandler(IEventHandler eventHandler) {
            this.event = eventHandler;
        }

        public void indicesSelected(int[] indices) {
            points.clear();
            event.onAppletSelectionCleared();
            List<Location> locations = new ArrayList<Location>();
            for (int index : indices) {
                Location location = map.locations.get(index);
                locations.add(location);
                int x = (int) Math.round(location.x * map.height);
                int y = (int) Math.round(location.y * map.height);
                points.add(new Point(x, y));
            }
            // callback for tag-cloud
            event.onAppletSelection(locations);
            setNeedsRedraw();
        }

        protected void setNeedsRedraw() {
            changed = true;
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
