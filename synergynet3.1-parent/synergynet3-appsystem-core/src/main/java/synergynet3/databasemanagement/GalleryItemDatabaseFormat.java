package synergynet3.databasemanagement;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * The Class GalleryItemDatabaseFormat.
 */
public class GalleryItemDatabaseFormat implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -1023344043552809499L;

	/** The feedback items. */
	private ArrayList<Object[]> feedbackItems = new ArrayList<Object[]>();

	/** The type. */
	private String type = "";

	/** The values. */
	private ArrayList<Object> values = new ArrayList<Object>();

	/** The height. */
	private float width, height = 0;

	/**
	 * Adds the feedback item.
	 *
	 * @param feedbackItem the feedback item
	 */
	public void addFeedbackItem(Object[] feedbackItem) {
		feedbackItems.add(feedbackItem);
	}

	/**
	 * Adds the value.
	 *
	 * @param value the value
	 */
	public void addValue(Object value) {
		values.add(value);
	}

	/**
	 * @return the feedbackItems
	 */
	public ArrayList<Object[]> getFeedbackItems() {
		return feedbackItems;
	}

	/**
	 * @return the height
	 */
	public float getHeight() {
		return height;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return the values
	 */
	public ArrayList<Object> getValues() {
		return values;
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
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @param width the width to set
	 */
	public void setWidth(float width) {
		this.width = width;
	}

}
