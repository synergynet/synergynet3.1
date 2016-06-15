package synergynet3.web.commons.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * The Class SimpleListBox.
 */
public class SimpleListBox extends VerticalPanel
{

	/** The allows selection. */
	private boolean allowsSelection = true;

	/** The multiple select. */
	private boolean multipleSelect = false;

	/**
	 * Instantiates a new simple list box.
	 */
	public SimpleListBox()
	{
		super();
		setStylePrimaryName("simpleListBox");
	}

	// *** managing items ***

	/**
	 * Adds the click handler.
	 *
	 * @param handler
	 *            the handler
	 * @return the handler registration
	 */
	public HandlerRegistration addClickHandler(final ClickHandler handler)
	{
		return addDomHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				if (allowsSelection)
				{
					handler.onClick(event);
				}
			}

		}, ClickEvent.getType());
	}

	/**
	 * Adds the item.
	 *
	 * @param item
	 *            the item
	 */
	public void addItem(String item)
	{
		addLabel(item);
	}

	/**
	 * Gets the item at index.
	 *
	 * @param index
	 *            the index
	 * @return the item at index
	 */
	public String getItemAtIndex(int index)
	{
		Label labelForIndex = getLabelAtIndex(index);
		return labelForIndex.getText();
	}

	/**
	 * Gets the item count.
	 *
	 * @return the item count
	 */
	public int getItemCount()
	{
		return getWidgetCount();
	}

	// *** item selection ***

	/**
	 * Gets the selected index.
	 *
	 * @return the selected index
	 */
	public int getSelectedIndex()
	{
		for (int i = 0; i < getItemCount(); i++)
		{
			if (isItemSelectedAtIndex(i))
			{
				return i;
			}
		}
		return -1;
	}

	/**
	 * Gets the selected item.
	 *
	 * @return the selected item
	 */
	public String getSelectedItem()
	{
		int selectedIndex = getSelectedIndex();
		if (selectedIndex == -1)
		{
			return null;
		}
		return getItemAtIndex(selectedIndex);
	}

	/**
	 * Gets the selected items.
	 *
	 * @return the selected items
	 */
	public List<String> getSelectedItems()
	{
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < getItemCount(); i++)
		{
			if (isItemSelectedAtIndex(i))
			{
				list.add(getItemAtIndex(i));
			}
		}
		return list;
	}

	/**
	 * Checks if is multiple select.
	 *
	 * @return true, if is multiple select
	 */
	public boolean isMultipleSelect()
	{
		return this.multipleSelect;
	}

	/**
	 * Checks if is selected.
	 *
	 * @param index
	 *            the index
	 * @return true, if is selected
	 */
	public boolean isSelected(int index)
	{
		return isItemSelectedAtIndex(index);
	}

	/**
	 * Removes the all items.
	 */
	public void removeAllItems()
	{
		removeAllWidgets();
	}

	/**
	 * Select all.
	 */
	public void selectAll()
	{
		if (!allowsSelection)
		{
			return;
		}
		if (!multipleSelect)
		{
			return;
		}
		makeAllLabelsSelected();
	}

	/**
	 * Sets the allows selection.
	 *
	 * @param allowsSelection
	 *            the new allows selection
	 */
	public void setAllowsSelection(boolean allowsSelection)
	{
		this.allowsSelection = allowsSelection;
		if (!allowsSelection)
		{
			makeAllLabelsUnselected();
		}
	}

	// *** handlers ***

	/**
	 * Sets the multiple select.
	 *
	 * @param isMultipleSelect
	 *            the new multiple select
	 */
	public void setMultipleSelect(boolean isMultipleSelect)
	{
		this.multipleSelect = isMultipleSelect;
		makeAllLabelsUnselected();
	}

	// *** private methods ***

	/**
	 * Adds the label.
	 *
	 * @param item
	 *            the item
	 */
	private void addLabel(String item)
	{
		final Label label = new Label(item);
		label.setStylePrimaryName("simpleListBoxItem");
		label.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				itemClicked(label);
			}
		});
		this.add(label);
	}

	/**
	 * Gets the label at index.
	 *
	 * @param index
	 *            the index
	 * @return the label at index
	 */
	private Label getLabelAtIndex(int index)
	{
		return (Label) this.getWidget(index);
	}

	/**
	 * Checks if is item selected at index.
	 *
	 * @param i
	 *            the i
	 * @return true, if is item selected at index
	 */
	private boolean isItemSelectedAtIndex(int i)
	{
		Label labelAtIndex = getLabelAtIndex(i);
		return labelIsSelected(labelAtIndex);
	}

	// *** private event handling ***

	/**
	 * Label is selected.
	 *
	 * @param label
	 *            the label
	 * @return true, if successful
	 */
	private boolean labelIsSelected(Label label)
	{
		return label.getStyleName().contains("selected");
	}

	/**
	 * Make all labels selected.
	 */
	private void makeAllLabelsSelected()
	{
		for (int i = 0; i < getItemCount(); i++)
		{
			Label labelAtIndex = getLabelAtIndex(i);
			makeLabelSelected(labelAtIndex);
		}
	}

	/**
	 * Make all labels unselected.
	 */
	private void makeAllLabelsUnselected()
	{
		for (int i = 0; i < getItemCount(); i++)
		{
			Label labelAtIndex = getLabelAtIndex(i);
			makeLabelUnselected(labelAtIndex);
		}
	}

	/**
	 * Make label selected.
	 *
	 * @param label
	 *            the label
	 */
	private void makeLabelSelected(Label label)
	{
		label.setStyleDependentName("selected", true);
	}

	/**
	 * Make labels unselected except for.
	 *
	 * @param label
	 *            the label
	 */
	private void makeLabelsUnselectedExceptFor(Label label)
	{
		for (int i = 0; i < getItemCount(); i++)
		{
			Label labelAtIndex = getLabelAtIndex(i);
			if (labelAtIndex != label)
			{
				makeLabelUnselected(labelAtIndex);
			}
		}
	}

	/**
	 * Make label unselected.
	 *
	 * @param label
	 *            the label
	 */
	private void makeLabelUnselected(Label label)
	{
		label.removeStyleDependentName("selected");
	}

	/**
	 * Removes the all widgets.
	 */
	private void removeAllWidgets()
	{
		this.clear();
	}

	/**
	 * Item clicked.
	 *
	 * @param label
	 *            the label
	 */
	protected void itemClicked(Label label)
	{
		if (!this.allowsSelection)
		{
			return;
		}

		if (!labelIsSelected(label))
		{
			makeLabelSelected(label);
		}
		else
		{
			makeLabelUnselected(label);
		}
		if (!multipleSelect)
		{
			makeLabelsUnselectedExceptFor(label);
		}
	}

}
