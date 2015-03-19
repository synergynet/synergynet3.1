package synergynet3.additionalitems.jme;

import java.util.UUID;

import synergynet3.additionalitems.interfaces.IButtonbox;
import synergynet3.fonts.FontColour;
import synergynet3.fonts.FontUtil;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;

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

@ImplementsContentItem(target = IButtonbox.class)
public class Buttonbox extends JMEContainer implements IButtonbox, IInitable {
	
	private IColourRectangle background;
	private IMutableLabel textLabel;
	private IRoundedBorder textBorder;
	private IImage listener;
	private float width;
	private float height;

	public Buttonbox(String name, UUID uuid) {
		super(name, uuid);		
	}

	@Override
	public void setText(String text, ColorRGBA bgColour, ColorRGBA borderColour, FontColour fontColour, float width, float height, IStage stage) {	
		this.width = width;
		this.height = height;
		
		try{			
			
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

		}catch(ContentTypeNotBoundException e){}		
	}

	@Override
	public void setImage(IImage image, ColorRGBA bgColour, ColorRGBA borderColour, float width, float height, IStage stage) {
		try{					
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

		}catch(ContentTypeNotBoundException e){}	
	}

	
	public void setBackgroundColour(ColorRGBA colour) {
		background.setSolidBackgroundColour(colour);
	}

	public IMutableLabel getTextLabel() {
		return textLabel;
	}

	public IImage getListener() {
		return listener;
	}

	public IRoundedBorder getTextBorder() {
		return textBorder;
	}

	@Override
	public String getText() {
		return textLabel.getText();
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

}
