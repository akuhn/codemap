package org.codemap.eclemma;

import static java.util.Collections.emptySet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.codemap.mapview.MapController;
import org.codemap.util.CodemapColors;
import org.codemap.util.Log;
import org.codemap.util.MColor;
import org.codemap.util.MapScheme;
import org.codemap.util.Resources;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

import ch.akuhn.util.Pair;
import ch.akuhn.values.Value;

import com.mountainminds.eclemma.core.CoverageTools;
import com.mountainminds.eclemma.core.analysis.IJavaCoverageListener;
import com.mountainminds.eclemma.core.analysis.IJavaElementCoverage;
import com.mountainminds.eclemma.core.analysis.IJavaModelCoverage;

public class CoverageListener implements IJavaCoverageListener {

    protected final class CoverageResourceVisitor implements IResourceVisitor {
        private final List<Pair<String, Double>> identifiers;

        protected CoverageResourceVisitor(List<Pair<String, Double>> identifiers) {
            this.identifiers = identifiers;
        }

        @Override
        public boolean visit(IResource resource) throws CoreException {
            IJavaElement javaElement = JavaCore.create(resource);
            if (javaElement == null)
                return true;
            if (javaElement.getElementType() != IJavaElement.COMPILATION_UNIT)
                return true;
            ICompilationUnit compilationUnit = (ICompilationUnit) javaElement
                    .getAdapter(ICompilationUnit.class);
            return visit(compilationUnit);
        }

        private boolean visit(ICompilationUnit compilationUnit) {

            String identifier = Resources.asPath(compilationUnit);
            IJavaElementCoverage coverageInfo = CoverageTools
                    .getCoverageInfo(compilationUnit);
            if (coverageInfo == null) {
                // for interfaces where we do not have coverage information
                return false;
            }
            identifiers.add(new Pair<String, Double>(identifier, coverageInfo
                    .getLineCounter().getRatio()));
            return false;
        }
    }

    private MapController theController;
    private Set<Pair<String, Double>> lastCoverageInfo;
    private boolean enabled;

    public CoverageListener(MapController mapController) {
        theController = mapController;
        enabled = false;
        lastCoverageInfo = emptySet();
    }

    @Override
    public void coverageChanged() {
        IJavaModelCoverage coverage = CoverageTools.getJavaModelCoverage();
        if (coverage == null) return;

        Set<Pair<String, Double>> coverageInfo = new HashSet<Pair<String, Double>>();
        List<IJavaProject> projects = Arrays.asList(coverage
                .getInstrumentedProjects());
        for (IJavaProject each : projects) {
            if (!each.isOpen())
                continue;
            coverageInfo.addAll(compilationUnitCoverage(each));
        }
        updateCoverageInfo(coverageInfo);
    }

    private void updateCoverageInfo(Set<Pair<String, Double>> coverageInfo) {
        lastCoverageInfo = coverageInfo;
        maybeDisplayCoverage();
    }

    private void maybeDisplayCoverage() {
        if (!enabled) return;
        Value<MapScheme<MColor>> colorValue = theController.getActiveMap()
                .getValues().colorScheme;
        CodemapColors colorScheme = new CodemapColors(MColor.GRAY_204);
        for (Pair<String, Double> pair : lastCoverageInfo) {
            String identifier = pair.fst;
            Double ratio = pair.snd;
            int redVal = (int) ((1 - ratio) * 255);
            int greenVal = (int) (ratio * 255);
            MColor col = new MColor(redVal, greenVal, 0);
            colorScheme.setColor(identifier, col);
        }
        colorValue.setValue(colorScheme);
    }

    private List<Pair<String, Double>> compilationUnitCoverage(
            IJavaProject project) {
        List<Pair<String, Double>> identifiers = new ArrayList<Pair<String, Double>>();
        try {
            for (IPackageFragmentRoot root : project.getPackageFragmentRoots()) {
                IResource resource = root.getResource();
                if (resource == null) continue;
                resource.accept(new CoverageResourceVisitor(identifiers));
            }
        } catch (JavaModelException e) {
            Log.error(e);
        } catch (CoreException e) {
            Log.error(e);
        }
        return identifiers;
    }

    public void setEnabled(boolean b) {
        enabled = b;
        maybeDisplayCoverage();
    }
}
