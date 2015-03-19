package synergynet3.web.apps.numbernet.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public enum CalculatorKey implements IsSerializable {
	PLUS		("+"), 
	MINUS		("-"), 
	DIVIDE		("\u00F7"), 
	MULTIPLY	("\u00D7"),
	NINE		("9"), 
	EIGHT		("8"), 
	SEVEN		("7"), 
	SIX			("6"), 
	FIVE		("5"), 
	FOUR		("4"), 
	THREE		("3"), 
	TWO			("2"), 
	ONE			("1"), 
	ZERO		("0"),
	POINT		("."),
	LEFTBRACKET		("("), 
	RIGHTBRACKET	(")");
	
	private String stringRepresentation;

	CalculatorKey(String stringRepresentation) {
		this.stringRepresentation = stringRepresentation;
	}

	public String getStringRepresentation() {
		return stringRepresentation;
	}
	
	
}
