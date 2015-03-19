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

public class CalculatorCollectionManager {

	private static final Logger log = Logger.getLogger(CalculatorCollectionManager.class.getName());

	private Map<String, Calculator> userToCalculator;
	private boolean visibility = true;

	private CalculatorEventProcessor eventProcessor;

	private IContainer container;

	public CalculatorCollectionManager(IContainer container, CalculatorEventProcessor eventProcessor) {
		this.container = container;
		this.eventProcessor = eventProcessor;
		userToCalculator = new HashMap<String,Calculator>();
	}

	public Calculator addCalculator(final String name, float x, float y) {
		final IStage stage = MultiplicityEnvironment.get().getLocalStages().get(0);

		try {
			Calculator c = new Calculator(container, stage);
			c.setOwner(name);
			c.buildAndAttach();
			c.setVisibility(visibility);
			c.setRelativeLocation(x,y);
			userToCalculator.put(name, c);

			c.addCalculatorEventListener(eventProcessor);

			IItem target = c.getDragAndDropTarget();
			stage.getDragAndDropSystem().registerDragDestinationListener(target, new CalculatorDragAndDropListener(c, eventProcessor));

			//graphBuilder.registerItemForConnecting(label);

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

	public void setAllCalculatorsVisible(boolean visible) {
		log.fine("Setting calculator visibility: " + visible);
		visibility = visible;
		for(String user : getUsersWhoHaveCalculators()) {
			Calculator c = getCalculatorForUser(user);
			c.setVisibility(visible);
		}
	}

	public void removeCalculator(String name) {
		Calculator c = userToCalculator.get(name);
		c.remove();
		userToCalculator.remove(name);
	}

	public Collection<String> getUsersWhoHaveCalculators() {
		return Collections.unmodifiableCollection(userToCalculator.keySet());
	}

	public Calculator getCalculatorForUser(String user) {
		return userToCalculator.get(user);
	}

	public void setAvailableCalculatorCollection(List<String> names) {
		List<String> toRemove = new ArrayList<String>();
		for(String name : userToCalculator.keySet()) {
			if(!names.contains(name)) {
				toRemove.add(name);
			}
		}
		for(String name : toRemove) {
			removeCalculator(name);
		}

		List<String> toAdd = new ArrayList<String>();
		for(String n : names) {
			if(!userToCalculator.containsKey(n)) {
				toAdd.add(n);
			}
		}
		for(String name : toAdd) {
			addCalculator(name, 0, 0);
		}		
	}

}
