package synergynet3.museum.table.mainapp;

import java.util.ArrayList;

import multiplicity3.csys.items.image.IImage;
import multiplicity3.csys.items.line.ILine;

public class LensVisiblePOI {
	
	private float x = 0;
	private float y = 0;
	private String lenseValues;
	private IImage poi;
	private ILine line;
	
	private ArrayList<Lens> lenses = new ArrayList<Lens>();
	
	public LensVisiblePOI(String lenseValues, IImage poi, ILine line, float x, float y){
		this.lenseValues = lenseValues;
		this.poi = poi;
		this.line = line;
		this.x = x;
		this.y = y;
	}
	
	public void update(Lens lens) { 
		
		if (lens.isWithinFilter(poi.getRelativeLocation())){ 
			if (lenseValues.equals(lens.getLensValue())){
				canBeSeenByLens(lens);
			}else{
				cannotBeSeenByLens(lens);
			}
		}else{
			if (lenseValues.equals(lens.getLensValue())){
				cannotBeSeenByLens(lens);				
			}else{
				cannotBeSeenByLens(lens);				
			}
		}	
	}
	
	private void canBeSeenByLens(Lens lens){
		if (!lenses.contains(lens)){
			if (lenses.size() == 0){
				poi.setVisible(true);	
				if (!line.getSourceItem().isVisible()){
					line.setVisible(false);
				}
			}
			lenses.add(lens);
		}
	}
	
	private void cannotBeSeenByLens(Lens lens){
		if (lenses.contains(lens)){
			lenses.remove(lens);
		}
		if (lenses.size() == 0){
			poi.setVisible(false);
		}
	}

	public void removeLens(Lens lens) {
		if (lenses.contains(lens)){
			lenses.remove(lens);
		}
		if (lenses.size() == 0){
			poi.setVisible(false);
		}
	}
		
	/**
	 * @return the poi
	 */
	public IImage getPoi() {
		return poi;
	}
	
	/**
	 * @param poi the poi to set
	 */
	public void setPoi(IImage poi) {
		this.poi = poi;
	}

	/**
	 * @return the x
	 */
	public float getX() {
		return x;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(float x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public float getY() {
		return y;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(float y) {
		this.y = y;
	}

}
