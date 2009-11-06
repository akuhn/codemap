package org.codemap.flow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.codemap.CodemapCore;
import org.codemap.MapPerProject;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.internal.corext.callhierarchy.CallerMethodWrapper;
import org.eclipse.jdt.internal.corext.callhierarchy.MethodCall;
import org.eclipse.jdt.internal.corext.callhierarchy.MethodWrapper;

import ch.akuhn.util.List;
import ch.akuhn.values.ReferenceValue;
import ch.deif.meander.swt.SWTLayer;

public class FlowModel {

    private MethodCallNode rootNode;
    private ReferenceValue<Object> trigger = new ReferenceValue<Object>();
    private boolean dirty = true;
    
    public void newRoot(MethodWrapper currentRootMethod) {
        rootNode = new MethodCallNode(currentRootMethod, this);
    }
    
    public void expand(MethodWrapper source, List<MethodWrapper> targets) {
        MethodCallPath nodePath = new MethodCallPath(source);
        MethodCallNode toExpand = nodePath.walk(rootNode);
        // we might be outside the current scope        
        if (toExpand == null) return;
        
        toExpand.setChildren(targets);
    }

    public void collapse(MethodWrapper source) {
        MethodCallPath nodePath = new MethodCallPath(source);
        MethodCallNode toCollapse = nodePath.walk(rootNode);
        // we might be outside the current scope
        if (toCollapse == null) return;
        
        toCollapse.clearChildren();
    }

    public void setDirty() {
        MapPerProject activeMap = CodemapCore.getPlugin().getActiveMap();
        SWTLayer layer = activeMap.getLayer(FLowOverlay.class);
        if (layer == null) {
            layer = new FLowOverlay(this);
            activeMap.addLayer(layer);
            activeMap.redrawWhenChanges(trigger);
        }
        trigger.setValue(new Object());
        dirty = true;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setClean() {
        dirty = false;
    }

    public void accept(GraphConversionVisitor visitor) {
        rootNode.accept(visitor);
    }
}

class MethodCallPath {
    
    ArrayList<MethodWrapper> path;

    public MethodCallPath(MethodWrapper source) {
        path = new ArrayList<MethodWrapper>(source.getLevel());
        path.add(source);
        MethodWrapper current = source;
        MethodWrapper parent;
        while( (parent = current.getParent()) != null) {
            path.add(0, parent);
            current = parent;
        }
    }

    public MethodCallNode walk(MethodCallNode rootNode) {
        if (path.size() <= 1) return rootNode;
        
        MethodCallNode target = rootNode;
        java.util.List<MethodWrapper> subList = path.subList(1, path.size());
        for (MethodWrapper each: subList) {
            target = target.getChild(each);
            if (target == null) return null;
        }
        return target;
    }
}

class MethodCallNode {

    private MethodWrapper sourceMethod;
    private ArrayList<MethodCallNode> children;
    private FlowModel model;

    public MethodCallNode(MethodWrapper currentRootMethod, FlowModel flowModel) {
        model = flowModel;
        sourceMethod = currentRootMethod;
        children = new ArrayList<MethodCallNode>();
    }

    public void accept(GraphConversionVisitor visitor) {
        visitor.visit(this);
        for(MethodCallNode each: children) {
            each.accept(visitor);
        }
    }

    public void clearChildren() {
        if (children.isEmpty()) return;
        children.clear();
        model.setDirty();
    }

    public void setChildren(List<MethodWrapper> targets) {
        children.clear();
        for (MethodWrapper each: targets) {
            children.add(new MethodCallNode(each, model));
        }
        model.setDirty();
    }

    public MethodCallNode getChild(MethodWrapper wrapper) {
        for (MethodCallNode child: children) {
            if (child.represents(wrapper)) return child;
        }
        return null;
    }

    private boolean represents(MethodWrapper m) {
        return sourceMethod.equals(m);
    }

    public MethodWrapper getSourceMethod() {
        return sourceMethod;
    }

    public ArrayList<MethodCallNode> getChildren() {
        return children;
    }
}
