package multiplicity3.input;

import multiplicity3.input.filters.IMultiTouchInputFilter;

public interface IMultiTouchEventProducer {
	public void registerMultiTouchEventListener(IMultiTouchEventListener listener);	
	public void registerMultiTouchEventListener(IMultiTouchEventListener listener, int index);	
	public void registerMultiTouchExceptionListener(IDispatchedMultiTouchEventExceptionListener listener);
	public void unregisterMultiTouchEventListener(IMultiTouchEventListener listener);
	public void addMultiTouchInputFilter(IMultiTouchInputFilter filter);	
	public boolean isFilterActive(Class<? extends IMultiTouchInputFilter> filter);
	public IMultiTouchInputFilter getLastFilter();
}
