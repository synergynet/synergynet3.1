package multiplicity3.input;

/**
 * The listener interface for receiving IDispatchedMultiTouchEventException
 * events. The class that is interested in processing a
 * IDispatchedMultiTouchEventException event implements this interface, and the
 * object created with that class is registered with a component using the
 * component's
 * <code>addIDispatchedMultiTouchEventExceptionListener<code> method. When
 * the IDispatchedMultiTouchEventException event occurs, that object's appropriate
 * method is invoked.
 *
 * @see IDispatchedMultiTouchEventExceptionEvent
 */
public interface IDispatchedMultiTouchEventExceptionListener {

	/**
	 * Dispatched multi touch event exception.
	 *
	 * @param problem the problem
	 */
	public void dispatchedMultiTouchEventException(Throwable problem);
}
