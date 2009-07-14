package org.codemap.plugin.marker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.codemap.CodemapCore;
import org.codemap.util.Log;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IMarkerDelta;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import ch.deif.meander.MapSelection;

/**
 * The activator class controls the plug-in life cycle
 */
public class MarkerPluginCore extends AbstractUIPlugin {
	
	// ----------------------------------------------------------------------
	// ----------------------------------------------------------------------
	// found out how to listen to resource changes from:
	// @see BreakpointManager.class
	// @see IMarker.class
	// @see MarkerUtilities.class
	// @see MarkerUtil.class (great naming btw.)
	// @see ExtendedMarkersView.class
	// @see MarkerSupportRegistry.class
	// ----------------------------------------------------------------------
	// ----------------------------------------------------------------------	
	
	private IResourceDeltaVisitor resourceDeltaVisitor = new IResourceDeltaVisitor() {

		@Override
		public boolean visit(IResourceDelta delta) throws CoreException {
			if (delta == null) {
				return false;
			}
			if (resourceOpenStateChanged(delta)) {
				handleProjectResourceOpenStateChange(delta.getResource());
				return false;
			}
			for (IMarkerDelta markerDelta: delta.getMarkerDeltas()) {
				switch (markerDelta.getKind()) {
					case IResourceDelta.ADDED :
						handleAddMarker(markerDelta.getMarker());
						break;
					case IResourceDelta.REMOVED :
						handleRemoveMarker(markerDelta.getMarker());
						break;
				}
			}
			return true;
		}

		private boolean resourceOpenStateChanged(IResourceDelta delta) {
			return 0 != (delta.getFlags() & IResourceDelta.OPEN) && 0 == (delta.getFlags() & IResourceDelta.MOVED_FROM);
		}
		
		private void handleRemoveMarker(IMarker marker) {
			removeMarker(marker);
			System.out.println("removed marker " + marker.getClass() + " @" + marker.getResource().getName());
		}

		private void handleAddMarker(IMarker marker) {
			addMarker(marker);
			System.out.println("added marker " + marker.getClass() + " @" + marker.getResource().getName());
			
		}

		/**
		 * A project has been opened or closed.  Updates the breakpoints for
		 * that project
		 */
		private void handleProjectResourceOpenStateChange(IResource project) {
			if (!project.isAccessible()) {
				handleClosedProject(project);
			} else {
				handleOpenedProject(project);
			}
		}

		private void handleOpenedProject(IResource project) {
			IMarker[] findMarkers;
			try {
				findMarkers = project.findMarkers(IMarker.MARKER, true, IResource.DEPTH_INFINITE);
				System.out.println("found " + findMarkers.length + " markers in opened project");
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}

		private void handleClosedProject(IResource project) {
			// TODO: iterate over the list of markers and delete the ones from the closed project
//			Enumeration breakpoints= ((Vector)getBreakpoints0().clone()).elements();
//			while (breakpoints.hasMoreElements()) {
//				IBreakpoint breakpoint= (IBreakpoint) breakpoints.nextElement();
//				IResource markerResource= breakpoint.getMarker().getResource();
//				if (project.getFullPath().isPrefixOf(markerResource.getFullPath())) {
//					fRemoved.add(breakpoint);
//				}
//			}
			
		}			
	};
	
	private IResourceChangeListener resourceChangeListener = new IResourceChangeListener() {
		/*
		 * @see IResourceChangeListener#resourceChanged(org.eclipse.core.resources.IResourceChangeEvent)
		 */
		@Override
		public void resourceChanged(IResourceChangeEvent event) {
			IResourceDelta delta= event.getDelta();
			if (delta == null) return;
			
			try {
				delta.accept(resourceDeltaVisitor);
			} catch (CoreException e) {
				Log.error(e);
			}
		}
	};
	

	public static final String PLUGIN_ID = "org.codemap.plugin.marker";
	private static MarkerPluginCore plugin;
	private MapSelection markerSelection;

	private MarkersOverlay markersOverlay;
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		markerSelection = new MapSelection();
		markersOverlay = new MarkersOverlay();
		markersOverlay.setSelection(markerSelection);
		CodemapCore.getPlugin().addLayer(markersOverlay);
		ResourcesPlugin.getWorkspace().addResourceChangeListener(resourceChangeListener);				
	}

	private void removeMarker(IMarker marker) {
		IJavaElement javaElement = JavaCore.create(marker.getResource());
		if (javaElement == null) return;
		String identifier = javaElement.getHandleIdentifier();
		markerSelection.remove(identifier);
		CodemapCore.getPlugin().redrawCodemap();
	}

	private void addMarker(IMarker marker) {
		IJavaElement javaElement = JavaCore.create(marker.getResource());
		if (javaElement == null) return;
		String identifier = javaElement.getHandleIdentifier();
		markerSelection.add(identifier);
		CodemapCore.getPlugin().redrawCodemap();
	}
	
	/**
	 * Loads all markers on the given resource.
	 * 
	 * @param resource the resource which contains the markers
	 */
	private void loadMarkers(IResource resource) {
		try {
			IMarker[] markers = resource.findMarkers(IMarker.MARKER, true, IResource.DEPTH_INFINITE);
			markerSelection.addAll(collectIdentifiers(markers));
			CodemapCore.getPlugin().redrawCodemap();
		} catch (CoreException e) {
			Log.error(e);
		}
	}	

	private Collection<String> collectIdentifiers(IMarker[] markers) {
		List<String> identifiers = new ArrayList<String>();
		for(IMarker each: markers) {
			IJavaElement javaElement = JavaCore.create(each.getResource());
			if (javaElement == null) continue;
			identifiers.add(javaElement.getHandleIdentifier());
		}
		return identifiers;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(resourceChangeListener);
		CodemapCore.getPlugin().removeLayer(markersOverlay);
		markersOverlay = null;
		markerSelection = null;
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static MarkerPluginCore getPlugin() {
		return plugin;
	}
	
	/**
	 * Loads all markers of the current workspace as soon as the markers action runs for the
	 * first time.
	 * 
	 * @param showMarkersAction
	 */
	public void register(ShowMarkersAction showMarkersAction) {
		loadMarkers(ResourcesPlugin.getWorkspace().getRoot());
	}	
}
