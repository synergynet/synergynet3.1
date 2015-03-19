package synergynet3.feedbacksystem.defaultfeedbacktypes;

import java.util.UUID;

import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;

import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.items.image.IImage;
import multiplicity3.input.MultiTouchEventAdapter;
import multiplicity3.input.events.MultiTouchCursorEvent;

import synergynet3.feedbacksystem.FeedbackItem;
import synergynet3.feedbacksystem.FeedbackViewer;

public class SmilieFeedback extends FeedbackItem {
	
	public static final String CACHABLE_TYPE = "CACHABLE_FEEDBACK_SMILIE";
	
	private static String SMILIE_BACKGROUND_IMAGE = "synergynet3/feedbacksystem/defaultfeedbacktypes/smilieFeedbackBG.png";
	private static String SMILE_IMAGE = "synergynet3/feedbacksystem/defaultfeedbacktypes/smilieFeedbackFront.png";
	private static int smileWidth= 105;
	private static int maxSmileHeight = 40;
	
	private IImage smilieBackGround; 
	private IImage smile;
	private IImage finalSmile;
	
	private boolean allSettingsMade = true;
	
	private float rotationToApply = 0;
	private float heightToApply = 3;
	private int yOffset = -35;
	
	private float rating = 0.5f;
	
	@Override
	public String getIcon(){
		return "synergynet3/feedbacksystem/defaultfeedbacktypes/smilieFeedback.png";
	}
	
	@Override
	protected void addSettings() throws ContentTypeNotBoundException{		
		
		smilieBackGround = getStage().getContentFactory().create(IImage.class, "play", UUID.randomUUID());
		smilieBackGround.setImage(SMILIE_BACKGROUND_IMAGE);		
		smilieBackGround.setSize(200, 200);	
		smilieBackGround.getZOrderManager().setAutoBringToTop(false);
		smilieBackGround.getZOrderManager().setBringToTopPropagatesUp(false);
		
		setter.addToFrame(smilieBackGround, 0, 0, 0);
		
		smile = getStage().getContentFactory().create(IImage.class, "play", UUID.randomUUID());
		smile.setImage(SMILE_IMAGE);		
		smile.setSize(smileWidth, maxSmileHeight);	
		setter.addToFrame(smile, 0, 0, yOffset);
				
		smilieBackGround.getMultiTouchDispatcher().addListener(getSmileAdapter());	
		smile.getMultiTouchDispatcher().addListener(getSmileAdapter());

		applyRating();
		
	}
	
	private MultiTouchEventAdapter getSmileAdapter(){
		MultiTouchEventAdapter smileAdapter = new MultiTouchEventAdapter() {
			
			private float previousTopToBottomRatio = 0.5f;
			private boolean newTouch = true;
			
			@Override
			public void cursorChanged(MultiTouchCursorEvent event) {
				
				Vector2f realTouchLocation = new Vector2f( 
						(event.getPosition().x * (getStage().getWorldLocation().x*2)) - getStage().getWorldLocation().x,
						(event.getPosition().y * (getStage().getWorldLocation().y*2)) - getStage().getWorldLocation().y);
				
				if (realTouchLocation.distance(setter.getRelativeLocation()) < smilieBackGround.getWidth()/2){
					
					Vector2f topVector = new Vector2f(0, 200);
					Vector2f bottomVector = new Vector2f(0, -200);
					
					topVector.rotateAroundOrigin(setter.getRelativeRotation(), false);
					bottomVector.rotateAroundOrigin(setter.getRelativeRotation(), false);
					
					topVector.set(setter.getRelativeLocation().x + topVector.x, 
							setter.getRelativeLocation().y + topVector.y);
					bottomVector.set(setter.getRelativeLocation().x + bottomVector.x, 
							setter.getRelativeLocation().y + bottomVector.y);					
					
					float distanceToTop = realTouchLocation.distance(topVector);
					float distanceToBottom = realTouchLocation.distance(bottomVector);					
					
					if (newTouch){
						newTouch = false;
						previousTopToBottomRatio = distanceToBottom/(distanceToTop + distanceToBottom);
					}else{												
						
						float currentTopToBottomRatio =  distanceToBottom/(distanceToTop + distanceToBottom);						
						float changeInRating = previousTopToBottomRatio - currentTopToBottomRatio;
						
						rating -= changeInRating;
						if (rating < 0){
							rating = 0;
						}else if (rating > 1){
							rating = 1;
						}
	
						previousTopToBottomRatio = currentTopToBottomRatio;
						
						applyRating();					
						
					}		
				}
				
			}
			
			@Override
			public void cursorPressed(MultiTouchCursorEvent event) {
				newTouch = true;
				setter.getZOrderManager().bringToTop(smile);
			}
		};

		return smileAdapter;
	}
	
