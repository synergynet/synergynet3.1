package synergynet3.apps.numbernet.graphing.layout.spring;

import multiplicity3.csys.IUpdateable;

/**
 * The Class SpringLayout.
 */
public class SpringLayout implements IUpdateable {

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.IUpdateable#update(float)
	 */
	@Override
	public void update(float timePerFrameSeconds) {
		// TODO Auto-generated method stub

	}

	// private float desiredLineLength;
	// private float maxNodeVelocity = 10f;
	// private List<Node> updateList = new ArrayList<Node>();
	// private Graph g;
	// private boolean active = false;
	// private DisplayManager displayManager;
	//
	// public SpringLayout(IStage stage, Graph g, float desiredLineLength) {
	// this.g = g;
	// this.desiredLineLength = desiredLineLength;
	//
	// this.displayManager = stage.getContentSystem().getDisplayManager();
	// }
	//
	// public float getMaxNodeVelocity() {
	// return maxNodeVelocity;
	// }
	//
	// public void setMaxNodeVelocity(float maxNodeVelocity) {
	// this.maxNodeVelocity = maxNodeVelocity;
	// }
	//
	// @Override
	// public void update(float timePerFrameSeconds) {
	// if(!active) return;
	// //if(g.getEdges().size() < 1) return;
	//
	//
	// // calculate force acting between pairs of nodes connected by an edge
	// for(Edge e : g.getEdges()) {
	// // rather than update the lines at the end of this update() method in an
	// iteration
	// // through the Edge collection - doing the update now to save that pass
	// through the
	// // edges. The change in location of the lines will probably be minimal
	// anyway.
	//
	// // e.line.updateEndPoints();
	//
	// Vector2f v =
	// e.b.item.getRelativeLocation().subtract(e.a.item.getRelativeLocation());
	//
	// float len = v.length(); if(len == 0) len = 0.0001f;
	//
	// float forceBetweenNodes = (len - desiredLineLength) / (len * 3f);
	// v.multLocal(forceBetweenNodes * timePerFrameSeconds); // take into
	// account time between successive frames
	//
	// // if we are 'holding' a node, ignore it.
	// if(!e.a.shouldIgnore()) {
	// e.a.v.addLocal(v);
	// if(e.a.v.length() > maxNodeVelocity) {
	// // cap velocity
	// e.a.v.normalizeLocal().multLocal(maxNodeVelocity);
	// }
	// }
	//
	// // if we are 'holding' a node, ignore it
	// if(!e.b.shouldIgnore()) {
	// e.b.v.subtractLocal(v);
	// if(e.b.v.length() > maxNodeVelocity) {
	// // cap velocity
	// e.b.v.normalizeLocal().multLocal(maxNodeVelocity);
	// }
	// }
	// }
	//
	//
	// // have some 'electrostatic' force that repels each node from each other
	// // for(Node i : g.getNodes().values()) {
	// // Vector2f delta = new Vector2f();
	// // for(Node j : g.getNodes().values()) {
	// // // sum up all the forces between nodes...
	// // if(i == j) continue;
	// //
	// // Vector2f v = i.v.subtract(j.v);
	// //
	// // float len = v.lengthSquared();
	// //
	// // if (len == 0) {
	// // delta.x += (float)Math.random();
	// // delta.y += (float)Math.random();
	// // } else if (len < desiredLineLength * desiredLineLength) {
	// // delta.addLocal(v).divideLocal(len);
	// // }
	// //
	// // }
	// //
	// // // apply the force...
	// // float dlen = delta.lengthSquared();
	// //
	// // if (dlen > 0) {
	// // dlen = (float)Math.sqrt(dlen) / 2f;
	// // delta.multLocal(dlen);
	// // i.v.addLocal(delta);
	// // }
	// // }
	//
	// // now update each node with its position
	//
	// updateList.clear();
	// for(Node n : g.getNodes().values()) {
	// if(!n.shouldIgnore()) {
	// // as setRelativeLocation will trigger another method in Graph that
	// inspects nodes.values, need to do this hack :-(
	// updateList.add(n);
	// }
	// }
	//
	// // for the nodes we need to update, update them.
	// for(Node n : updateList) {
	// n.item.setRelativeLocation(n.item.getRelativeLocation().add(n.v.mult(timePerFrameSeconds)));
	// constrainToWindow(n);
	// }
	//
	// }
	//
	//
	// // temp store for window position - saves memory burn
	// Vector2f itemWindowPos = new Vector2f();
	//
	//
	// /**
	// * Ensure a node doesn't leave the bounds of the window
	// * @param n
	// */
	// private void constrainToWindow(Node n) {
	// IItem item = n.item;
	// itemWindowPos = item.getWorldLocation();
	// if(itemWindowPos.x < 0) {
	// itemWindowPos.x = 0;
	// n.v.set(0f, n.v.y);
	// }else if(itemWindowPos.x > displayManager.getDisplayWidth()) {
	// itemWindowPos.x = displayManager.getDisplayWidth();
	// n.v.set(0f, n.v.y);
	// }
	//
	// if(itemWindowPos.y < 0) {
	// itemWindowPos.y = 0;
	// n.v.set(n.v.x, 0f);
	// }else if(itemWindowPos.y > displayManager.getDisplayHeight()) {
	// itemWindowPos.y = displayManager.getDisplayHeight();
	// n.v.set(n.v.x, 0f);
	// }
	//
	// item.setWorldLocation(itemWindowPos);
	// }
	//
	// public void setActive(boolean active) {
	// this.active = active;
	// }
}
