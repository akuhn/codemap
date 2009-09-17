package edu.stanford.hci.flowmap.prefuse.structure;

import java.util.Iterator;

import edu.berkeley.guir.prefuse.graph.DefaultGraph;

/**
 * The logical structre of the flowmap
 *
 * This software is distributed under the Berkeley Software Distribution License.
 * Please see http://graphics.stanford.edu/~dphan/code/bsd.license.html
 *
 */
public class FlowMapStructure extends DefaultGraph {

	protected FlowNode m_rootNode;

	public FlowMapStructure(FlowNode rootNode){
		super(false);
		m_rootNode = rootNode;
		this.addNode(rootNode);
	}
	
	public FlowMapStructure(){
		super(false);
		m_rootNode = null;
	}
	
	
	
	public FlowNode getRootNode(){
		for(Iterator nodeIter = this.getNodes(); nodeIter.hasNext();){
			FlowNode tNode = (FlowNode)nodeIter.next();
			if(tNode.isRootNode()){
				return tNode;
			}
		}
		return null;
	}
	
	public void reinit(boolean directed) {
		super.reinit(directed);
		m_rootNode = null;
		
	}
}
