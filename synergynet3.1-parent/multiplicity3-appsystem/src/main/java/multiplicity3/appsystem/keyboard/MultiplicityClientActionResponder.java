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

public class MultiplicityClientActionResponder  implements ActionListener {
	private static final Logger log = Logger.getLogger(MultiplicityClientActionResponder.class.getName());
	
	private IStage stage;
	private Map<String,Callable<Void>> actions = new HashMap<String,Callable<Void>>();

	public MultiplicityClientActionResponder(InputManager inputManager, IStage stage) {
		this.stage = stage;

		addPrintStageMapping(inputManager);
		addPrintJMEMapping(inputManager);
	}

	private void addPrintStageMapping(InputManager inputManager) {
		inputManager.addMapping("M3_PRINT_STAGE", new KeyTrigger(KeyInput.KEY_D));
		
		actions.put("M3_PRINT_STAGE", new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				ContentSystemPrinter.logContentSystemByZOrderFromRoot(log, Level.INFO, MultiplicityClientActionResponder.this.stage);
				return null;
			}			
		});
	}
	
	private void addPrintJMEMapping(InputManager inputManager) {
		inputManager.addMapping("M3_PRINT_JME", new KeyTrigger(KeyInput.KEY_J));
		
		actions.put("M3_PRINT_JME", new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				ContentSystemPrinter.logJMEStructure(log, Level.INFO, MultiplicityClientActionResponder.this.stage.getManipulableSpatial());
				return null;
			}			
		});
	}

	@Override
	public void onAction(String actionName, boolean valuePresent, float timePerFrame) {
		if(!valuePresent) return;

		Callable<Void> callableAction = actions.get(actionName);
		if(callableAction != null) {
			try {
				callableAction.call();
			}catch(Exception ex) {
				log.log(Level.WARNING, "callable action had a problem.", ex);
			}
		}
	}

	public String[] getActionNamesSupported() {
		return actions.keySet().toArray(new String[actions.keySet().size()]);
	}

}
