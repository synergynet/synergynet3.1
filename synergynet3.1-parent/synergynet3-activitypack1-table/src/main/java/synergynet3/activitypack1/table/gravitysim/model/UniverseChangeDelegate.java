package synergynet3.activitypack1.table.gravitysim.model;

public interface UniverseChangeDelegate {
	public void bodyRemoved(Body b);
	public void bodyAdded(Body b);
	public void bodyPositionChanged(Body body);
}
