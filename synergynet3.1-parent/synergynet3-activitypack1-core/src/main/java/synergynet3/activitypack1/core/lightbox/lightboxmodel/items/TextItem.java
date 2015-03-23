package synergynet3.activitypack1.core.lightbox.lightboxmodel.items;

import java.awt.geom.Point2D.Float;
import java.io.Serializable;

/**
 * The Class TextItem.
 */
public class TextItem extends LightBoxItem implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8639066683140974098L;

	/** The font size. */
	private int fontSize;

	/** The size. */
	private Float size;

	/** The text. */
	private String text;

	/**
	 * Instantiates a new text item.
	 */
	public TextItem() {
	}

	/**
	 * Gets the font size.
	 *
	 * @return the font size
	 */
	public int getFontSize() {
		return this.fontSize;
	}

	/**
	 * Gets the size.
	 *
	 * @return the size
	 */
	public Float getSize() {
		return this.size;
	}

	/**
	 * Gets the text.
	 *
	 * @return the text
	 */
	public String getText() {
		return this.text;
	}

	/**
	 * Sets the font size.
	 *
	 * @param fontSize the new font size
	 */
	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	/**
	 * Sets the size.
	 *
	 * @param size the new size
	 */
	public void setSize(Float size) {
		this.size = size;
	}

	/**
	 * Sets the text.
	 *
	 * @param text the new text
	 */
	public void setText(String text) {
		this.text = text;
	}

}
