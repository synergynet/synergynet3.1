package synergynet3.additionalitems.jme;

import java.util.UUID;

import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;

import synergynet3.additionalitems.interfaces.ITextbox;
import synergynet3.cachecontrol.IItemCachable;
import synergynet3.databasemanagement.GalleryItemDatabaseFormat;
import synergynet3.fonts.FontColour;
import synergynet3.fonts.FontUtil;

import multiplicity3.csys.annotations.ImplementsContentItem;
import multiplicity3.csys.behaviours.RotateTranslateScaleBehaviour;
import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.items.border.IRoundedBorder;
import multiplicity3.csys.items.image.IImage;
import multiplicity3.csys.items.mutablelabel.IMutableLabel;
import multiplicity3.csys.items.shapes.IColourRectangle;
import multiplicity3.csys.stage.IStage;
import multiplicity3.jme3csys.items.IInitable;
import multiplicity3.jme3csys.items.container.JMEContainer;

@ImplementsContentItem(target = ITextbox.class)
public class Textbox extends JMEContainer implements ITextbox, IInitable, IItemCachable {
	
	private boolean movable = true;
	
	private float bgWidth = 50f;
	private float bgHeight = 50f;
	
	private boolean scaleLimitsSet = false;
	private float scaleMin = 0.5f;
	private float scaleMax = 1.5f;
	
	private IColourRectangle background;
	private IMutableLabel textLabel;
	private IRoundedBorder textBorder;
	
	private IImage listenBlock;
	private RotateTranslateScaleBehaviour rts;
	
	private ColorRGBA bgColour = ColorRGBA.Black;
	private ColorRGBA borderColour = ColorRGBA.White;
	private FontColour fontColour = FontColour.Black;
	
	private float width = -1;
	private float height = -1;
		
	public static final String CACHABLE_TYPE = "CACHABLE_TEXT";

	public Textbox(String name, UUID uuid) {
		super(name, uuid);		
	}

	@Override
	public GalleryItemDatabaseFormat deconstruct(String loc) {		
		GalleryItemDatabaseFormat galleryItem = new GalleryItemDatabaseFormat();
		galleryItem.setType(CACHABLE_TYPE);
		galleryItem.addValue(textLabel.getText());
		galleryItem.addValue(movable);
		
		galleryItem.addValue(bgColour.r);
		galleryItem.addValue(bgColour.g);
		galleryItem.addValue(bgColour.b);
		galleryItem.addValue(bgColour.a);
		
		galleryItem.addValue(borderColour.r);
		galleryItem.addValue(borderColour.g);
		galleryItem.addValue(borderColour.b);
		galleryItem.addValue(borderColour.a);
		
		galleryItem.addValue(fontColour.toString());
		
		galleryItem.addValue(scaleLimitsSet);
		galleryItem.addValue(scaleMin);
		galleryItem.addValue(scaleMax);
		
		return galleryItem;
	}

	public static Textbox reconstruct(GalleryItemDatabaseFormat galleryItem, IStage stage, String loc) {
		try {
			Textbox textbox = stage.getContentFactory().create(ITextbox.class, (String)galleryItem.getValues().get(0), UUID.randomUUID());
			
			String text = (String)galleryItem.getValues().get(0);
			boolean movable = (Boolean)galleryItem.getValues().get(1);			
			ColorRGBA bgColour = new ColorRGBA((Float)galleryItem.getValues().get(2), (Float)galleryItem.getValues().get(3), (Float)galleryItem.getValues().get(4), (Float)galleryItem.getValues().get(5));
			ColorRGBA borderColour = new ColorRGBA((Float)galleryItem.getValues().get(6), (Float)galleryItem.getValues().get(7), (Float)galleryItem.getValues().get(8), (Float)galleryItem.getValues().get(9));
			FontColour fontColour = FontUtil.getFontColourFromString((String)galleryItem.getValues().get(10));
			boolean scaleLimitsSet = (Boolean)galleryItem.getValues().get(11);	
			float scaleMin = (Float)galleryItem.getValues().get(12);
			float scaleMax = (Float)galleryItem.getValues().get(13);
			
			textbox.setColours(bgColour, borderColour, fontColour);
			textbox.setMovable(movable);
			if (scaleLimitsSet){
				textbox.setScaleLimits(scaleMin, scaleMax);
			}
			textbox.setWidth(galleryItem.getWidth());
			textbox.setHeight(galleryItem.getHeight());
			textbox.setText(text, stage); 
			return textbox;
		} catch (ContentTypeNotBoundException e) {
			return null;
		}
	}
	
