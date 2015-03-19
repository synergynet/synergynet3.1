package synergynet3.additionalUtils;

import multiplicity3.input.events.MultiTouchCursorEvent;

public class IgnoreDoubleClick {
	
	private int timeOut = 1000;
	boolean clicked = false;
	
	public IgnoreDoubleClick(){}
	
	public IgnoreDoubleClick(int timeOut){
		this.timeOut = timeOut;
	}

	public void click(MultiTouchCursorEvent event){
		if (!clicked){
			onAction(event);
			clicked = true;
			Thread timeoutThread = new Thread(new Runnable() {	  
				public void run() {
					try {
						Thread.sleep(timeOut);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					clicked = false;
				}
			});
			timeoutThread.start();
		}
	}
	
	public void onAction(MultiTouchCursorEvent event){}
	
}
