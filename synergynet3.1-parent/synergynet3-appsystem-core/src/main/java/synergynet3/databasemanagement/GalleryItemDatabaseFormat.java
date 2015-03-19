package synergynet3.databasemanagement;

import java.io.Serializable;
import java.util.ArrayList;

public class GalleryItemDatabaseFormat implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1023344043552809499L;
	private String type = "";
	private float width, height = 0;
	private ArrayList<Object> values = new ArrayList<Object>();
	private ArrayList<Object[]> feedbackItems = new ArrayList<Object[]>();

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param width the width to set
	 */
	public void setWidth(float width) {
		this.width = width;
	}

	/**
	 * @return the width
	 */
	public float getWidth() {
		return width;
	}

	/**
	 * @param height the height to set
	 */
	public void setHeight(float height) {
		this.height = height;
	}

	/**
	 * @return the height
	 */
	public float getHeight() {
		return height;
	}

	public void addValue(Object value) {
		values.add(value);
	}

	/**
	 * @return the values
	 */
	public ArrayList<Object> getValues() {
		return values;
	}

	public void addFeedbackItem(Object[] feedbackItem) {
		feedbackItems.add(feedbackItem);		
	}
	
	/**
	 * @return the feedbackItems
	 */
	public ArrayList<Object[]> getFeedbackItems() {
		return feedbackItems;
	}
	
}
