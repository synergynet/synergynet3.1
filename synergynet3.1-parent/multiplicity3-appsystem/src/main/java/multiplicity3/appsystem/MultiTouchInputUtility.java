package multiplicity3.appsystem;

import java.util.logging.Logger;

import com.jme3.input.InputManager;

import multiplicity3.config.display.DisplayPrefsItem;
import multiplicity3.input.IMultiTouchInputSource;
import multiplicity3.input.luminja.LuminMultiTouchInput;
import multiplicity3.input.simulator.jme.JMEDirectSimulator;
import multiplicity3.input.tuio.TUIOMultiTouchInput;
import multiplicity3.input.win7.Win7TouchInput;

public class MultiTouchInputUtility {
	
	private static final Logger log = Logger.getLogger(MultiTouchInputUtility.class.getName());

	public static IMultiTouchInputSource getInputSource(InputManager inputManager, int displayWidth, int displayHeight) {
		DisplayPrefsItem tablePrefs = new DisplayPrefsItem();
		String tabletype = tablePrefs.getInputType();
		IMultiTouchInputSource multiTouchInput = null;
		
		log.info("Table type: " + tabletype.toString());
		
		try {
			if (tabletype.equals(DisplayPrefsItem.INPUT_TYPES[0])){
				multiTouchInput = new JMEDirectSimulator(inputManager, displayWidth, displayHeight);
			}else if (tabletype.equals(DisplayPrefsItem.INPUT_TYPES[1])){
				multiTouchInput = new TUIOMultiTouchInput();
			}else if (tabletype.equals(DisplayPrefsItem.INPUT_TYPES[2])){
				multiTouchInput = new LuminMultiTouchInput();
			}else if (tabletype.equals(DisplayPrefsItem.INPUT_TYPES[3])){
				multiTouchInput = new Win7TouchInput(tablePrefs.getWidth(), tablePrefs.getHeight(), false);				
			}else if (tabletype.equals(DisplayPrefsItem.INPUT_TYPES[4])){
				multiTouchInput = new Win7TouchInput(tablePrefs.getWidth(), tablePrefs.getHeight(), true);				
			}

		} catch (SecurityException e) {
			e.printStackTrace();
		}


		return multiTouchInput;
	}
}
