package synergynet3.activitypack1.core.lightbox.ppt2lightbox.elements;

import java.awt.Dimension;
import java.awt.geom.Point2D;

import org.apache.poi.hslf.model.TextShape;

import synergynet3.activitypack1.core.lightbox.lightboxmodel.items.TextItem;

public class TextShapeConvertor extends Convertor {
	private TextShape shape;

	private boolean isMoveable;
	private Point2D.Float position;
	private Point2D.Float size;
	private String text;

	private int fontSize;

	private int rotation;

	public static TextItem convertTextShape(TextShape textShape, Dimension slideSize) {		
		return new TextShapeConvertor(textShape, slideSize).convert();
	}

	public TextShapeConvertor(TextShape shape, Dimension slideSize) {
		super(shape, slideSize);
		this.shape = shape;		
	}

	private TextItem convert() {
		extractData();
		TextItem item = createEmptyTextItem();
		populateTextItemWithExtractedData(item);
		return item;
	}

	private void extractData() {
		this.text = getTextFromRichText();		
		this.fontSize = getFontSizeFromRichText();
		this.isMoveable = interpretMoveablePropertyFromBackgroundFillColour();
		this.position = getPositionFromShapeBounds();
		this.size = getSizeFromShapeBounds();
		this.rotation = getRotationDegreesFromShape();
	}
	
	private int getRotationDegreesFromShape() {
		return this.shape.getRotation();
	}

	private int getFontSizeFromRichText() {
		int fontSize = 32;
		try {
			fontSize = shape.getTextRun().getRichTextRunAt(0).getFontSize();
		}catch(NullPointerException npe) {}
		return fontSize;
	}

	private String getTextFromRichText() {
		String extractedText = "";
		try {
			extractedText = shape.getTextRun().getRichTextRunAt(0).getText();
		}catch(NullPointerException npe) {}
		return extractedText;
	}



	private TextItem createEmptyTextItem() {
		return new TextItem();
	}

	private void populateTextItemWithExtractedData(TextItem item) {
		item.setText(this.text);
		item.setMoveable(this.isMoveable);
		item.setPosition(this.position);
		item.setSize(this.size);
		item.setFontSize(this.fontSize);
		item.setRotationDegrees(this.rotation);
	}
}
