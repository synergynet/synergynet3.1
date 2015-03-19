package synergynet3.museum.table.mainapp.textentry;

import java.util.UUID;

import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.stage.IStage;

import synergynet3.additionalitems.interfaces.ITextbox;
import synergynet3.museum.table.settingsapp.MuseumAppPreferences;

import com.jme3.math.ColorRGBA;

public class TextScrollBox {
	
	private static final int CHARACTER_DISPLAY_LIMIT = 25;
	
	private static final ColorRGBA INVIS = new ColorRGBA(0,0,0,0);
	
	private String text = "";
	private ITextbox textLabel;

	public TextScrollBox(IStage stage, float width, float height, float gapSize, float textScale) throws ContentTypeNotBoundException{
		textLabel = stage.getContentFactory().create(ITextbox.class, "textLabel", UUID.randomUUID());
		textLabel.setColours(INVIS, INVIS, MuseumAppPreferences.getTextInputScrollboxFontColour());
		textLabel.setWidth((width - gapSize) / textScale);
		textLabel.setHeight(height);
		textLabel.setText(" ", stage);		
		textLabel.setRelativeScale(textScale);
		textLabel.setMovable(false);	
	}

	public void backspace() {
		if (text.length() != 0){
			text = text.substring(0, text.length()-1);
		}
		updateText();		
	}
	
	public void addChar(String s){
		text += s;
		updateText();
	}

	private void updateText() {
		if (text.length() >= CHARACTER_DISPLAY_LIMIT + 3){
			textLabel.getTextLabel().setText("..." + text.substring(text.length() - (CHARACTER_DISPLAY_LIMIT), text.length()));
		}else{
			textLabel.getTextLabel().setText(text);
		}
	}

	/**
	 * @return the textLabel
	 */
	public ITextbox getTextLabel() {
		return textLabel;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}
}
