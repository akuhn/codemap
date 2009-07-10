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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.jface.util.SafeRunnable;
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

	private List<ISelectionChangedListener> listeners = new ArrayList<ISelectionChangedListener>();
	private ISelection selection = StructuredSelection.EMPTY;
	private MapView view;
	private boolean forceToPackageExplorer = ForceSelectionAction.DEFAULT_VALUE;

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
	public void setSelection(final ISelection selection) {
		this.selection = selection;
		provideSelection(selection);
		forcePackageExplorerSelection(selection);
	}

	private void provideSelection(final ISelection selection) {
		final SelectionChangedEvent e = new SelectionChangedEvent(this, selection);
		for (final ISelectionChangedListener each: listeners) {
			SafeRunner.run(new SafeRunnable() {
				@Override
				public void run() {
					each.selectionChanged(e);
				}
			});
		}
	}

	private void forcePackageExplorerSelection(final ISelection selection) {
		if (!forceToPackageExplorer) return;
			
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				try {
					IViewPart showView = view.getSite().getPage().showView(PACKAGE_EXPLORER.id);
					((ISetSelectionTarget) showView).selectReveal(selection);
				} catch (PartInitException e1) {
					// TODO stuff
				}
			}
		});
	}

	public void setForceEnabled(boolean checked) {
		forceToPackageExplorer = checked;
	}

}
