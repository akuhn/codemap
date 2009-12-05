package org.codemap.layers;

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
