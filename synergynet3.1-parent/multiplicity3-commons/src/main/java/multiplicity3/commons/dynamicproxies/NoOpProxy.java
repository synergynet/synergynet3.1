package multiplicity3.commons.dynamicproxies;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * The Class NoOpProxy.
 */
public class NoOpProxy implements InvocationHandler {

	/**
	 * New instance.
	 *
	 * @param interfaceClass the interface class
	 * @return the object
	 */
	public static Object newInstance(Class<?> interfaceClass) {
		ClassLoader loader = NoOpProxy.class.getClassLoader();
		Class<?>[] interfaces = { interfaceClass };
		Object proxy = java.lang.reflect.Proxy.newProxyInstance(loader,
				interfaces, new NoOpProxy());
		return proxy;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object,
	 * java.lang.reflect.Method, java.lang.Object[])
	 */
	@Override
	public Object invoke(Object onObject, Method method, Object[] arguments)
			throws Throwable {
		return null;
	}
}
