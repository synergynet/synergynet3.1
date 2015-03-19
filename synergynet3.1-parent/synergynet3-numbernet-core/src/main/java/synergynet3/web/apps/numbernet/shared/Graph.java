package synergynet3.web.apps.numbernet.shared;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph {	
	public List<Edge> edges = new ArrayList<Edge>();
	public Map<String,Node> nodes = new HashMap<String,Node>();

	public Graph() {
	}
	
	public List<Edge> getEdges() {
		return edges;
	}
	
	public Map<String, Node> getNodes() {
		return nodes;
	}

	public Node getNodeForItem(String item) {
		Node n = nodes.get(item);
		if(n == null) {
			n = new Node(item);
			nodes.put(item, n);
		}
		return n;
	}
	

	public void forceConnect(String a, String b) {
		Node na = getNodeForItem(a);
		Node nb = getNodeForItem(b);
		Edge e = new Edge(na, nb);
		edges.add(e);
	}

	public Edge connect(String a, String b) {
		if(!isConnected(a,b)) {
			Node na = getNodeForItem(a);
			Node nb = getNodeForItem(b);
			if(na.shouldIgnore() && nb.shouldIgnore()) {
				// if node shouldIgnore then user is currently 'holding' the node with their finger
				Edge e = new Edge(na, nb);
				edges.add(e);
				return e;
			}			
		}
		return null;
	}

	public boolean isConnected(String a, String b) {
		for(Edge e : edges) {
			if((e.a.getID().equals(a) && e.b.getID().equals(b)) || (e.a.getID().equals(b) && e.b.getID().equals(a))) {
				return true;
			}
		}
		return false;
	}

	public boolean removeEdges(List<Edge> collection) {
		return edges.removeAll(collection);		
	}
	
	public List<Edge> getEdgesLongerThanDistanceIgnoringCurrentlyInteractingEdges(float distance) {
		List<Edge> edgesLongerThanDistance = new ArrayList<Edge>();
		for(Edge e : edges) {
			if(e.getLength() >= distance && e.a.shouldIgnore() && e.b.shouldIgnore()) {			
				edgesLongerThanDistance.add(e);
			}
		}
		return edgesLongerThanDistance;
	}


	/**
	 * When a node is pressed on, we want to ignore it during our
	 * clever moving-about-type-algorithm.
	 * @param item
	 */
	public void pressedOn(String itemID) {		
		Node n = getNodeForItem(itemID);
		if(n != null) {
			n.setIgnore(true);
		}
	}

	public void releasedOn(String itemID) {
		Node n = getNodeForItem(itemID);
		if(n != null) {
			n.setIgnore(false);
		}
	}


}
