package synergynet3.apps.numbernet.model;

public class ExpressionSessionScores {
	private int correct = 0;
	private int incorrect = 0;

	public ExpressionSessionScores(int correct, int incorrect) {
		this.correct = correct;
		this.incorrect = incorrect;
	}

	public int getCorrect() {
		return correct;
	}

	public int getIncorrect() {
		return incorrect;
	}
}