package synergynet3.museum.table.mainapp;

import java.util.UUID;

import com.jme3.math.Vector2f;

import synergynet3.additionalitems.interfaces.ITextbox;
import synergynet3.museum.table.MuseumApp;
import synergynet3.museum.table.settingsapp.MuseumAppPreferences;
import synergynet3.museum.table.utils.ImageUtils;
import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.items.image.IImage;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.stage.IStage;
import multiplicity3.input.MultiTouchEventAdapter;
import multiplicity3.input.events.MultiTouchCursorEvent;

public class LabelGenerator {	

	public static final float TEXT_HEIGHT = 60f;
	public static final float CIRCLE_SIZE = TEXT_HEIGHT - 20f;
	public static final float TEXT_WIDTH_LIMIT = 500f;
	public static final float ITEM_SCALE = 0.5f;
	
	private static final float NAME_SCALE = 0.8f;
	private static final float LINK_SCALE = 0.65f;
	
	public static ITextbox generateName(final String name, IStage stage, final MuseumApp app) throws ContentTypeNotBoundException{	
		
		ITextbox textItem = stage.getContentFactory().create(ITextbox.class, "fact", UUID.randomUUID());
		textItem.setMovable(true);
		textItem.setColours(MuseumAppPreferences.getEntityBackgroundColour(), MuseumAppPreferences.getEntityBorderColour(), MuseumAppPreferences.getEntityFontColour());
		textItem.setHeight(TEXT_HEIGHT);
		textItem.setText(name, stage);
		textItem.setWidth(TEXT_WIDTH_LIMIT);
		
		textItem.getBackground().setSize(textItem.getBackground().getWidth() + TEXT_HEIGHT, TEXT_HEIGHT);
		textItem.getBackground().setRelativeLocation(new Vector2f(TEXT_HEIGHT/2, 0));
		textItem.getTextBorder().setSize(textItem.getBackground().getWidth(), TEXT_HEIGHT);
		textItem.getTextBorder().setRelativeLocation(new Vector2f(TEXT_HEIGHT/2, 0));
		((IImage)textItem.getListenBlock()).setSize(textItem.getBackground().getWidth(), TEXT_HEIGHT);
		textItem.getListenBlock().setRelativeLocation(new Vector2f(TEXT_HEIGHT/2, 0));
		textItem.setRelativeScale(NAME_SCALE);
		textItem.setScaleLimits(NAME_SCALE - 0.25f, NAME_SCALE + 0.25f);
			
		IImage circle = stage.getContentFactory().create(IImage.class, "circleBackground", UUID.randomUUID());
		circle.setImage(ImageUtils.getImage(MuseumAppPreferences.getEntityCloseButtonBackgroundColour(), ImageUtils.RESOURCE_DIR + "entitybuttons/close/", "_close_button.png"));
		circle.setSize(CIRCLE_SIZE, CIRCLE_SIZE);		
		circle.setRelativeLocation(new Vector2f(textItem.getWidth()/2 + textItem.getHeight()/2, 0));		
		textItem.addItem(circle);

		circle.getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter() {
			@Override
			public void cursorClicked(MultiTouchCursorEvent event) {					
				app.closeTree(name);
			}
		});		
		
		textItem.getZOrderManager().setAutoBringToTop(false);
		
		return textItem;
	}

	public static IItem generateLink(final String link, final String parent, IStage stage, final MuseumApp app) throws ContentTypeNotBoundException{
		
		String prefix = "";
		
		if (!MuseumAppPreferences.getEntityLinkText().equals("")){
			prefix = MuseumAppPreferences.getEntityLinkText() + "\n";
		}
		
		final ITextbox textItem = stage.getContentFactory().create(ITextbox.class, "fact", UUID.randomUUID());
		textItem.setMovable(true);
		textItem.setColours(MuseumAppPreferences.getEntityBackgroundColour(), MuseumAppPreferences.getEntityBorderColour(), MuseumAppPreferences.getEntityFontColour());
		textItem.setHeight(TEXT_HEIGHT);
		textItem.setText(prefix + link, stage);
		textItem.setWidth(TEXT_WIDTH_LIMIT);
		
		textItem.getBackground().setSize(textItem.getBackground().getWidth() + TEXT_HEIGHT, textItem.getHeight());
		textItem.getBackground().setRelativeLocation(new Vector2f(TEXT_HEIGHT/2, 0));
		textItem.getTextBorder().setSize(textItem.getBackground().getWidth(), textItem.getHeight());
		textItem.getTextBorder().setRelativeLocation(new Vector2f(TEXT_HEIGHT/2, 0));
		((IImage)textItem.getListenBlock()).setSize(textItem.getBackground().getWidth(), textItem.getHeight());
		textItem.getListenBlock().setRelativeLocation(new Vector2f(TEXT_HEIGHT/2, 0));
		textItem.setRelativeScale(LINK_SCALE);
		textItem.setScaleLimits(LINK_SCALE - 0.25f, LINK_SCALE + 0.25f);
			
		IImage circle = stage.getContentFactory().create(IImage.class, "circleBackground", UUID.randomUUID());
		circle.setImage(ImageUtils.getImage(MuseumAppPreferences.getEntityLinkButtonBackgroundColour(), ImageUtils.RESOURCE_DIR + "entitybuttons/link/", "_link_button.png"));
		circle.setSize(CIRCLE_SIZE, CIRCLE_SIZE);		
		circle.setRelativeLocation(new Vector2f(textItem.getWidth()/2 + textItem.getHeight()/2, 0));		
		textItem.addItem(circle);

		circle.getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter() {
			@Override
			public void cursorClicked(MultiTouchCursorEvent event) {	
				app.focusShift(parent, link, textItem.getRelativeLocation());
			}
		});		
		
		textItem.getZOrderManager().setAutoBringToTop(false);
		
		return textItem;		
	}	
	
	public static IItem generateFact(String fact, IStage stage) throws ContentTypeNotBoundException{
		ITextbox textItem = stage.getContentFactory().create(ITextbox.class, "fact", UUID.randomUUID());
		textItem.setMovable(true);
		textItem.setColours(MuseumAppPreferences.getEntityBackgroundColour(), MuseumAppPreferences.getEntityBorderColour(), MuseumAppPreferences.getEntityFontColour());
		textItem.setWidth(TEXT_WIDTH_LIMIT);
		textItem.setHeight(TEXT_HEIGHT);	
		textItem.setText(fact, stage);
		textItem.setRelativeScale(ITEM_SCALE);
		textItem.setScaleLimits(ITEM_SCALE - 0.25f, ITEM_SCALE + 0.25f);
		return textItem;
	}	
	
}
