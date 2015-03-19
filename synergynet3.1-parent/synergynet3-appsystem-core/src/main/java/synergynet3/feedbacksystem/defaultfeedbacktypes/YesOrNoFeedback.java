package synergynet3.feedbacksystem.defaultfeedbacktypes;

import java.util.UUID;

import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.items.mutablelabel.IMutableLabel;
import multiplicity3.input.MultiTouchEventAdapter;
import multiplicity3.input.events.MultiTouchCursorEvent;

import synergynet3.feedbacksystem.FeedbackItem;
import synergynet3.feedbacksystem.FeedbackViewer;
import synergynet3.fonts.FontColour;
import synergynet3.fonts.FontUtil;

public class YesOrNoFeedback extends FeedbackItem {
	
	public static final String CACHABLE_TYPE = "CACHABLE_FEEDBACK_YESORNO";	
	
	private IMutableLabel yesLabel;
	private IMutableLabel noLabel;
	
	private boolean yesSelected = false;
	
	private boolean allSettingsMade = false;
	
	@Override
	public String getIcon(){
		return "synergynet3/feedbacksystem/defaultfeedbacktypes/yesOrNoFeedback.png";
	}
	
	@Override
	protected void addSettings() throws ContentTypeNotBoundException{
		
		yesLabel = getStage().getContentFactory().create(IMutableLabel.class, "yesLabel", UUID.randomUUID());
		yesLabel.setFont(FontUtil.getFont(FontColour.White));
		yesLabel.setText("YES");
		yesLabel.setFontScale(3f);	
		setter.addToFrame(yesLabel, 0, -(setter.getWidth()/4 + 30), 0);
		yesLabel.getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter() {
			@Override
			public void cursorClicked(MultiTouchCursorEvent event) {					
				if (!allSettingsMade)allSettingsMade = true;;
				yesSelected = true;
				noLabel.setFont(FontUtil.getFont(FontColour.White));
				yesLabel.setFont(FontUtil.getFont(FontColour.Green));				
			}
		});
		
		noLabel = getStage().getContentFactory().create(IMutableLabel.class, "noLabel", UUID.randomUUID());
		noLabel.setFont(FontUtil.getFont(FontColour.White));
		noLabel.setText("NO");
		noLabel.setFontScale(3f);				
		setter.addToFrame(noLabel, 0, setter.getWidth()/4, 0);
		noLabel.getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter() {
			@Override
			public void cursorClicked(MultiTouchCursorEvent event) {					
				if (!allSettingsMade)allSettingsMade = true;;
				yesSelected = false;
				noLabel.setFont(FontUtil.getFont(FontColour.Green));
				yesLabel.setFont(FontUtil.getFont(FontColour.White));					
			}
		});				
	}
	
	@Override
	protected void generateFeedbackView(FeedbackViewer feedbackViewer, int frameNo) throws ContentTypeNotBoundException{
		
		IMutableLabel finalLabel = getStage().getContentFactory().create(IMutableLabel.class, "finalLabel", UUID.randomUUID());
		finalLabel.setFont(FontUtil.getFont(FontColour.White));
		if (yesSelected){			
			finalLabel.setText("YES");
		}else{
			finalLabel.setText("NO");
		}		
		finalLabel.setFontScale(4f);		
		feedbackViewer.addToFeedbackFrame(finalLabel, frameNo, -100, 0);
		
	}
	
	/**
	 * @param yesSelected the yesSelected to set
	 */
	public void setYesSelected(boolean yesSelected) {
		this.yesSelected = yesSelected;
	}

	@Override
	public Object[] deconstruct(String studentIDin) {
		Object[] feedbackItem = new Object[3];
		feedbackItem[0] = CACHABLE_TYPE;
		feedbackItem[1] = studentID;
		feedbackItem[2] = yesSelected;
		return feedbackItem;
	}
	
	public static YesOrNoFeedback reconstruct(Object[] feedbackItem){
		YesOrNoFeedback feedback = new YesOrNoFeedback();
		feedback.setStudentID((String)feedbackItem[1]);
		feedback.setYesSelected((Boolean)feedbackItem[2]);	
		return feedback;
	}
	
	@Override
	protected boolean getAllSettingsMade() {
		return allSettingsMade;
	}

}
