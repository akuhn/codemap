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
public abstract class PositionLayout {

	protected Dimension screenSize;
	
	public PositionLayout(Dimension screenSize) {
		this.screenSize = screenSize;
		//System.out.println("PositionLayout got screenSize " + screenSize);
	}
	
	/**
	 * Called by the class that queries the database
	 * @param index column index of the data in the query (usually starts at one)
	 * @param name column name of the data (we use this name to retrieve the data)
	 * @param rowSchema the rowSchema
	 */
	public abstract void setColumnMapping(int index, String name, RowSchema rowSchema);
	
	public abstract Point2D computePostion(QueryRow row);
	
	protected Point2D getPoint(double x, double y) {
		return new Point2D.Double(x, y) {
			public String toString() {
				return "("+x+","+y+")";
			}
		};
	}
	
	public abstract boolean doesSrcHavePosition();
}
