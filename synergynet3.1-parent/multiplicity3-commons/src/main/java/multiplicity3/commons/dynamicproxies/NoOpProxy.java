package multiplicity3.commons.dynamicproxies;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class NoOpProxy implements InvocationHandler {
	
	public static Object newInstance(Class<?> interfaceClass) {
		ClassLoader loader = NoOpProxy.class.getClassLoader();
		Class<?>[] interfaces = { interfaceClass };
		Object proxy = java.lang.reflect.Proxy.newProxyInstance(loader, interfaces, new NoOpProxy());
		return proxy;
	}

	@Override
	public Object invoke(Object onObject, Method method, Object[] arguments)
			throws Throwable {
		return null;
	}
}
