package synergynet3.additionalitems.jme;

import java.util.UUID;

import multiplicity3.csys.annotations.ImplementsContentItem;
import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.items.border.IRoundedBorder;
import multiplicity3.csys.items.image.IImage;
import multiplicity3.csys.items.mutablelabel.IMutableLabel;
import multiplicity3.csys.items.shapes.IColourRectangle;
import multiplicity3.csys.stage.IStage;
import multiplicity3.jme3csys.items.IInitable;
import multiplicity3.jme3csys.items.container.JMEContainer;
import synergynet3.additionalitems.interfaces.IToggleButtonbox;
import synergynet3.fonts.FontColour;
import synergynet3.fonts.FontUtil;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;

/**
 * The Class ToggleButtonbox.
 */
@ImplementsContentItem(target = IToggleButtonbox.class)
public class ToggleButtonbox extends JMEContainer implements IToggleButtonbox, IInitable
{

	/** The background off. */
	private IColourRectangle backgroundOff;

	/** The background on. */
	private IColourRectangle backgroundOn;

	/** The image off. */
	private IImage imageOff;

	/** The image on. */
	private IImage imageOn;

	/** The listener. */
	private IImage listener;

	/** The text border off. */
	private IRoundedBorder textBorderOff;

	/** The text border on. */
	private IRoundedBorder textBorderOn;

	/** The text label off. */
	private IMutableLabel textLabelOff;

	/** The text label on. */
	private IMutableLabel textLabelOn;

	/** The toggled. */
	private boolean toggled = false;

