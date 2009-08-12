package ch.deif.meander.map;

import org.eclipse.swt.graphics.Image;

import ch.akuhn.util.ProgressMonitor;
import ch.akuhn.values.Arguments;
import ch.akuhn.values.TaskValue;
import ch.akuhn.values.Value;
import ch.deif.meander.DigitalElevationModel;
import ch.deif.meander.HillShading;
import ch.deif.meander.util.MColor;
import ch.deif.meander.util.MapScheme;

public class ComputeBackgroundTask extends TaskValue<Image> {

    public ComputeBackgroundTask(Value<DigitalElevationModel>  elevationModel, Value<HillShading> shading, Value<MapScheme<MColor>> colors) {
        super("Caching background image", elevationModel, shading, colors);
    }

    @Override
    protected Image computeValue(ProgressMonitor monitor, Arguments arguments) {
        // TODO Auto-generated method stub
        return null;
    }

}
