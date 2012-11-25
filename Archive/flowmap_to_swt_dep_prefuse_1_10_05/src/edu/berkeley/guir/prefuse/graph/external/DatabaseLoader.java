package edu.berkeley.guir.prefuse.graph.external;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import edu.berkeley.guir.prefuse.ItemRegistry;

/**
 * Abstract class containing functionality for loading graph elements from
 * a SQL database.
 *
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public abstract class DatabaseLoader extends GraphLoader {

    private final String m_columns[];
    
    protected String m_neighborQuery;
    protected String m_childrenQuery;
    protected String m_parentQuery;
    
    private Connection m_db;
    private PreparedStatement m_ns, m_cs, m_ps;
    
    public DatabaseLoader(ItemRegistry registry, String columns[])  {
        super(registry, columns[0]);
        m_columns = columns;
    } //
    
    public String[] getColumns() {
        return m_columns;
    } //
    
    public void connect(String driver, String url, String user, String password)
        throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException
    {
            Class.forName(driver).newInstance();
            m_db = DriverManager.getConnection(url, user, password);
            if ( m_neighborQuery != null )
                m_ns = prepare(m_neighborQuery);
            if ( m_childrenQuery != null )
                m_cs = prepare(m_childrenQuery);
            if ( m_parentQuery != null )
                m_ps = prepare(m_parentQuery);
    } //
    
    public Connection getConnection() {
        return m_db;
    } //
    
    private PreparedStatement prepare(String query) throws SQLException {
        if ( query == null )
            throw new IllegalArgumentException("Input query must be non-null");
        if ( m_db == null )
            throw new IllegalStateException("Connection to database not yet"
                + " established! Make sure connect() is called first.");
           
        return m_db.prepareStatement(query);
    }
    
    public void setNeighborQuery(String query) throws SQLException {
        if ( m_db != null )
            m_ns = prepare(query);
        m_neighborQuery = query;
    } //
    
    public String getNeighborQuery() {
        return m_neighborQuery;
    } //
    
    public void setChildrenQuery(String query) throws SQLException {
        if ( m_db != null )
            m_cs = prepare(query);
        m_childrenQuery = query;
    } //
    
    public String getChildrenQuery() {
        return m_childrenQuery;
    } //
    
    public void setParentQuery(String query) throws SQLException {
        if ( m_db != null )
            m_ps = prepare(query);
        m_parentQuery = query;
    } //
    
    public String getParentQuery() {
        return m_parentQuery;
    } //
    
    protected abstract void prepareNeighborQuery(PreparedStatement s, ExternalNode n);
    protected abstract void prepareChildrenQuery(PreparedStatement s, ExternalTreeNode n);
    protected abstract void prepareParentQuery(PreparedStatement s, ExternalTreeNode n);     
    
    /**
     * @see edu.berkeley.guir.prefuse.graph.external.GraphLoader#getNeighbors(edu.berkeley.guir.prefuse.graph.external.ExternalNode)
     */
    protected void getNeighbors(ExternalNode n) {
        prepareNeighborQuery(m_ns, n);
        loadNodes(LOAD_NEIGHBORS, m_ns, n);
    } //

    /**
     * @see edu.berkeley.guir.prefuse.graph.external.GraphLoader#getChildren(edu.berkeley.guir.prefuse.graph.external.ExternalTreeNode)
     */
    protected void getChildren(ExternalTreeNode n) {
        prepareChildrenQuery(m_cs, n);
        loadNodes(LOAD_CHILDREN, m_cs, n);
    } //
    
    /**
     * @see edu.berkeley.guir.prefuse.graph.external.GraphLoader#getParent(edu.berkeley.guir.prefuse.graph.external.ExternalTreeNode)
     */
    protected void getParent(ExternalTreeNode n) {
        prepareParentQuery(m_ps, n);
        loadNodes(LOAD_PARENT, m_ps, n);
    } //
    
    private void loadNodes(int type, PreparedStatement s, ExternalEntity src) {
        try {
            ResultSet rs = s.executeQuery();
            while ( rs.next() )
                loadNode(type, rs, src);
        } catch ( SQLException e ) {
            e.printStackTrace();
        }
    } //

    public ExternalEntity loadNode(int type, ResultSet rs, ExternalEntity src) throws SQLException {
        ExternalEntity node = (type==LOAD_NEIGHBORS ? 
            (ExternalEntity) new ExternalNode() : new ExternalTreeNode());
        for ( int i=0; i<m_columns.length; i++ ) {
            String value = rs.getString(m_columns[i]);
            if ( value != null )
                value = value.replaceAll("\r","");
            node.setAttribute(m_columns[i], value);
        }
        super.foundNode(type, src, node, null);
        return node;
    } //
    
} // end of class DatabaseQuery
