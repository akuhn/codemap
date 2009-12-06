package org.codemap.layers;

/**
 * Maintains focus information for the labels.
 * At the moment at most one label can be focused.
 * 
 * @author deif
 */
public class LabelFocus {

    private Label currentLabel;

    public void setLabel(Label label) {
        if (currentLabel != null) {
            currentLabel.setHasFocus(false);
        }
        currentLabel = label;
        if (currentLabel != null) {
            currentLabel.setHasFocus(true);
        }
    }

}
