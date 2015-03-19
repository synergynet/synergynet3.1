package multiplicity3.csys.animation.elements;

public abstract class SimpleActionAnimationElement extends AnimationElement {

	private boolean finished = false;
	
	@Override
	public void reset() {}

	@Override
	public void elementStart(float tpf) {
		doSimpleAction();
		finished = true;
	}

	@Override
	public void updateAnimationState(float tpf) {}

	@Override
	public boolean isFinished() {
		return finished;
	}
	
	public abstract void doSimpleAction();

}
