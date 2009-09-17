package edu.stanford.hci.flowmap.main;

import java.awt.FlowLayout;
import java.io.File;
import java.util.Iterator;

import javax.swing.JPanel;

import edu.stanford.hci.flowmap.db.CSVQueryRecord;
import edu.stanford.hci.flowmap.db.QueryRecord;
import edu.stanford.hci.flowmap.db.QueryRow;
import edu.stanford.hci.flowmap.structure.Graph;
import edu.stanford.hci.flowmap.structure.Node;
import edu.stanford.hci.flowmap.utils.WindowUtilities;

/**
 * This version of the code reads from a CSV file the source and destination data 
 * for a flow map.
 * 
 * This software is distributed under the Berkeley Software Distribution License.
 * Please see http://graphics.stanford.edu/~dphan/code/bsd.license.html
 * 
 * All this code in edu.stanford.hci.flowmap was written by one of 
 * the following authors:
 * 
 * @author <a href="http://graphics.stanford.edu/~dphan">Doantam Phan</a>
 * @author <a href="http://graphics.stanford.edu/~lingxiao">Ling Xiao</a>
 * @author <a href="http://graphics.stanford.edu/~ronyeh">Ron Yeh</a>
 *
 */
@SuppressWarnings("serial")
public class CSVMain extends JPanel
{
	private MapDisplayPanel display;
	
	private Options otions;
	private CSVQueryRecord queryRecord;
	
	/**
	 * Constructs this panel, which we can use to interactively query the 
	 * database and create a flow map.
	 * @param file
	 */
	public CSVMain(String file) {
		super();

		queryRecord = new CSVQueryRecord(new File("direct.csv"));
	
		otions = new Options();
		otions.putDouble(Options.MIN_DISPLAY_WIDTH, 1);
		otions.putDouble(Options.MAX_DISPLAY_WIDTH, 10);
		// make sure to have only one scale boolean set to true
		otions.putBoolean(Options.LINEAR_SCALE, true);
		
		display = new MapDisplayPanel(Globals.getScreenDimension());
		this.setLayout(new FlowLayout());
		this.add(display);		
	}
	
	public static void main(String[] args) {
		WindowUtilities.setNativeLookAndFeel();
		CSVMain mapMain=null;
		if (args.length == 0) {
			mapMain = new CSVMain(null);
		} else if (args.length == 1){
			mapMain = new CSVMain(args[0]);
		} else {
			System.out.println("Usage: FlowMapLayout <optional csvfile>");
			System.exit(0);
			
		}
		WindowUtilities.openInJFrame(mapMain, 1024, 768, null, "FlowMapLayout 0.1 alpha");
		mapMain.drawDisplay();
	}
	
	private Graph createNodes(QueryRecord flowRecord) {
		
		otions.putString(Options.CURRENT_FLOW_TYPE, flowRecord.getSourceRow().getRowSchema().getDefaultValueId());
		
		Graph originalGraph = new Graph();
		assert(flowRecord.getSourceRow() != null);
		Node rootNode = new Node(flowRecord.getSourceRow());
		rootNode.setRootNode(true);
		
		originalGraph.addNode(rootNode);
		originalGraph.setRootNode(rootNode);
			
		QueryRow row;
		Node dest;
		Iterator i = flowRecord.getRowsIterator();
		
		
		while (i.hasNext()) {
			row = (QueryRow)i.next();
			
			// if the root node is a destination, just ignore it
			if (row.getName().equals(rootNode.getName())) {
				continue;
			}
			
			dest = new Node(row);
			originalGraph.addNode(dest);
		}
		
		return originalGraph;
		
	}
	
	public void drawDisplay() {
		if (queryRecord != null) {
			Graph graph = createNodes(queryRecord);			
			display.updateDisplay(graph, otions, queryRecord);
		}
	}
}