	private void applyRating(){
			
		int height = maxSmileHeight;
		
		if (rating > 0.5){
			if (smile.getRelativeRotation() != 0){
				smile.setRelativeRotation(0);
			}
			height = (int)(((rating - 0.5f)/0.5f) * maxSmileHeight);			
			
		}else{
			if (smile.getRelativeRotation() != FastMath.DEG_TO_RAD*180){
				smile.setRelativeRotation( FastMath.DEG_TO_RAD*180);
			}
			height = (int)((((1-rating) - 0.5f)/0.5f) * maxSmileHeight);
		}
		
		if (height<3)height = 3;
		
		smile.setSize(smileWidth, height);	
		
		rotationToApply = smile.getRelativeRotation();
		heightToApply = smile.getHeight();


	}
	
	@Override
	protected void generateFeedbackView(final FeedbackViewer feedbackViewer, int frameNo) throws ContentTypeNotBoundException{
		
		IImage finalSmilieBackGround = getStage().getContentFactory().create(IImage.class, "play", UUID.randomUUID());
		finalSmilieBackGround.setImage(SMILIE_BACKGROUND_IMAGE);		
		finalSmilieBackGround.setSize(200, 200);		
		feedbackViewer.addToFeedbackFrame(finalSmilieBackGround, frameNo, 0, 0);	
		
		finalSmile = getStage().getContentFactory().create(IImage.class, "play", UUID.randomUUID());
		finalSmile.setImage(SMILE_IMAGE);		
		finalSmile.setSize(smileWidth, heightToApply);	
		finalSmile.setRelativeRotation(rotationToApply);
		feedbackViewer.addToFeedbackFrame(finalSmile, frameNo, 0, yOffset);		

		finalSmilieBackGround.getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter() {
			@Override
			public void cursorPressed(MultiTouchCursorEvent event) {
				feedbackViewer.getContainer().getZOrderManager().bringToTop(finalSmile);
			}			
		});				
		
	}

	/**
	 * @param rotationToApply the rotationToApply to set
	 */
	public void setRotationToApply(float rotationToApply) {
		this.rotationToApply = rotationToApply;
	}

	/**
	 * @return the rotationToApply
	 */
	public float getRotationToApply() {
		return rotationToApply;
	}
	
	/**
	 * @param heightToApply the heightToApply to set
	 */
	public void setHeightToApply(float heightToApply) {
		this.heightToApply = heightToApply;
	}

	/**
	 * @return the heightToApply
	 */
	public float getHeightToApply() {
		return heightToApply;
	}

	@Override
	public Object[] deconstruct(String studentIDin) {
		Object[] feedbackItem = new Object[5];
		feedbackItem[0] = CACHABLE_TYPE;
		feedbackItem[1] = studentID;
		feedbackItem[2] = rotationToApply;
		feedbackItem[3] = heightToApply;
		return feedbackItem;
	}
	
	public static SmilieFeedback reconstruct(Object[] feedbackItem){
		SmilieFeedback feedback = new SmilieFeedback();
		feedback.setStudentID((String)feedbackItem[1]);
		feedback.setRotationToApply((Float)feedbackItem[2]);	
		feedback.setHeightToApply((Float)feedbackItem[3]);	

		return feedback;
	}
	
	@Override
	protected boolean getAllSettingsMade() {
		return allSettingsMade;
	}

}
