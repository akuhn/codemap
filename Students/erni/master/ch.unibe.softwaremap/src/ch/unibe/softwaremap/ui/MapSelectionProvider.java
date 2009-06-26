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
package ch.unibe.softwaremap.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;


/**
 * Copied from "org.eclipse.ui.internal.part" package.
 * 
 */
public class MapSelectionProvider implements ISelectionProvider {

	List<ISelectionChangedListener> listeners = new ArrayList<ISelectionChangedListener>();
	ISelection theSelection = StructuredSelection.EMPTY;
	private MapView view;

	public MapSelectionProvider(MapView mapView) {
		this.view = mapView;
		view.getSite().setSelectionProvider(this);
	}

	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		listeners.add(listener);
	}

	public ISelection getSelection() {
		return theSelection;
	}

	public void removeSelectionChangedListener(ISelectionChangedListener listener) {
		listeners.remove(listener);
	}

	public void setSelection(ISelection selection) {
		theSelection = selection;
		final SelectionChangedEvent e = new SelectionChangedEvent(this, selection);
		for (final ISelectionChangedListener each: listeners) {
			SafeRunner.run(new SafeRunnable() {
				public void run() {
					each.selectionChanged(e);
				}
			});
		}
	}

}
