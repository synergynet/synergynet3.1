package synergynet3.museum.table.mainapp.ptmViewer;

import com.jme3.math.Vector2f;

import jpview.gui.PTMCanvas;
import jpview.ptms.PTM;
import jpview.transforms.PixelTransformOp;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.stage.IStage;
import multiplicity3.input.MultiTouchEventAdapter;
import multiplicity3.input.events.MultiTouchCursorEvent;

public class PTMListener extends MultiTouchEventAdapter {

	private PTM ptm;
	private PixelTransformOp pixelTransformOp;
	private PTMCanvas canvas; 
	private IStage stage;	
	private IItem parentItem;
	private PTMEventDispatcher ptmEventDispatcher;
	private float width, height;
	
	public void setValues(PTM ptm, PixelTransformOp pixelTransformOp, PTMCanvas canvas, float width, float height, IStage stage, IItem parentItem, PTMEventDispatcher ptmEventDispatcher){
		this.ptm = ptm;
		this.pixelTransformOp = pixelTransformOp;
		this.canvas = canvas;
		this.width = width;
		this.height = height;
		this.stage = stage;
		this.parentItem = parentItem;
		this.ptmEventDispatcher = ptmEventDispatcher;
	}
	
	@Override
	public void cursorChanged(MultiTouchCursorEvent event) {		
		
		//localisation transformation
		Vector2f newPos = event.getPosition();
		newPos = stage.tableToWorld(newPos);
		newPos = newPos.subtract(parentItem.getWorldLocation());	
		newPos = newPos.mult(1/parentItem.getRelativeScale());
		newPos.rotateAroundOrigin(-parentItem.getRelativeRotation(), false);
		newPos = newPos.add(new Vector2f(width/2, height/2));		
		newPos = newPos.mult(canvas.getDisplayWidth()/width);		

		pixelTransformOp.transformPixels(canvas.getPixels(), ptm, Math.round(newPos.x), Math.round(newPos.y));
		canvas.paintImmediately(0, 0, canvas.getDisplayWidth(), canvas.getDisplayHeight());
	
		ptmEventDispatcher.generateImage();
	
	}
	
}