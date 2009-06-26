package ch.unibe.softwaremap.ui;

import static ch.unibe.softwaremap.ui.MapView.MAP_VIEW_ID;
import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jdt.launching.LibraryLocation;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ISetSelectionTarget;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.deif.meander.Map;
import ch.deif.meander.viz.Layers;
import ch.deif.meander.viz.MapVisualization;
import ch.deif.meander.viz.SelectionOverlay;
import ch.unibe.eclipse.util.ID;
import ch.unibe.softwaremap.BaseTest;
import ch.unibe.softwaremap.CodemapCore;

public class MapViewTest extends BaseTest {

	private static IJavaProject javaProject;
	private MapView mapView;

	@BeforeClass
	public static void setUpOnce() throws CoreException {
		waitForJobs();
		initDummyProject();
		waitForJobs();
	}

	protected static IProject initDummyProject() throws CoreException {
		System.out.println("Start creating project");
		
		IProject project = createProject();
		javaProject = createJavaProject(project); 
		setClassPath(javaProject);
		IPackageFragmentRoot root = addSourceFolder(project, javaProject);
		addSourceFile(root);
		System.out.println("done creating project");
		
		return project;
	}

	private static void addSourceFile(IPackageFragmentRoot root)
			throws JavaModelException {
		IPackageFragment pack = root.createPackageFragment("foo", false, null);

		String content = "//Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.";
		createMockClass(pack, content, "Foo");
		createMockClass(pack, content, "Bar");
	}

	private static void createMockClass(IPackageFragment pack, String content, String className) throws JavaModelException {
		StringBuffer buffer = new StringBuffer();
		buffer.append("package " + pack.getElementName() + ";\n");
		buffer.append("public class " + className + "{\n");
		buffer.append(content);
		buffer.append("\n}");
		pack.createCompilationUnit(className + ".java", buffer.toString(), false, null);
		
	}

	private static IPackageFragmentRoot addSourceFolder(IProject project,
			IJavaProject javaProject) throws CoreException, JavaModelException {
		IFolder sourceFolder = project.getFolder("src");
		sourceFolder.create(false, true, null);
		IPackageFragmentRoot root = javaProject.getPackageFragmentRoot(sourceFolder);
		
		IClasspathEntry[] oldEntries = javaProject.getRawClasspath();
		IClasspathEntry[] newEntries = new IClasspathEntry[oldEntries.length + 1];
		System.arraycopy(oldEntries, 0, newEntries, 0, oldEntries.length);
		newEntries[oldEntries.length] = JavaCore.newSourceEntry(root.getPath());
		javaProject.setRawClasspath(newEntries, null);
		return root;
	}

	private static void setClassPath(IJavaProject javaProject)
			throws JavaModelException {
		List<IClasspathEntry> entries = new ArrayList<IClasspathEntry>();
		IVMInstall vmInstall = JavaRuntime.getDefaultVMInstall();
		LibraryLocation[] locations = JavaRuntime.getLibraryLocations(vmInstall);
		for (LibraryLocation element : locations) {
		 entries.add(JavaCore.newLibraryEntry(element.getSystemLibraryPath(), null, null));
		}
		//add libs to project class path
		javaProject.setRawClasspath(entries.toArray(new IClasspathEntry[entries.size()]), null);
	}

	private static IJavaProject createJavaProject(IProject project)
			throws CoreException {
		IProjectDescription description = project.getDescription();
		description.setNatureIds(new String[] { JavaCore.NATURE_ID });
		project.setDescription(description, null);

		IJavaProject javaProject = JavaCore.create(project);
		return javaProject;
	}

	private static IProject createProject() throws CoreException {
		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = workspaceRoot.getProject("test-java-project");
		if (project.exists()) {
			project.delete(true, true, null);
		}
		project.create(null);
		project.open(null);
		
		return project;
	}

	@Before
	public void setUp() throws PartInitException {
		// Initialize the test fixture for each test
		// that is run.
		waitForJobs();
		mapView = (MapView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(MAP_VIEW_ID);
		waitForJobs();
	}
	
	@After
	public void tearDown() {
		waitForJobs();
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().hideView(mapView);
	}

	@Test
	public void testView() throws PartInitException {
		selectJavaProject();
		Layers layers = testLayersLoaded();
		Map map = layers.map;
		assertEquals(2, map.locationCount());
	}

	private Layers testLayersLoaded() {
		MapVisualization visualization = mapView.softwareMap().getApplet().getVisualization();
		assertTrue(visualization instanceof Layers);
		Layers layers = (Layers) visualization;
		MapVisualization mapVisualization = layers.get(SelectionOverlay.class);
		assertTrue(mapVisualization instanceof SelectionOverlay);
		SelectionOverlay overlay = (SelectionOverlay) mapVisualization;
		return layers;
	}

	private void selectJavaProject() throws PartInitException {
		assertNotNull(mapView);
		
		StructuredSelection selection = new StructuredSelection(javaProject);
		IViewPart showView = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(ID.PACKAGE_EXPLORER.id);
		((ISetSelectionTarget) showView).selectReveal(selection);		
		
		waitForJobs();

	}

}
