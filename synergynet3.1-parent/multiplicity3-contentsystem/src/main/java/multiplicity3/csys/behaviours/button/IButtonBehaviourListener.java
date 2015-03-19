package multiplicity3.csys.behaviours.button;

import multiplicity3.csys.items.item.IItem;

public interface IButtonBehaviourListener {
	public void buttonPressed(IItem item);
	public void buttonClicked(IItem item);
	public void buttonReleased(IItem item);
}
