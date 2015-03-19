package synergynet3.table.appcontrol;

import multiplicity3.appsystem.IMultiplicityApp;

public class AppFactory {
	public static IMultiplicityApp instantiateAppForClassName(String className) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		@SuppressWarnings("unchecked")		
		Class<? extends IMultiplicityApp> appClass = (Class<? extends IMultiplicityApp>) Class.forName(className);
		IMultiplicityApp app = appClass.newInstance();
		return app;
	}
}
