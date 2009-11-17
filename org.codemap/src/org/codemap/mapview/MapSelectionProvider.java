/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.codemap.mapview;

import static org.codemap.util.ID.PACKAGE_EXPLORER;

import java.util.HashSet;
import java.util.Set;

import org.codemap.mapview.action.ForceSelectionAction;
import org.codemap.util.Log;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ISetSelectionTarget;

/**
 * Copied from "org.eclipse.ui.internal.part" package.
 * 
 */
public class MapSelectionProvider implements ISelectionProvider {

	private Set<ISelectionChangedListener> listeners = new HashSet<ISelectionChangedListener>();
	private ISelection selection = StructuredSelection.EMPTY;
	private MapView view;
	private boolean forceToPackageExplorer = ForceSelectionAction.DEFAULT_CHECKED;

	public MapSelectionProvider(MapView mapView) {
		this.view = mapView;
		view.getSite().setSelectionProvider(this);
	}

	@Override
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		listeners.add(listener);
	}

	@Override
	public ISelection getSelection() {
		return selection;
	}

	@Override
	public void removeSelectionChangedListener(ISelectionChangedListener listener) {
		listeners.remove(listener);
	}

	@Override
	public void setSelection(final ISelection sel) {
		this.selection = sel;
		forcePackageExplorerSelection(selection);
		provideSelection(selection);
	}

	private void provideSelection(final ISelection newSelection) {
		final SelectionChangedEvent e = new SelectionChangedEvent(this, newSelection);
		/*
			TODO: listeners might need to be synchronized:
			java.util.ConcurrentModificationException
			at java.util.AbstractList$Itr.checkForComodification(AbstractList.java:372)
			at java.util.AbstractList$Itr.next(AbstractList.java:343)
			at org.codemap.mapview.MapSelectionProvider.provideSelection(MapSelectionProvider.java:70)
			at org.codemap.mapview.MapSelectionProvider.setSelection(MapSelectionProvider.java:64)
			at org.codemap.mapview.MapView$3.selectionChanged(MapView.java:171)
			at org.codemap.mapview.MapView$3.handleEvent(MapView.java:159)
			at ch.deif.meander.ui.CodemapEventRegistry$1.run(CodemapEventRegistry.java:19)
			at java.lang.Thread.run(Thread.java:637)
			at ch.deif.meander.ui.CodemapEventRegistry.fireEvent(CodemapEventRegistry.java:22)
			at ch.deif.meander.ui.MeanderApplet.fireEvent(MeanderApplet.java:42)
			at ch.deif.meander.visual.CurrentSelectionOverlay.handleDraggingStopped(CurrentSelectionOverlay.java:84)
			at ch.deif.meander.visual.CurrentSelectionOverlay.handleDragging(CurrentSelectionOverlay.java:65)
			at ch.deif.meander.visual.CurrentSelectionOverlay.handleEvents(CurrentSelectionOverlay.java:42)
			at ch.deif.meander.visual.CurrentSelectionOverlay.draw(CurrentSelectionOverlay.java:32)
			at ch.deif.meander.visual.Composite.drawChildren(Composite.java:30)
			at ch.deif.meander.visual.Composite.draw(Composite.java:25)
			at ch.deif.meander.visual.MapVisualization.draw(MapVisualization.java:70)
			at ch.deif.meander.ui.MeanderApplet.draw(MeanderApplet.java:55)
			at processing.core.PApplet.handleDraw(PApplet.java:1406)
			at processing.core.PApplet.run(PApplet.java:1311)
			at java.lang.Thread.run(Thread.java:637)
		 */
		for (final ISelectionChangedListener each: listeners) {
			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					each.selectionChanged(e);
				}
			});
		}
	}

	private void forcePackageExplorerSelection(final ISelection newSelection) {
		if (!forceToPackageExplorer) return;
		if (newSelection == null) return;
		
		// do syncExec to make sure that the selection is set before we proceed
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
//					IViewPart showView = view.getSite().getPage().showView(PACKAGE_EXPLORER.id);
				    IViewPart packageExplorer = view.getSite().getPage().findView(PACKAGE_EXPLORER.id);	
				    if (packageExplorer == null) return;
					((ISetSelectionTarget) packageExplorer).selectReveal(newSelection);
			}
		});
	}

	public void setForceEnabled(boolean checked) {
		forceToPackageExplorer = checked;
	}

}
