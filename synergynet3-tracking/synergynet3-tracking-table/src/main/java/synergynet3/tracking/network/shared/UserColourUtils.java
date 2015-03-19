package synergynet3.tracking.network.shared;

import java.awt.Color;

import multiplicity3.csys.gfx.ColourUtils;

import com.jme3.math.ColorRGBA;

public class UserColourUtils {
	
	private static final Color[] COLOURS = {Color.RED, Color.BLUE, Color.CYAN, Color.GREEN, Color.MAGENTA, Color.PINK, 
		Color.YELLOW, new Color(0, 100, 0), new Color(139, 0, 204), new Color(255, 90, 0)};
	private static ColorRGBA[] coloursRGBA = null;

	private static ColorRGBA[] getColoursRGBA(){
		if (coloursRGBA == null){
			coloursRGBA = new ColorRGBA[COLOURS.length];
			for (int i = 0; i < COLOURS.length; i++){
				coloursRGBA[i] = ColourUtils.getColorRGBAFromColor(COLOURS[i]);
			}
		}
		return coloursRGBA;
	}
	
	public static Color getColour(int uniqueID){
		if (uniqueID == 0) return Color.white;
		uniqueID %= COLOURS.length;
		return COLOURS[uniqueID];
	}
	
	public static ColorRGBA getRGBAColour(int userID){		
		if (userID <= 0) return ColorRGBA.White;			
		userID %= getColoursRGBA().length;		
		return getColoursRGBA()[userID];		
	}
	

}
