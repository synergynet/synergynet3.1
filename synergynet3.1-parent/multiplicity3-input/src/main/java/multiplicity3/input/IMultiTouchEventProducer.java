package multiplicity3.input;

import multiplicity3.input.filters.IMultiTouchInputFilter;

/**
 * The Interface IMultiTouchEventProducer.
 */
public interface IMultiTouchEventProducer {

	/**
	 * Adds the multi touch input filter.
	 *
	 * @param filter the filter
	 */
	public void addMultiTouchInputFilter(IMultiTouchInputFilter filter);

	/**
	 * Gets the last filter.
	 *
	 * @return the last filter
	 */
	public IMultiTouchInputFilter getLastFilter();

	/**
	 * Checks if is filter active.
	 *
	 * @param filter the filter
	 * @return true, if is filter active
	 */
	public boolean isFilterActive(Class<? extends IMultiTouchInputFilter> filter);

	/**
	 * Register multi touch event listener.
	 *
	 * @param listener the listener
	 */
	public void registerMultiTouchEventListener(
			IMultiTouchEventListener listener);

	/**
	 * Register multi touch event listener.
	 *
	 * @param listener the listener
	 * @param index the index
	 */
	public void registerMultiTouchEventListener(
			IMultiTouchEventListener listener, int index);

	/**
	 * Register multi touch exception listener.
	 *
	 * @param listener the listener
	 */
	public void registerMultiTouchExceptionListener(
			IDispatchedMultiTouchEventExceptionListener listener);

	/**
	 * Unregister multi touch event listener.
	 *
	 * @param listener the listener
	 */
	public void unregisterMultiTouchEventListener(
			IMultiTouchEventListener listener);
}
