package synergynet3.web.apps.numbernet.shared;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Class Graph.
 */
public class Graph
{

	/** The edges. */
	public List<Edge> edges = new ArrayList<Edge>();

	/** The nodes. */
	public Map<String, Node> nodes = new HashMap<String, Node>();

	/**
	 * Instantiates a new graph.
	 */
	public Graph()
	{
	}

	/**
	 * Connect.
	 *
	 * @param a
	 *            the a
	 * @param b
	 *            the b
	 * @return the edge
	 */
	public Edge connect(String a, String b)
	{
		if (!isConnected(a, b))
		{
			Node na = getNodeForItem(a);
			Node nb = getNodeForItem(b);
			if (na.shouldIgnore() && nb.shouldIgnore())
			{
				// if node shouldIgnore then user is currently 'holding' the
				// node with their finger
				Edge e = new Edge(na, nb);
				edges.add(e);
				return e;
			}
		}
		return null;
	}

	/**
	 * Force connect.
	 *
	 * @param a
	 *            the a
	 * @param b
	 *            the b
	 */
	public void forceConnect(String a, String b)
	{
		Node na = getNodeForItem(a);
		Node nb = getNodeForItem(b);
		Edge e = new Edge(na, nb);
		edges.add(e);
	}

	/**
	 * Gets the edges.
	 *
	 * @return the edges
	 */
	public List<Edge> getEdges()
	{
		return edges;
	}

	/**
	 * Gets the edges longer than distance ignoring currently interacting edges.
	 *
	 * @param distance
	 *            the distance
	 * @return the edges longer than distance ignoring currently interacting
	 *         edges
	 */
	public List<Edge> getEdgesLongerThanDistanceIgnoringCurrentlyInteractingEdges(float distance)
	{
		List<Edge> edgesLongerThanDistance = new ArrayList<Edge>();
		for (Edge e : edges)
		{
			if ((e.getLength() >= distance) && e.a.shouldIgnore() && e.b.shouldIgnore())
			{
				edgesLongerThanDistance.add(e);
			}
		}
		return edgesLongerThanDistance;
	}

	/**
	 * Gets the node for item.
	 *
	 * @param item
	 *            the item
	 * @return the node for item
	 */
	public Node getNodeForItem(String item)
	{
		Node n = nodes.get(item);
		if (n == null)
		{
			n = new Node(item);
			nodes.put(item, n);
		}
		return n;
	}

	/**
	 * Gets the nodes.
	 *
	 * @return the nodes
	 */
	public Map<String, Node> getNodes()
	{
		return nodes;
	}

	/**
	 * Checks if is connected.
	 *
	 * @param a
	 *            the a
	 * @param b
	 *            the b
	 * @return true, if is connected
	 */
	public boolean isConnected(String a, String b)
	{
		for (Edge e : edges)
		{
			if ((e.a.getID().equals(a) && e.b.getID().equals(b)) || (e.a.getID().equals(b) && e.b.getID().equals(a)))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * When a node is pressed on, we want to ignore it during our clever
	 * moving-about-type-algorithm.
	 *
	 * @param item
	 */
	public void pressedOn(String itemID)
	{
		Node n = getNodeForItem(itemID);
		if (n != null)
		{
			n.setIgnore(true);
		}
	}

	/**
	 * Released on.
	 *
	 * @param itemID
	 *            the item id
	 */
	public void releasedOn(String itemID)
	{
		Node n = getNodeForItem(itemID);
		if (n != null)
		{
			n.setIgnore(false);
		}
	}

	/**
	 * Removes the edges.
	 *
	 * @param collection
	 *            the collection
	 * @return true, if successful
	 */
	public boolean removeEdges(List<Edge> collection)
	{
		return edges.removeAll(collection);
	}

}
