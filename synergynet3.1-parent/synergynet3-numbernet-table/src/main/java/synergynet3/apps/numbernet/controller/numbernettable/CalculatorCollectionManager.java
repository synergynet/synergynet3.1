package synergynet3.apps.numbernet.controller.numbernettable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import multiplicity3.csys.MultiplicityEnvironment;
import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.items.container.IContainer;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.stage.IStage;
import synergynet3.apps.numbernet.draganddrop.CalculatorDragAndDropListener;
import synergynet3.apps.numbernet.ui.calculator.Calculator;

/**
 * The Class CalculatorCollectionManager.
 */
public class CalculatorCollectionManager {

	/** The Constant log. */
	private static final Logger log = Logger
			.getLogger(CalculatorCollectionManager.class.getName());

	/** The container. */
	private IContainer container;

	/** The event processor. */
	private CalculatorEventProcessor eventProcessor;

	/** The user to calculator. */
	private Map<String, Calculator> userToCalculator;

	/** The visibility. */
	private boolean visibility = true;

	/**
	 * Instantiates a new calculator collection manager.
	 *
	 * @param container the container
	 * @param eventProcessor the event processor
	 */
	public CalculatorCollectionManager(IContainer container,
			CalculatorEventProcessor eventProcessor) {
		this.container = container;
		this.eventProcessor = eventProcessor;
		userToCalculator = new HashMap<String, Calculator>();
	}

	/**
	 * Adds the calculator.
	 *
	 * @param name the name
	 * @param x the x
	 * @param y the y
	 * @return the calculator
	 */
	public Calculator addCalculator(final String name, float x, float y) {
		final IStage stage = MultiplicityEnvironment.get().getLocalStages()
				.get(0);

		try {
			Calculator c = new Calculator(container, stage);
			c.setOwner(name);
			c.buildAndAttach();
			c.setVisibility(visibility);
			c.setRelativeLocation(x, y);
			userToCalculator.put(name, c);

			c.addCalculatorEventListener(eventProcessor);

			IItem target = c.getDragAndDropTarget();
			stage.getDragAndDropSystem().registerDragDestinationListener(
					target,
					new CalculatorDragAndDropListener(c, eventProcessor));

			// graphBuilder.registerItemForConnecting(label);

			return c;
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (ContentTypeNotBoundException e) {
			e.printStackTrace();
		} catch (NullPointerException npe) {
			npe.printStackTrace();
		}
		return null;

	}

	/**
	 * Gets the calculator for user.
	 *
	 * @param user the user
	 * @return the calculator for user
	 */
	public Calculator getCalculatorForUser(String user) {
		return userToCalculator.get(user);
	}

	/**
	 * Gets the users who have calculators.
	 *
	 * @return the users who have calculators
	 */
	public Collection<String> getUsersWhoHaveCalculators() {
		return Collections.unmodifiableCollection(userToCalculator.keySet());
	}

	/**
	 * Removes the calculator.
	 *
	 * @param name the name
	 */
	public void removeCalculator(String name) {
		Calculator c = userToCalculator.get(name);
		c.remove();
		userToCalculator.remove(name);
	}

	/**
	 * Sets the all calculators visible.
	 *
	 * @param visible the new all calculators visible
	 */
	public void setAllCalculatorsVisible(boolean visible) {
		log.fine("Setting calculator visibility: " + visible);
		visibility = visible;
		for (String user : getUsersWhoHaveCalculators()) {
			Calculator c = getCalculatorForUser(user);
			c.setVisibility(visible);
		}
	}

	/**
	 * Sets the available calculator collection.
	 *
	 * @param names the new available calculator collection
	 */
	public void setAvailableCalculatorCollection(List<String> names) {
		List<String> toRemove = new ArrayList<String>();
		for (String name : userToCalculator.keySet()) {
			if (!names.contains(name)) {
				toRemove.add(name);
			}
		}
		for (String name : toRemove) {
			removeCalculator(name);
		}

		List<String> toAdd = new ArrayList<String>();
		for (String n : names) {
			if (!userToCalculator.containsKey(n)) {
				toAdd.add(n);
			}
		}
		for (String name : toAdd) {
			addCalculator(name, 0, 0);
		}
	}

}
