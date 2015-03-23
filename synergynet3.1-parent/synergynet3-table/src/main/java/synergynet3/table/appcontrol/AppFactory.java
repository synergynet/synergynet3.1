package synergynet3.table.appcontrol;

import multiplicity3.appsystem.IMultiplicityApp;

/**
 * A factory for creating App objects.
 */
public class AppFactory {

	/**
	 * Instantiate app for class name.
	 *
	 * @param className the class name
	 * @return the i multiplicity app
	 * @throws ClassNotFoundException the class not found exception
	 * @throws InstantiationException the instantiation exception
	 * @throws IllegalAccessException the illegal access exception
	 */
	public static IMultiplicityApp instantiateAppForClassName(String className)
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		@SuppressWarnings("unchecked")
		Class<? extends IMultiplicityApp> appClass = (Class<? extends IMultiplicityApp>) Class
				.forName(className);
		IMultiplicityApp app = appClass.newInstance();
		return app;
	}
}
