package multiplicity3.csys.factory;

import multiplicity3.csys.items.item.IItem;

public class ContentTypeAlreadyBoundException extends Exception {
	private static final long serialVersionUID = 7555560238063836362L;
	
	private Class<? extends IItem> alreadyBoundTo;

	public ContentTypeAlreadyBoundException(
			Class<? extends IItem> alreadyBoundTo) {
		this.setAlreadyBoundTo(alreadyBoundTo);
	}

	public void setAlreadyBoundTo(Class<? extends IItem> alreadyBoundTo) {
		this.alreadyBoundTo = alreadyBoundTo;
	}

	public Class<? extends IItem> getAlreadyBoundTo() {
		return alreadyBoundTo;
	}

	public String toString() {
		return super.toString() + " already bound to " + alreadyBoundTo.getName();
	}	
}
