package synergynet3.additionalitems.jme;

import java.util.UUID;

import multiplicity3.csys.annotations.ImplementsContentItem;
import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.gfx.Gradient;
import multiplicity3.csys.gfx.Gradient.GradientDirection;
import multiplicity3.csys.items.border.IRoundedBorder;
import multiplicity3.csys.items.image.IImage;
import multiplicity3.csys.items.mutablelabel.IMutableLabel;
import multiplicity3.csys.items.shapes.IColourRectangle;
import multiplicity3.csys.stage.IStage;
import multiplicity3.jme3csys.items.IInitable;
import multiplicity3.jme3csys.items.container.JMEContainer;
import synergynet3.additionalitems.interfaces.IButtonbox;
import synergynet3.fonts.FontColour;
import synergynet3.fonts.FontUtil;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;

/**
 * The Class Buttonbox.
 */
@ImplementsContentItem(target = IButtonbox.class)
public class Buttonbox extends JMEContainer implements IButtonbox, IInitable
{

	/** The background. */
	private IColourRectangle background;

	/** The height. */
	private float height;

	/** The listener. */
	private IImage listener;

	/** The text border. */
	private IRoundedBorder textBorder;

	/** The text label. */
	private IMutableLabel textLabel;

	/** The width. */
	private float width;

	/**
	 * Instantiates a new buttonbox.
	 *
	 * @param name
	 *            the name
	 * @param uuid
	 *            the uuid
	 */
	public Buttonbox(String name, UUID uuid)
	{
		super(name, uuid);
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.additionalitems.interfaces.IButtonbox#getHeight()
	 */
	@Override
	public float getHeight()
	{
		return height;
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.additionalitems.interfaces.IButtonbox#getListener()
	 */
	@Override
	public IImage getListener()
	{
		return listener;
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.additionalitems.interfaces.IButtonbox#getText()
	 */
	@Override
	public String getText()
	{
		return textLabel.getText();
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.additionalitems.interfaces.IButtonbox#getTextBorder()
	 */
	@Override
	public IRoundedBorder getTextBorder()
	{
		return textBorder;
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.additionalitems.interfaces.IButtonbox#getTextLabel()
	 */
	@Override
	public IMutableLabel getTextLabel()
	{
		return textLabel;
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.additionalitems.interfaces.IButtonbox#getWidth()
	 */
	@Override
	public float getWidth()
	{
		return width;
	}

	/**
	 * Sets the background colour.
	 *
	 * @param colour
	 *            the new background colour
	 */
	public void setBackgroundColour(ColorRGBA colour)
	{
		background.setSolidBackgroundColour(colour);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.additionalitems.interfaces.IButtonbox#setImage(multiplicity3
	 * .csys.items.image.IImage, com.jme3.math.ColorRGBA,
	 * com.jme3.math.ColorRGBA, float, float, multiplicity3.csys.stage.IStage)
	 */
	@Override
	public void setImage(IImage image, ColorRGBA bgColour, ColorRGBA borderColour, float width, float height, IStage stage)
	{
		try
		{
			background = stage.getContentFactory().create(IColourRectangle.class, "bg", UUID.randomUUID());
			background.setSolidBackgroundColour(bgColour);
			background.setSize(width, height);
			this.addItem(background);

			image.setRelativeLocation(new Vector2f());
			image.setRelativeScale(1);
			image.setRelativeRotation(0);
			image.setSize(width, height);
			this.addItem(image);

			textBorder = stage.getContentFactory().create(IRoundedBorder.class, "textBorder", UUID.randomUUID());
			textBorder.setBorderWidth(3);
			textBorder.setSize(width, height);
			textBorder.setColor(borderColour);
			this.addItem(textBorder);

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
	 * synergynet3.additionalitems.interfaces.IButtonbox#setText(java.lang.String
	 * , com.jme3.math.ColorRGBA, com.jme3.math.ColorRGBA,
	 * synergynet3.fonts.FontColour, float, float,
	 * multiplicity3.csys.stage.IStage)
	 */
	@Override
	public void setText(String text, ColorRGBA bgColour, ColorRGBA borderColour, FontColour fontColour, float width, float height, IStage stage)
	{
		this.width = width;
		this.height = height;

		try
		{

			background = stage.getContentFactory().create(IColourRectangle.class, "bg", UUID.randomUUID());
			background.enableTransparency();
			background.setGradientBackground(new Gradient(bgColour, bgColour, GradientDirection.DIAGONAL));
			background.setSize(width, height);
			this.addItem(background);

			textLabel = stage.getContentFactory().create(IMutableLabel.class, "textLabel", UUID.randomUUID());
			textLabel.setFont(FontUtil.getFont(fontColour));
			textLabel.setRelativeScale(0.8f);
			textLabel.setBoxSize(width, height);
			textLabel.setText(text);
			this.addItem(textLabel);

			textBorder = stage.getContentFactory().create(IRoundedBorder.class, "textBorder", UUID.randomUUID());
			textBorder.setBorderWidth(3);
			textBorder.setSize(width, height);
			textBorder.setColor(borderColour);
			this.addItem(textBorder);

			listener = stage.getContentFactory().create(IImage.class, "listenBlock", UUID.randomUUID());
			listener.setSize(width, height);
			this.addItem(listener);

			this.getZOrderManager().setAutoBringToTop(false);

		}
		catch (ContentTypeNotBoundException e)
		{
		}
	}

}
