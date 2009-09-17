package edu.stanford.hci.flowmap.layout;

import java.awt.Dimension;
import java.awt.geom.Point2D;

import edu.stanford.hci.flowmap.db.QueryRow;
import edu.stanford.hci.flowmap.db.RowSchema;

/**
 * Maps the position directly to the X and Y attributes.
 * @author dphan
 *
 * This software is distributed under the Berkeley Software Distribution License.
 * Please see http://graphics.stanford.edu/~dphan/code/bsd.license.html
 *
 */
public class DirectPositionLayout extends PositionLayout {

	public static final String X_POS="x";
	public static final String Y_POS="y";
	
	private String xString;
	private String yString;
	
	public DirectPositionLayout(Dimension d) {
		super(d);
	}
	
	public Point2D computePostion(QueryRow row) {
		Double x = ((Double) row.getInfo(xString)).doubleValue();
		Double y = ((Double) row.getInfo(yString)).doubleValue();
		return getPoint(x.doubleValue(), y.doubleValue());
	}

	@Override
	public boolean doesSrcHavePosition() {
		return true;
	}

	/**************************************************************************
	 * Methods to set the proper identifiers lat and lon
	 *************************************************************************/
	private final int nameColumn = 1;
	private final int xColumn = 2;
	private final int yColumn = 3;
	private final int valueColumn = 4;
	
	/**
	 * Sets the column mapping
	 * @param index the index of the data
	 * @param name the name of the type
	 * @param rowSchema the row schema to use
	 */
	public void setColumnMapping(int index, String name, RowSchema rowSchema) {
		switch(index) {
			case nameColumn:
				rowSchema.setNameId(name);
				break;
			case xColumn:
				rowSchema.setXId(name);
				xString = name;
				break;
			case yColumn:
				rowSchema.setYId(name);
				yString = name;
				break;
			case valueColumn:
				rowSchema.setValueId(name);
				break;
		}
	}
	
	
}
