package synergynet3.activitypack1.core.lightbox.ppt2lightbox.elements;

import java.awt.Dimension;
import java.awt.geom.Point2D;

import org.apache.poi.hslf.model.TextShape;

import synergynet3.activitypack1.core.lightbox.lightboxmodel.items.TextItem;

/**
 * The Class TextShapeConvertor.
 */
public class TextShapeConvertor extends Convertor
{

	/** The font size. */
	private int fontSize;

	/** The is moveable. */
	private boolean isMoveable;

	/** The position. */
	private Point2D.Float position;

	/** The rotation. */
	private int rotation;

	/** The shape. */
	private TextShape shape;

	/** The size. */
	private Point2D.Float size;

	/** The text. */
	private String text;

	/**
	 * Instantiates a new text shape convertor.
	 *
	 * @param shape
	 *            the shape
	 * @param slideSize
	 *            the slide size
	 */
	public TextShapeConvertor(TextShape shape, Dimension slideSize)
	{
		super(shape, slideSize);
		this.shape = shape;
	}

	/**
	 * Convert text shape.
	 *
	 * @param textShape
	 *            the text shape
	 * @param slideSize
	 *            the slide size
	 * @return the text item
	 */
	public static TextItem convertTextShape(TextShape textShape, Dimension slideSize)
	{
		return new TextShapeConvertor(textShape, slideSize).convert();
	}

	/**
	 * Convert.
	 *
	 * @return the text item
	 */
	private TextItem convert()
	{
		extractData();
		TextItem item = createEmptyTextItem();
		populateTextItemWithExtractedData(item);
		return item;
	}

	/**
	 * Creates the empty text item.
	 *
	 * @return the text item
	 */
	private TextItem createEmptyTextItem()
	{
		return new TextItem();
	}

	/**
	 * Extract data.
	 */
	private void extractData()
	{
		this.text = getTextFromRichText();
		this.fontSize = getFontSizeFromRichText();
		this.isMoveable = interpretMoveablePropertyFromBackgroundFillColour();
		this.position = getPositionFromShapeBounds();
		this.size = getSizeFromShapeBounds();
		this.rotation = getRotationDegreesFromShape();
	}

	/**
	 * Gets the font size from rich text.
	 *
	 * @return the font size from rich text
	 */
	private int getFontSizeFromRichText()
	{
		int fontSize = 32;
		try
		{
			fontSize = shape.getTextRun().getRichTextRunAt(0).getFontSize();
		}
		catch (NullPointerException npe)
		{
		}
		return fontSize;
	}

	/**
	 * Gets the rotation degrees from shape.
	 *
	 * @return the rotation degrees from shape
	 */
	private int getRotationDegreesFromShape()
	{
		return this.shape.getRotation();
	}

	/**
	 * Gets the text from rich text.
	 *
	 * @return the text from rich text
	 */
	private String getTextFromRichText()
	{
		String extractedText = "";
		try
		{
			extractedText = shape.getTextRun().getRichTextRunAt(0).getText();
		}
		catch (NullPointerException npe)
		{
		}
		return extractedText;
	}

	/**
	 * Populate text item with extracted data.
	 *
	 * @param item
	 *            the item
	 */
	private void populateTextItemWithExtractedData(TextItem item)
	{
		item.setText(this.text);
		item.setMoveable(this.isMoveable);
		item.setPosition(this.position);
		item.setSize(this.size);
		item.setFontSize(this.fontSize);
		item.setRotationDegrees(this.rotation);
	}
}
