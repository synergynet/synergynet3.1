package synergynet3.feedbacksystem.defaultfeedbacktypes;

import java.util.UUID;

import com.jme3.math.ColorRGBA;

import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.items.shapes.IColourRectangle;
import multiplicity3.input.MultiTouchEventAdapter;
import multiplicity3.input.events.MultiTouchCursorEvent;

import synergynet3.feedbacksystem.FeedbackItem;
import synergynet3.feedbacksystem.FeedbackViewer;

public class SimpleTrafficLightFeedback extends FeedbackItem {
	
	public static final String CACHABLE_TYPE = "CACHABLE_FEEDBACK_SIMPLETRAFFICLIGHT";
	
	private boolean allSettingsMade = true;
	
	private int onTop = 0;
	private IColourRectangle redRect;
	private IColourRectangle yellowRect;
	private IColourRectangle greenRect;
	
	@Override
	public String getIcon(){
		return "synergynet3/feedbacksystem/defaultfeedbacktypes/simpleTrafficLightFeedback.png";
	}
	
	@Override
	protected void addSettings() throws ContentTypeNotBoundException{
		
		redRect = getStage().getContentFactory().create(IColourRectangle.class, "trafficLight", UUID.randomUUID());
		redRect.setSolidBackgroundColour(ColorRGBA.Red);
		redRect.setSize(300, 100);		
		setter.addToFrame(redRect, 0, 0, 0);
		redRect.setVisible(false);
		redRect.setInteractionEnabled(false);
		
		yellowRect = getStage().getContentFactory().create(IColourRectangle.class, "trafficLight", UUID.randomUUID());
		yellowRect.setSolidBackgroundColour(ColorRGBA.Yellow);
		yellowRect.setSize(300, 100);		
		setter.addToFrame(yellowRect, 0, 0, 0);
		
		greenRect = getStage().getContentFactory().create(IColourRectangle.class, "trafficLight", UUID.randomUUID());
		greenRect.setSolidBackgroundColour(ColorRGBA.Green);
		greenRect.setSize(300, 100);		
		setter.addToFrame(greenRect, 0, 0, 0);			
		greenRect.setVisible(false);
		greenRect.setInteractionEnabled(false);
		
		redRect.getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter() {
			@Override
			public void cursorClicked(MultiTouchCursorEvent event) {					
				redRect.setVisible(false);
				redRect.setInteractionEnabled(false);
				yellowRect.setVisible(true);
				yellowRect.setInteractionEnabled(true);
				onTop = 0;
			}
		});		
		
		yellowRect.getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter() {
			@Override
			public void cursorClicked(MultiTouchCursorEvent event) {					
				yellowRect.setVisible(false);
				yellowRect.setInteractionEnabled(false);
				greenRect.setVisible(true);
				greenRect.setInteractionEnabled(true);
				onTop = 1;
			}
		});
		
		greenRect.getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter() {
			@Override
			public void cursorClicked(MultiTouchCursorEvent event) {					
				greenRect.setVisible(false);
				greenRect.setInteractionEnabled(false);
				redRect.setVisible(true);
				redRect.setInteractionEnabled(true);
				onTop = 2;
			}
		});		
		
	}
	
	@Override
	protected void generateFeedbackView(FeedbackViewer feedbackViewer, int frameNo) throws ContentTypeNotBoundException{
		
		IColourRectangle finalRect = getStage().getContentFactory().create(IColourRectangle.class, "trafficLight", UUID.randomUUID());
		finalRect.setSize(300, 100);		
		
		if (onTop == 1){
			finalRect.setSolidBackgroundColour(ColorRGBA.Green);
		}else if (onTop ==2){
			finalRect.setSolidBackgroundColour(ColorRGBA.Red);
		}else{
			finalRect.setSolidBackgroundColour(ColorRGBA.Yellow);			
		}
		feedbackViewer.addToFeedbackFrame(finalRect, frameNo, 0, 0);

	}

	/**
	 * @param onTop the onTop to set
	 */
	public void setOnTop(int onTop) {
		this.onTop = onTop;
	}

	/**
	 * @return the onTop
	 */
	public int getOnTop() {
		return onTop;
	}
	
	@Override
	protected boolean getAllSettingsMade() {
		return allSettingsMade;
	}

	@Override
	public Object[] deconstruct(String studentIDin) {
		Object[] feedbackItem = new Object[3];
		feedbackItem[0] = CACHABLE_TYPE;
		feedbackItem[1] = studentID;
		feedbackItem[2] = onTop;
		return feedbackItem;
	}
	
	public static SimpleTrafficLightFeedback reconstruct(Object[] feedbackItem){
		SimpleTrafficLightFeedback feedback = new SimpleTrafficLightFeedback();
		feedback.setStudentID((String)feedbackItem[1]);
		feedback.setOnTop((Integer)feedbackItem[2]);	
		return feedback;
	}

}
