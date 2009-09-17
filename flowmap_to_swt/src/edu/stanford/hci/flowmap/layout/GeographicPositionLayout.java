package edu.stanford.hci.flowmap.layout;

import java.awt.Dimension;
import java.awt.geom.Point2D;

import edu.stanford.hci.flowmap.db.QueryRow;
import edu.stanford.hci.flowmap.db.RowSchema;

/**
 * This software is distributed under the Berkeley Software Distribution License.
 * Please see http://graphics.stanford.edu/~dphan/code/bsd.license.html
 *
 */
public class GeographicPositionLayout extends PositionLayout {
	
	private String latitude, longitude;

	public GeographicPositionLayout(Dimension d) {
		super(d);
		latitude = longitude = null;
	}
	
	public Point2D computePostion(QueryRow row) {
		double lat, lon;
		//System.out.println(row);
		if (row.hasDefaultSchema()) {
			lat = row.getValue(RowSchema.X_POS);
			lon = row.getValue(RowSchema.Y_POS);
		} else {
			lat = row.getValue(latitude);
			lon = row.getValue(longitude);
		}
		Point2D pt = getPoint(lat, lon);
		
		//this just scales lat/lon to the screen directly
		
		pt = MapProjection.mercatorProjection(pt.getX(), pt.getY());
		MapProjection.mercatorToScreen(pt, screenSize);
		return pt;
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
				this.latitude = name;
				break;
			case yColumn:
				rowSchema.setYId(name);
				this.longitude = name;
				break;
			case valueColumn:
				rowSchema.setValueId(name);
				break;
		}
	}
	public boolean doesSrcHavePosition() {
		return true;
	}
}
