package synergynet3.activitypack1.core.lightbox.lightboxmodel.items;

import java.awt.geom.Point2D.Float;
import java.io.Serializable;

public class TextItem extends LightBoxItem implements Serializable {
	private static final long serialVersionUID = -8639066683140974098L;
	
	private String text;


	private Float size;
	private int fontSize;
	
	public TextItem() {		
	}

	public void setText(String text) {
		this.text = text;		
	}
	
	public String getText() {
		return this.text;
	}

	public void setSize(Float size) {
		this.size = size;		
	}
	
	public Float getSize() {
		return this.size;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;		
	}
	
	public int getFontSize() {
		return this.fontSize;
	}

}
