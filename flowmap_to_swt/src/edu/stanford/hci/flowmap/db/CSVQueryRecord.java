package edu.stanford.hci.flowmap.db;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import edu.stanford.hci.flowmap.layout.DirectPositionLayout;
import edu.stanford.hci.flowmap.layout.GeographicPositionLayout;
import edu.stanford.hci.flowmap.layout.PositionLayout;
import flowmap.swt.main.Globals;

/**
 * Constructs a QueryRecord from a CSV file.
 * 
 * The file format is: any line starting with # is discarded. It is expected that the
 * first line is the type of data direct or latitude/longitude
 * 
 * layout: type (mercator or direct)
 * root name, root x, root y
 * destination name, destination x, destination y
 * 
 * This software is distributed under the Berkeley Software Distribution License.
 * Please see http://graphics.stanford.edu/~dphan/code/bsd.license.html
 *
 */
public class CSVQueryRecord extends QueryRecord {

	RowSchema csvRowSchema;
	PositionLayout posLayout;
	
	public CSVQueryRecord(File csvFile) {
		super();
		
		// load position layout by reading file
		boolean noLayout = true;
		try {
			BufferedReader br = new BufferedReader(new FileReader(csvFile));
			String line = br.readLine();
			while(line != null) {
				//System.out.println("Reading line: " + line);
				if (line.startsWith("layout:")) {
					if (line.contains("mercator")) {
						posLayout = new GeographicPositionLayout(Globals.getScreenDimension());						
					} else if (line.contains("direct")){
						posLayout = new DirectPositionLayout(Globals.getScreenDimension());
					} else {
						throw new RuntimeException("Unable to understand CSV File. Got line " + line + ".\n Was expecting layout: geographic or layout: direct");
					}
					noLayout=false;
				} else if (line.startsWith("edgeRouting")) {
					if (line.contains("true")) {
						Globals.runNodeEdgeRouting = true;
					} else if (line.contains("false")) {
						Globals.runNodeEdgeRouting = false;
					} else {
						throw new RuntimeException("Unable to understand CSV File. Got line " + line + ".\n Was expecting edgeRouting: true or edgeRouting: false");
					}
				} else if (line.startsWith("adjustNodes")) {
					if (line.contains("true")) {
						Globals.useLayoutAdjustment = true;
					} else if (line.contains("false")) {
						Globals.useLayoutAdjustment = false;
					} else {
						throw new RuntimeException("Unable to understand CSV File. Got line " + line + ".\n Was expecting adjustNodes: true or adjustNodes: false");
					}
				} 
				line = br.readLine();
			}
			if (noLayout) {
				throw new RuntimeException("CSV File did not contain layout information.");
			} else {
				br.close();
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		
		// create the row schema
		csvRowSchema = new RowSchema();
		String columnNames[] = {"Name", "X", "Y", "Value"};
		int columnTypes[] = {ColumnSchema.STRING, ColumnSchema.NUMBER, ColumnSchema.NUMBER, ColumnSchema.NUMBER};
		for(int i=0; i<4; i++) {
			csvRowSchema.addSchema(columnNames[i], columnTypes[i]);
			
			// tell the position layout to setup the mapping between columns
			// we use i+1 because in sql the index starts at 1, not 0.
			posLayout.setColumnMapping(i+1, columnNames[i], csvRowSchema);
		}
		
		// must set the row schema first for the query record
		this.setRowSchema(csvRowSchema);
		
		// the row schema must know about the position layout
		csvRowSchema.setPositionLayout(posLayout);
		
		QueryRow srcRow;
		QueryRow destRow;
		
		String nameString, xString, yString, valString;
		try {
			BufferedReader br = new BufferedReader(new FileReader(csvFile));
			String line = br.readLine();
			boolean sourceRead = false;
			String[] tokens; 
			while (line != null) {
				//System.out.println("CSVQueryRecord processing line of size: " + line.length() + " data: " + line);
				if (!line.startsWith("#") && !line.startsWith("layout") &&
						!line.startsWith("adjustNodes") &&
						!line.startsWith("edgeRouting") && (line.length() > 0)) {
					if (!sourceRead) {

						// assume the 1st thing we see is the source
						sourceRead = true;
						
						// split the csv up
						tokens = line.split(",");
						nameString = tokens[0];
						xString = tokens[1];
						yString = tokens[2];
						srcRow = new QueryRow(csvRowSchema, this.getId());
						srcRow.setInfo(csvRowSchema.getDefaultNameId(), nameString);
						
						if (posLayout.doesSrcHavePosition()) {
							srcRow.setInfo(csvRowSchema.getDefaultX(), Double.valueOf(xString));
							srcRow.setInfo(csvRowSchema.getDefaultY(), Double.valueOf(yString));
						}
						srcRow.setInfo(csvRowSchema.defaultValue, 0);
						this.setSourceRow(srcRow);
						
					} else {
						// assume the next thing we see are destination nodes
						destRow = new QueryRow(csvRowSchema, this.getId());
						this.addFlowRow(destRow);
						tokens = line.split(",");
						nameString = tokens[0];
						xString = tokens[1];
						yString = tokens[2];
						valString = tokens[3];
						
						destRow.setInfo(csvRowSchema.getDefaultNameId(), nameString);
						destRow.setInfo(csvRowSchema.getDefaultX(), Double.valueOf(xString));
						destRow.setInfo(csvRowSchema.getDefaultY(), Double.valueOf(yString));
						destRow.setInfo(csvRowSchema.getDefaultValueId(), Double.valueOf(valString));
						
					}
				}
				
				line = br.readLine();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.rowsDone();
	}

	
	
}
