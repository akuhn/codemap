package flowmap.swt.main;

import edu.stanford.hci.flowmap.db.ColumnSchema;
import edu.stanford.hci.flowmap.db.QueryRecord;
import edu.stanford.hci.flowmap.db.QueryRow;
import edu.stanford.hci.flowmap.db.RowSchema;
import edu.stanford.hci.flowmap.layout.DirectPositionLayout;

@SuppressWarnings("serial")
public class FakeQueryRecord extends QueryRecord {
    
    public FakeQueryRecord() {
        DirectPositionLayout posLayout = new DirectPositionLayout(Globals.getScreenDimension());
        Globals.runNodeEdgeRouting = false;
        Globals.useLayoutAdjustment = false;        
        
        RowSchema queryRowScheme = new RowSchema();
        String columnNames[] = {"Name", "X", "Y", "Value"};
        int columnTypes[] = {ColumnSchema.STRING, ColumnSchema.NUMBER, ColumnSchema.NUMBER, ColumnSchema.NUMBER};
        for(int i=0; i<4; i++) {
            queryRowScheme.addSchema(columnNames[i], columnTypes[i]);
            
            // tell the position layout to setup the mapping between columns
            // we use i+1 because in sql the index starts at 1, not 0.
            posLayout.setColumnMapping(i+1, columnNames[i], queryRowScheme);
        }        
        
        setRowSchema(queryRowScheme);
        queryRowScheme.setPositionLayout(posLayout);
        
        QueryRow srcRow = new QueryRow(queryRowScheme, this.getId());
        srcRow.setInfo(queryRowScheme.getDefaultNameId(), "root");
        
        srcRow.setInfo(queryRowScheme.getDefaultX(), 200.0);
        srcRow.setInfo(queryRowScheme.getDefaultY(), 200.0);
        srcRow.setInfo(queryRowScheme.getDefaultValueId(), 0);
        this.setSourceRow(srcRow);        
        


        this.rowsDone();
    }
}
