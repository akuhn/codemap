package org.codemap.resources;

import java.util.ArrayList;
import java.util.List;

import org.codemap.Location;
import org.codemap.MapInstance;
import org.codemap.tasks.ComputeElevationModelTask;
import org.codemap.util.MapScheme;
import org.codemap.util.Resources;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.junit.JUnitCore;

import ch.akuhn.values.Value;

public class ComputeFilteredElevationTask extends ComputeElevationModelTask {

    private Value<Boolean> showTests;

    public ComputeFilteredElevationTask(Value<MapInstance> mapInstance, Value<MapScheme<Boolean>> hills, Value<Boolean> showTests) {
        super(mapInstance, hills);
        // super constructor requires 2 params, so we must re-implement the behavior here ...
        showTests.addDependent(this);
        this.showTests = showTests;
    }
    
    @Override
    protected MapInstance processMap(MapInstance map) {
        if (!showTests.getValue()) {
            try {
                filterLocations(map);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            map.setDEMLoccations(null);
        }
        return super.processMap(map);
    }
    
    private void filterLocations(MapInstance map) throws OperationCanceledException, CoreException {
        List<Location> result = new ArrayList<Location>();
        for (Location each: map.locations()) {
            IJavaElement javaElement = Resources.asJavaElement(each.getDocument());
            IType[] findTestTypes = JUnitCore.findTestTypes(javaElement, null);
            // we have found a test within the resource, in our case this means the
            // resource is a test as the resources represent files a.k.a. compilation units
            if (findTestTypes.length >= 1) continue;
            result.add(each);
        }
        map.setDEMLoccations(result);
    }
}
