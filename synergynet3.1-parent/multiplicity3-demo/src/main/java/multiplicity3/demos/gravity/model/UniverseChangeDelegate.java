package multiplicity3.demos.gravity.model;

public interface UniverseChangeDelegate {
	public void bodyRemoved(Body b);
	public void bodyAdded(Body b);
	public void bodyPositionChanged(Body body);
}
