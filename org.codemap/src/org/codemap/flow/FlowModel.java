package org.codemap.flow;

import org.eclipse.jdt.internal.corext.callhierarchy.CallerMethodWrapper;

public class FlowModel {

    private CallerMethodWrapper rootMethod;

    public void setCurrentRoot(CallerMethodWrapper root) {
        // need an instance check here, if they are equal the call hierarchy was reloaded
        if (rootMethod == root) return;
        
        rootMethod = root;
        System.out.println("new root loaded");
    }
    
    

}
