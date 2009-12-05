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
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewPart;
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
		
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
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
