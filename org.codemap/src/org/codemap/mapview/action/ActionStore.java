package org.codemap.mapview.action;

import java.util.HashMap;


public class ActionStore {
    
    private HashMap<Class<? extends CommandAction>, CommandAction> store;

    public ActionStore() {
        store = new HashMap<Class<? extends CommandAction>, CommandAction>();
    }

    public CommandAction get(Class<? extends CommandAction> clazz) {
        return store.get(clazz);
    }

    public void put(CommandAction action) {
        store.put(action.getClass(), action);
    }

}
