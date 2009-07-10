package ch.deif.meander.swt;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.events.DragDetectEvent;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.GC;

import ch.deif.meander.MapInstance;

public class CompositeLayer extends SWTLayer {

	private List<SWTLayer> children = new ArrayList<SWTLayer>();
	
	@Override
	public void paintMap(MapInstance map, GC gc) {
		for (SWTLayer each: children ) each.paintMap(map, gc);
	}
	
	public CompositeLayer add(SWTLayer layer) {
		children.add(layer);
		layer.root = this.root;
		return this;
	}

	public CompositeLayer remove(SWTLayer layer) {
		children.remove(layer);
		layer.root = null;
		return this;
	}

	@Override
	public void dragDetected(DragDetectEvent e) {
		for (SWTLayer each: children ) each.dragDetected(e);
	}

	@Override
	public void menuDetected(MenuDetectEvent e) {
		for (SWTLayer each: children ) each.menuDetected(e);
	}

	@Override
	public void mouseDoubleClick(MouseEvent e) {
		for (SWTLayer each: children ) each.mouseDoubleClick(e);
	}

	@Override
	public void mouseDown(MouseEvent e) {
		for (SWTLayer each: children ) each.mouseDown(e);
	}

	@Override
	public void mouseEnter(MouseEvent e) {
		for (SWTLayer each: children ) each.mouseEnter(e);
	}

	@Override
	public void mouseExit(MouseEvent e) {
		for (SWTLayer each: children ) each.mouseExit(e);
	}

	@Override
	public void mouseHover(MouseEvent e) {
		for (SWTLayer each: children ) each.mouseHover(e);
	}

	@Override
	public void mouseMove(MouseEvent e) {
		for (SWTLayer each: children ) each.mouseMove(e);
	}

	@Override
	public void mouseUp(MouseEvent e) {
		for (SWTLayer each: children ) each.mouseUp(e);
	}
	
	@Override
	public void mouseScrolled(MouseEvent e) {
		for (SWTLayer each: children ) each.mouseScrolled(e);
	}
	
}
