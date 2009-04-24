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
import processing.core.PGraphics;
import ch.deif.meander.Location;
import ch.deif.meander.Map;
import ch.deif.meander.MaxDistNearestNeighbor;
import ch.deif.meander.NearestNeighbor;
import ch.deif.meander.viz.MapVisualization;

@SuppressWarnings("serial")
public class MeanderApplet extends PApplet {

    protected final int SELECTION_SIZE = 10;
    protected final int POINT_STROKE = 2;
    protected final int BOX_STROKE = 2;

    private MapVisualization viz;
    private Collection<Point> points;
    private IEventHandler event;

    private boolean preSelect = false;
    private boolean changed = false;
    private PGraphics background;

    private Point dragStart;
    private Point dragStop;

    public MeanderApplet(MapVisualization vizualization) {
        event = new NullEventHandler();
        viz = vizualization;
        // TODO check if the concurrency problem really comes from the points
        points = Collections.synchronizedSet(new HashSet<Point>());
        background = createGraphics(width(), height(), JAVA2D);
    }

    @Override
    public void setup() {
        size(width(), height());
        frameRate(25);
        setupBackground();
        drawBackground();
        setNeedsRedraw();
    }

    private void setupBackground() {
        background.beginDraw();
        viz.draw(background);
        background.endDraw();
    }

    @Override
    public void draw() {
        if (!(changed || preSelect)) return;
        smooth();
        noFill();
        strokeWeight(POINT_STROKE);

        drawBackground();
        drawSelectedPoints();
        drawPreSelectionPoint();
        drawSelectionBox();
        changed = false;
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
        // TODO: guarding if-statements
        if (preSelect) {
            stroke(Color.BLUE.getRGB());
            Point current = new Point(mouseX, mouseY);
            Point preSelect = new MaxDistNearestNeighbor(map(), width() / 10)
                    .forLocation(current);
            if (preSelect != null) {
                ellipse(preSelect.x, preSelect.y, 3, 3);
            }
        }
    }

    private void drawBackground() {
        image(background, 0, 0);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        super.keyTyped(e);
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
            NearestNeighbor nn = new MaxDistNearestNeighbor(map(), width() / 10);
            Point nearest = nn.forLocation(point);
            if (nearest != null) {
                points.add(nearest);
                event.onAppletSelection(nn.location());
            }
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            // button3 is 2nd mouse button
            NearestNeighbor nn = new NearestNeighbor(map());
            Point nearest = nn.forLocation(point);
            event.onAppletSelection(nn.location());
            points.add(nearest);
        }
        unsetSelectionBox();
        setNeedsRedraw();
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
            points.clear();
            for (Location each : map().locations()) {
                int x = (int) Math.round(each.x() * map().getHeight());
                int y = (int) Math.round(each.y() * map().getHeight());
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
            Location location = map().locationAt(index);
            locations.add(location);
            int x = (int) Math.round(location.x() * map().getHeight());
            int y = (int) Math.round(location.y() * map().getHeight());
            points.add(new Point(x, y));
        }
        // callback for tag-cloud
        event.onAppletSelection(locations);
        setNeedsRedraw();
    }

    protected void setNeedsRedraw() {
        changed = true;
    }

    private Map map() {
        return viz.map;
    }

    private int width() {
        return viz.map.getParameters().width;
    }

    private int height() {
        return viz.map.getParameters().height;
    }


}
