package synergynet3.apps.numbernet.ui.calculator;

public interface ICalculatorEventListener {
	public void enterKeyPressed(Calculator calculator, String currentDisplay);
	public void characterAdded(char character, Calculator calculator, String currentDisplay);
	public void characterRemoved(Calculator calculator, String text);
}