	/**
	 * Instantiates a new toggle buttonbox.
	 *
	 * @param name
	 *            the name
	 * @param uuid
	 *            the uuid
	 */
	public ToggleButtonbox(String name, UUID uuid)
	{
		super(name, uuid);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.additionalitems.interfaces.IToggleButtonbox#getImageOff()
	 */
	@Override
	public IImage getImageOff()
	{
		return imageOff;
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.additionalitems.interfaces.IToggleButtonbox#getImageOn()
	 */
	@Override
	public IImage getImageOn()
	{
		return imageOn;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.additionalitems.interfaces.IToggleButtonbox#getListener()
	 */
	@Override
	public IImage getListener()
	{
		return listener;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.additionalitems.interfaces.IToggleButtonbox#getTextBorderOff
	 * ()
	 */
	@Override
	public IRoundedBorder getTextBorderOff()
	{
		return textBorderOff;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.additionalitems.interfaces.IToggleButtonbox#getTextBorderOn()
	 */
	@Override
	public IRoundedBorder getTextBorderOn()
	{
		return textBorderOn;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.additionalitems.interfaces.IToggleButtonbox#getTextLabelOff()
	 */
	@Override
	public IMutableLabel getTextLabelOff()
	{
		return textLabelOff;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.additionalitems.interfaces.IToggleButtonbox#getTextLabelOn()
	 */
	@Override
	public IMutableLabel getTextLabelOn()
	{
		return textLabelOn;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.additionalitems.interfaces.IToggleButtonbox#getToggleStatus()
	 */
	@Override
	public boolean getToggleStatus()
	{
		return toggled;
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.additionalitems.interfaces.IToggleButtonbox#setImage(
	 * multiplicity3.csys.items.image.IImage, com.jme3.math.ColorRGBA,
	 * com.jme3.math.ColorRGBA, multiplicity3.csys.items.image.IImage,
	 * com.jme3.math.ColorRGBA, com.jme3.math.ColorRGBA, float, float,
	 * multiplicity3.csys.stage.IStage)
	 */
	@Override
	public void setImage(IImage imageOff, ColorRGBA bgColourOff, ColorRGBA borderColourOff, IImage imageOn, ColorRGBA bgColourOn, ColorRGBA borderColourOn, float width, float height, IStage stage)
	{
		try
		{

			backgroundOn = stage.getContentFactory().create(IColourRectangle.class, "bg", UUID.randomUUID());
			backgroundOn.setSolidBackgroundColour(bgColourOn);
			backgroundOn.setSize(width, height);
			this.addItem(backgroundOn);

			this.imageOn = imageOn;
			imageOn.setRelativeLocation(new Vector2f());
			imageOn.setRelativeScale(1);
			imageOn.setRelativeRotation(0);
			imageOn.setSize(width, height);
			this.addItem(imageOn);

			textBorderOn = stage.getContentFactory().create(IRoundedBorder.class, "textBorder", UUID.randomUUID());
			textBorderOn.setBorderWidth(3);
			textBorderOn.setSize(width, height);
			textBorderOn.setColor(borderColourOn);
			this.addItem(textBorderOn);

			backgroundOn.setVisible(false);
			imageOn.setVisible(false);
			textBorderOn.setVisible(false);

			backgroundOff = stage.getContentFactory().create(IColourRectangle.class, "bg", UUID.randomUUID());
			backgroundOff.setSolidBackgroundColour(bgColourOff);
			backgroundOff.setSize(width, height);
			this.addItem(backgroundOff);

			this.imageOff = imageOff;
			imageOff.setRelativeLocation(new Vector2f());
			imageOff.setRelativeScale(1);
			imageOff.setRelativeRotation(0);
			imageOff.setSize(width, height);
			this.addItem(imageOff);

			textBorderOff = stage.getContentFactory().create(IRoundedBorder.class, "textBorder", UUID.randomUUID());
			textBorderOff.setBorderWidth(3);
			textBorderOff.setSize(width, height);
			textBorderOff.setColor(borderColourOff);
			this.addItem(textBorderOff);

			listener = stage.getContentFactory().create(IImage.class, "listenBlock", UUID.randomUUID());
			listener.setSize(width, height);
			this.addItem(listener);

			this.getZOrderManager().setAutoBringToTop(false);

		}
		catch (ContentTypeNotBoundException e)
		{
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.additionalitems.interfaces.IToggleButtonbox#setText(java.
	 * lang.String, com.jme3.math.ColorRGBA, com.jme3.math.ColorRGBA,
	 * synergynet3.fonts.FontColour, java.lang.String, com.jme3.math.ColorRGBA,
	 * com.jme3.math.ColorRGBA, synergynet3.fonts.FontColour, float, float,
	 * multiplicity3.csys.stage.IStage)
	 */
	@Override
	public void setText(String textOff, ColorRGBA bgColourOff, ColorRGBA borderColourOff, FontColour fontColourOff, String textOn, ColorRGBA bgColourOn, ColorRGBA borderColourOn, FontColour fontColourOn, float width, float height, IStage stage)
	{
		try
		{

			backgroundOn = stage.getContentFactory().create(IColourRectangle.class, "bg", UUID.randomUUID());
			backgroundOn.setSolidBackgroundColour(bgColourOn);
			backgroundOn.setSize(width, height);
			this.addItem(backgroundOn);

			textLabelOn = stage.getContentFactory().create(IMutableLabel.class, "textLabel", UUID.randomUUID());
			textLabelOn.setFont(FontUtil.getFont(fontColourOn));
			textLabelOn.setRelativeScale(0.8f);
			textLabelOn.setBoxSize(width, height);
			textLabelOn.setText(textOn);
			this.addItem(textLabelOn);

			textBorderOn = stage.getContentFactory().create(IRoundedBorder.class, "textBorder", UUID.randomUUID());
			textBorderOn.setBorderWidth(3);
			textBorderOn.setSize(width, height);
			textBorderOn.setColor(borderColourOn);
			this.addItem(textBorderOn);

			backgroundOn.setVisible(false);
			textLabelOn.setVisible(false);
			textBorderOn.setVisible(false);

			backgroundOff = stage.getContentFactory().create(IColourRectangle.class, "bg", UUID.randomUUID());
			backgroundOff.setSolidBackgroundColour(bgColourOff);
			backgroundOff.setSize(width, height);
			this.addItem(backgroundOff);

			textLabelOff = stage.getContentFactory().create(IMutableLabel.class, "textLabel", UUID.randomUUID());
			textLabelOff.setFont(FontUtil.getFont(fontColourOff));
			textLabelOff.setRelativeScale(0.8f);
			textLabelOff.setBoxSize(width, height);
			textLabelOff.setText(textOff);
			this.addItem(textLabelOff);

			textBorderOff = stage.getContentFactory().create(IRoundedBorder.class, "textBorder", UUID.randomUUID());
			textBorderOff.setBorderWidth(3);
			textBorderOff.setSize(width, height);
			textBorderOff.setColor(borderColourOff);
			this.addItem(textBorderOff);

			listener = stage.getContentFactory().create(IImage.class, "listenBlock", UUID.randomUUID());
			listener.setSize(width, height);
			this.addItem(listener);

			this.getZOrderManager().setAutoBringToTop(false);

		}
		catch (ContentTypeNotBoundException e)
		{
		}
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.jme3csys.items.item.JMEItem#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean isVisible)
	{
		super.setVisible(isVisible);
		if (isVisible)
		{
			setButtonVisibility();
		}
	};

	/*
	 * (non-Javadoc)
	 * @see synergynet3.additionalitems.interfaces.IToggleButtonbox#toggle()
	 */
	@Override
	public void toggle()
	{
		toggled = !toggled;
		setButtonVisibility();
	}

	/**
	 * Sets the button visibility.
	 */
	private void setButtonVisibility()
	{
		backgroundOn.setVisible(toggled);
		textBorderOn.setVisible(toggled);
		backgroundOff.setVisible(!toggled);
		textBorderOff.setVisible(!toggled);

		if ((textLabelOn != null) && (textLabelOff != null))
		{
			textLabelOn.setVisible(toggled);
			textLabelOff.setVisible(!toggled);
		}
		else if ((imageOn != null) && (imageOff != null))
		{
			imageOn.setVisible(toggled);
			imageOff.setVisible(!toggled);
		}
	}

}
