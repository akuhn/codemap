package org.codemap.plugin.eclemma;

import java.util.List;

import org.codemap.CodemapCore;
import org.codemap.mapview.action.ICodemapPluginAction;
import org.eclipse.jface.action.Action;

import ch.akuhn.util.Pair;
import ch.akuhn.values.Value;
import ch.deif.meander.util.CodemapColors;
import ch.deif.meander.util.MColor;
import ch.deif.meander.util.MapScheme;

public class ShowCoverageAction implements ICodemapPluginAction {

    private Action action;
    private List<Pair<String, Double>> lastCoverageInfo;

    public ShowCoverageAction() {
        EclemmaPlugin.getPlugin().registerCoverageAction(this);
    }

    @Override
    public void run(Action act) {
        action = act;
        if (isChecked()) showCoverage();
        else hideCoverage();
    }

    private void hideCoverage() {
        CodemapCore core = CodemapCore.getPlugin();
        Value<MapScheme<MColor>> colorScheme = core.getActiveMap().getValues().colorScheme;
        colorScheme.setValue(core.getDefaultColorScheme());
    }

    public boolean isChecked() {
        if (action == null) return false;
        return action.isChecked();
    }

    public void newCoverageAvailable(List<Pair<String, Double>> coverageInfo) {
        lastCoverageInfo = coverageInfo;		
        if (isChecked()) {
            showCoverage();
        }		
    }

    private void showCoverage() {
        CodemapColors colorScheme = new CodemapColors(MColor.GRAY_204);
        if (lastCoverageInfo == null) {
            updateColorScheme(colorScheme);
            return;
        }
        for (Pair<String, Double> pair : lastCoverageInfo) {
            String identifier = pair.fst;
            Double ratio = pair.snd;
            int redVal = (int) ((1 - ratio) * 255);
            int greenVal = (int) (ratio * 255);
            MColor col = new MColor(redVal, greenVal, 0);
            colorScheme.setColor(identifier, col);
        }
        updateColorScheme(colorScheme);
    }

    private void updateColorScheme(CodemapColors colorScheme) {
        CodemapCore.getPlugin().getActiveMap().getValues().colorScheme.setValue(colorScheme);
    }

}
