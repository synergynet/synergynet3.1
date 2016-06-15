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

/**
 * The Class YesOrNoFeedback.
 */
public class YesOrNoFeedback extends FeedbackItem
{

	/** The Constant CACHABLE_TYPE. */
	public static final String CACHABLE_TYPE = "CACHABLE_FEEDBACK_YESORNO";

	/** The all settings made. */
	private boolean allSettingsMade = false;

	/** The no label. */
	private IMutableLabel noLabel;

	/** The yes label. */
	private IMutableLabel yesLabel;

	/** The yes selected. */
	private boolean yesSelected = false;

	/**
	 * Reconstruct.
	 *
	 * @param feedbackItem
	 *            the feedback item
	 * @return the yes or no feedback
	 */
	public static YesOrNoFeedback reconstruct(Object[] feedbackItem)
	{
		YesOrNoFeedback feedback = new YesOrNoFeedback();
		feedback.setStudentID((String) feedbackItem[1]);
		feedback.setYesSelected((Boolean) feedbackItem[2]);
		return feedback;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.cachecontrol.IFeedbackItemCachable#deconstruct(java.lang.
	 * String)
	 */
	@Override
	public Object[] deconstruct(String studentIDin)
	{
		Object[] feedbackItem = new Object[3];
		feedbackItem[0] = CACHABLE_TYPE;
		feedbackItem[1] = studentID;
		feedbackItem[2] = yesSelected;
		return feedbackItem;
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.feedbacksystem.FeedbackItem#getIcon()
	 */
	@Override
	public String getIcon()
	{
		return "synergynet3/feedbacksystem/defaultfeedbacktypes/yesOrNoFeedback.png";
	}

	/**
	 * @param yesSelected
	 *            the yesSelected to set
	 */
	public void setYesSelected(boolean yesSelected)
	{
		this.yesSelected = yesSelected;
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.feedbacksystem.FeedbackItem#addSettings()
	 */
	@Override
	protected void addSettings() throws ContentTypeNotBoundException
	{

		yesLabel = getStage().getContentFactory().create(IMutableLabel.class, "yesLabel", UUID.randomUUID());
		yesLabel.setFont(FontUtil.getFont(FontColour.White));
		yesLabel.setText("YES");
		yesLabel.setFontScale(3f);
		setter.addToFrame(yesLabel, 0, -((setter.getWidth() / 4) + 30), 0);
		yesLabel.getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter()
		{
			@Override
			public void cursorClicked(MultiTouchCursorEvent event)
			{
				if (!allSettingsMade)
				{
					allSettingsMade = true;
				}
				;
				yesSelected = true;
				noLabel.setFont(FontUtil.getFont(FontColour.White));
				yesLabel.setFont(FontUtil.getFont(FontColour.Green));
			}
		});

		noLabel = getStage().getContentFactory().create(IMutableLabel.class, "noLabel", UUID.randomUUID());
		noLabel.setFont(FontUtil.getFont(FontColour.White));
		noLabel.setText("NO");
		noLabel.setFontScale(3f);
		setter.addToFrame(noLabel, 0, setter.getWidth() / 4, 0);
		noLabel.getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter()
		{
			@Override
			public void cursorClicked(MultiTouchCursorEvent event)
			{
				if (!allSettingsMade)
				{
					allSettingsMade = true;
				}
				;
				yesSelected = false;
				noLabel.setFont(FontUtil.getFont(FontColour.Green));
				yesLabel.setFont(FontUtil.getFont(FontColour.White));
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.feedbacksystem.FeedbackItem#generateFeedbackView(synergynet3
	 * .feedbacksystem.FeedbackViewer, int)
	 */
	@Override
	protected void generateFeedbackView(FeedbackViewer feedbackViewer, int frameNo) throws ContentTypeNotBoundException
	{

		IMutableLabel finalLabel = getStage().getContentFactory().create(IMutableLabel.class, "finalLabel", UUID.randomUUID());
		finalLabel.setFont(FontUtil.getFont(FontColour.White));
		if (yesSelected)
		{
			finalLabel.setText("YES");
		}
		else
		{
			finalLabel.setText("NO");
		}
		finalLabel.setFontScale(4f);
		feedbackViewer.addToFeedbackFrame(finalLabel, frameNo, -100, 0);

	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.feedbacksystem.FeedbackItem#getAllSettingsMade()
	 */
	@Override
	protected boolean getAllSettingsMade()
	{
		return allSettingsMade;
	}

}
