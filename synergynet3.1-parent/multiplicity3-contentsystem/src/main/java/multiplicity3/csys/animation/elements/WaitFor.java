package multiplicity3.csys.animation.elements;

public class WaitFor extends AnimationElement {
	
	private float seconds;
	private float elapsed;
	private boolean finished;

	public WaitFor(float seconds) {
		this.seconds = seconds;
		this.elapsed = 0f;
		finished = false;
	}

	@Override
	public void reset() {
		elapsed = 0f;
		finished = false;		
	}

	@Override
	public void elementStart(float tpf) {
		
	}

	@Override
	public void updateAnimationState(float tpf) {
		elapsed += tpf;
		if(elapsed > seconds) {
			finished = true;
		}		
	}

	@Override
	public boolean isFinished() {
		return finished;
	}

}