	public void setScaleLimits(float scaleMin, float scaleMax){
		scaleLimitsSet = true;
		this.scaleMin = scaleMin;
		this.scaleMax = scaleMax;
		if (rts != null){
			rts.setScaleLimits(scaleMin, scaleMax);
		}
	}

	public void setMovable(boolean movable){
		this.movable = movable;
		if (rts != null){
			rts.setActive(false);
		}
		this.setInteractionEnabled(movable);
	}
	
	public void setColours(ColorRGBA bgColour, ColorRGBA borderColour, FontColour fontColour){
		this.bgColour = bgColour;
		this.borderColour = borderColour;
		this.fontColour = fontColour;
	}
		
	public void setText(String text, IStage stage) {

		try{						
			textLabel = stage.getContentFactory().create(IMutableLabel.class, "textLabel", UUID.randomUUID());
			textLabel.setFont(FontUtil.getFont(fontColour));
			
			textLabel.setText("A");
			BitmapText bitmapText = (BitmapText)textLabel.getManipulableSpatial();	
			float border = bitmapText.getLineWidth();
			
			textLabel.setText(text);	
							
			if (width == -1){
				width = bitmapText.getLineWidth();
			}
			
			textLabel.setBoxSize(width, height);	
			bgWidth = bitmapText.getLineWidth() + border;
			bgHeight = bitmapText.getHeight();
			Vector2f bgSize = new Vector2f(bgWidth, bgHeight);
			
			textBorder = stage.getContentFactory().create(IRoundedBorder.class, "textBorder", UUID.randomUUID());		
			textBorder.setBorderWidth(3);
			textBorder.setSize(bgSize);
			textBorder.setColor(borderColour);	
					
			background = stage.getContentFactory().create(IColourRectangle.class, "bg", UUID.randomUUID());
			background.enableTransparency();
			background.setSolidBackgroundColour(bgColour);
			background.setSize(bgSize);
			this.addItem(background);					
			
			this.addItem(textLabel);	
			this.addItem(textBorder);	
			
			textBorder.setInteractionEnabled(false);
			textLabel.setInteractionEnabled(false);
			
			listenBlock = stage.getContentFactory().create(IImage.class, "listenBlock", UUID.randomUUID());
			listenBlock.setSize(bgSize);				
			this.addItem(listenBlock);	
					
			rts = stage.getBehaviourMaker().addBehaviour(listenBlock, RotateTranslateScaleBehaviour.class);
			if (scaleLimitsSet){
				rts.setScaleLimits(scaleMin, scaleMax);
			}
			rts.setItemActingOn(this);		
			rts.setActive(movable);

			this.setInteractionEnabled(movable);

		}catch(ContentTypeNotBoundException e){
		}catch(NullPointerException e){
		}
		
	}

	public IColourRectangle getBackground() {
		return background;
	}

	public IMutableLabel getTextLabel() {
		return textLabel;
	}
	
	public float getWidth() {
		return bgWidth;
	}

	public float getHeight() {
		return bgHeight;
	}

	public IRoundedBorder getTextBorder() {
		return textBorder;
	}

	public IImage getListenBlock() {
		return listenBlock;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public void setHeight(float height) {
		this.height = height;
	}

}
