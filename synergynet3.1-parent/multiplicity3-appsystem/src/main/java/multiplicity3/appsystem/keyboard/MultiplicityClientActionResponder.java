package multiplicity3.appsystem.keyboard;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

import multiplicity3.appsystem.tools.ContentSystemPrinter;
import multiplicity3.csys.stage.IStage;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;

/**
 * The Class MultiplicityClientActionResponder.
 */
public class MultiplicityClientActionResponder implements ActionListener {

	/** The Constant log. */
	private static final Logger log = Logger
			.getLogger(MultiplicityClientActionResponder.class.getName());

	/** The actions. */
	private Map<String, Callable<Void>> actions = new HashMap<String, Callable<Void>>();

	/** The stage. */
	private IStage stage;

	/**
	 * Instantiates a new multiplicity client action responder.
	 *
	 * @param inputManager the input manager
	 * @param stage the stage
	 */
	public MultiplicityClientActionResponder(InputManager inputManager,
			IStage stage) {
		this.stage = stage;

		addPrintStageMapping(inputManager);
		addPrintJMEMapping(inputManager);
	}

	/**
	 * Gets the action names supported.
	 *
	 * @return the action names supported
	 */
	public String[] getActionNamesSupported() {
		return actions.keySet().toArray(new String[actions.keySet().size()]);
	}

	/*
	 * (non-Javadoc)
	 * @see com.jme3.input.controls.ActionListener#onAction(java.lang.String,
	 * boolean, float)
	 */
	@Override
	public void onAction(String actionName, boolean valuePresent,
			float timePerFrame) {
		if (!valuePresent) {
			return;
		}

		Callable<Void> callableAction = actions.get(actionName);
		if (callableAction != null) {
			try {
				callableAction.call();
			} catch (Exception ex) {
				log.log(Level.WARNING, "callable action had a problem.", ex);
			}
		}
	}

	/**
	 * Adds the print jme mapping.
	 *
	 * @param inputManager the input manager
	 */
	private void addPrintJMEMapping(InputManager inputManager) {
		inputManager.addMapping("M3_PRINT_JME", new KeyTrigger(KeyInput.KEY_J));

		actions.put("M3_PRINT_JME", new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				ContentSystemPrinter.logJMEStructure(log, Level.INFO,
						MultiplicityClientActionResponder.this.stage
								.getManipulableSpatial());
				return null;
			}
		});
	}

	/**
	 * Adds the print stage mapping.
	 *
	 * @param inputManager the input manager
	 */
	private void addPrintStageMapping(InputManager inputManager) {
		inputManager.addMapping("M3_PRINT_STAGE",
				new KeyTrigger(KeyInput.KEY_D));

		actions.put("M3_PRINT_STAGE", new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				ContentSystemPrinter.logContentSystemByZOrderFromRoot(log,
						Level.INFO,
						MultiplicityClientActionResponder.this.stage);
				return null;
			}
		});
	}

}
