package synergynet3.apps.numbernet.ui.projection.expressions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.items.container.IContainer;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.items.line.ILine;
import multiplicity3.csys.stage.IStage;
import synergynet3.apps.numbernet.ui.expression.ExpressionVisualRepresentationFactory;
import synergynet3.cluster.sharedmemory.DistributedMap;
import synergynet3.web.apps.numbernet.comms.table.TargetMaps;
import synergynet3.web.apps.numbernet.comms.teachercontrol.TeacherControlComms;
import synergynet3.web.apps.numbernet.shared.Edge;
import synergynet3.web.apps.numbernet.shared.Expression;
import synergynet3.web.apps.numbernet.shared.ExpressionVisibleProperties;
import synergynet3.web.apps.numbernet.shared.TableTarget;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;

public class ProjectExpressionsUI  {

	private static final Logger log = Logger.getLogger(ProjectExpressionsUI.class.getName());
	private ExpressionVisualRepresentationFactory expressionVisualFactory;
	private Map<String,IContainer> expressionVisualMap;
	private IStage stage;
	private double target;
	private boolean unifyRotation;
	//private SpringGraphBuilder graphBuilder;
	private String tableWithTarget;
	private IContainer linesContainer;
	private IContainer expressionsContainer;

	public ProjectExpressionsUI(IStage stage) {
		log.info("Creating projector UI");		
		this.stage = stage;
		//this.graphBuilder = new SpringGraphBuilder(stage, 100, 200, 20);
		expressionVisualMap = new HashMap<String,IContainer>();
		try {
			linesContainer = stage.getContentFactory().create(IContainer.class, "lines", UUID.randomUUID());
			stage.addItem(linesContainer);

			expressionsContainer = stage.getContentFactory().create(IContainer.class, "exprui", UUID.randomUUID());
			stage.addItem(expressionsContainer);			
			expressionVisualFactory = new ExpressionVisualRepresentationFactory(expressionsContainer, this.stage);

			stage.getZOrderManager().sendToBottom(linesContainer);
		} catch (ContentTypeNotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setVisibility(boolean visible) {
		this.expressionsContainer.setVisible(visible);
	}

	public void clearDisplay() {
		log.fine("Clearing display");
		expressionsContainer.removeAllItems(true);		
		expressionVisualMap.clear();
		linesContainer.removeAllItems(true);
	}

	public void displayTarget(Double newValue) throws ContentTypeNotBoundException {
		log.info("Now display map: " + newValue);
		if(newValue == null) {
			clearDisplay();
			return;
		}
		if(newValue.isNaN()) {
			clearDisplay();
			return;
		}
		this.target = newValue;
		this.tableWithTarget = getTableForTarget(target);
		clearDisplay();
		addExpressionVisualsForAllItemsOfTarget(newValue);
		addLinkingLinesForTarget(newValue);
	}

	private String getTableForTarget(double target) {
		String table = null;
		List<TableTarget> tts = TeacherControlComms.get().getTableTargets();
		for(TableTarget tt : tts) {
			if(tt.getTarget() == target) {
				return tt.getTable();
			}
		}
		return table;
	}

	private void addLinkingLinesForTarget(double target) throws ContentTypeNotBoundException {
		for(Edge e : TargetMaps.get().getEdgesMapForTarget(target).values()) {
			addLinkingLineForEdge(e);
		}
	}

	private void addLinkingLineForEdge(Edge e) throws ContentTypeNotBoundException {
		IItem item = getItemForID(e.a.getID());
		IItem closest = getItemForID(e.b.getID());
		ILine linkLine = stage.getContentFactory().create(ILine.class, item.getUUID() + " to " + closest.getUUID(), UUID.randomUUID());
		linkLine.setLineWidth(4f);
		linkLine.setLineColour(new ColorRGBA(1f, 1f, 1f, 0.4f));
		linkLine.setSourceItem(item);
		linkLine.setDestinationItem(closest);
		linkLine.setLineVisibilityChangesWithItemVisibility(true);
		linesContainer.addItem(linkLine);

		item.setWorldLocation(item.getWorldLocation()); // force line to update
		closest.setWorldLocation(closest.getWorldLocation()); // force line to update
	}

	private IItem getItemForID(String id) {
		return expressionVisualMap.get(id);
	}

	private void addExpressionVisualsForAllItemsOfTarget(double target) throws ContentTypeNotBoundException {
		DistributedMap<String, Expression> expressionMap = TargetMaps.get().getDistributedMapForTarget(target);
				
		Boolean hideCorrect = TeacherControlComms.get().getStudentTableDeviceForName(tableWithTarget).getCorrectExpressionsVisibleControlVariable().getValue();		
		Boolean hideIncorrect = TeacherControlComms.get().getStudentTableDeviceForName(tableWithTarget).getIncorrectExpressionsVisibleControlVariable().getValue();
		Boolean hideOthersCorrect = TeacherControlComms.get().getStudentTableDeviceForName(tableWithTarget).getOthersCorrectExpressionsVisibleControlVariable().getValue();
		Boolean hideOthersIncorrect = TeacherControlComms.get().getStudentTableDeviceForName(tableWithTarget).getOthersIncorrectExpressionsVisibleControlVariable().getValue();
		
		boolean bShowCorrect = hideCorrect != null? hideCorrect : true;
		boolean bShowIncorrect = hideIncorrect != null? hideIncorrect : true;
		boolean bShowOthersCorrect = hideOthersCorrect != null? hideOthersCorrect : true;
		boolean bShowOthersIncorrect = hideOthersIncorrect != null? hideOthersIncorrect : true;
		
		log.info("Doing hiding based on table: " + tableWithTarget);
		log.info("show correct? " + bShowCorrect);
		log.info("show incorrect? " + bShowIncorrect);
		log.info("show others correct? " + bShowOthersCorrect);
		log.info("show others incorrect? " + bShowOthersIncorrect);
		
		for(Expression e : expressionMap.values()) {
			
			boolean isOwnExpression = e.getCreatedOnTable().equals(this.tableWithTarget);
			boolean isCorrect = e.isCorrect();
			
			if(isOwnExpression) {
				if(isCorrect) {
					if(bShowCorrect) {
						doAddExpression(e, target);			
					}
				}else{
					if(bShowIncorrect) {
						doAddExpression(e, target);
					}
				}
			}else{
				if(isCorrect) {
					if(bShowOthersCorrect) {
						doAddExpression(e, target);
					}
				}else{
					if(bShowOthersIncorrect) {
						doAddExpression(e, target);
					}
				}
			}			
		}
	}

	private void doAddExpression(Expression e, double target) throws ContentTypeNotBoundException {
		log.info("Adding expression " + e.getExpression());
		IContainer expressionVisual = expressionVisualFactory.createExpressionVisualRepresentation(e, 0, new Vector2f(0,0));
		//stage.addItem(expressionVisual);
		updateExpressionVisualWithDistributedVisualProperties(e, expressionVisual, target);
		expressionVisualMap.put(e.getId(), expressionVisual);
	}

	private void updateExpressionVisualWithDistributedVisualProperties(Expression e,
			IContainer expressionVisual, double target) {
		DistributedMap<String, ExpressionVisibleProperties> evpmap = TargetMaps.get().getExpressionVisiblePropertiesMapForTarget(target);
		ExpressionVisibleProperties evp = evpmap.get(e.getId());
		if(evp != null) {
			expressionVisual.setWorldLocation(new Vector2f(evp.x, evp.y));
			if(!unifyRotation) {
				expressionVisual.setRelativeRotation(evp.rot);
			}
		}
	}

	public void updateDistributedPositionData() {
		DistributedMap<String, ExpressionVisibleProperties> evpmap = TargetMaps.get().getExpressionVisiblePropertiesMapForTarget(target);

		for(String key : expressionVisualMap.keySet()) {
			IContainer container = expressionVisualMap.get(key);
			ExpressionVisibleProperties props = new ExpressionVisibleProperties();
			props.id = key;
			props.x = container.getWorldLocation().x;
			props.y = container.getWorldLocation().y;
			props.rot = container.getRelativeRotation();
			evpmap.put(key, props);
		}

		log.info("Projector has updated position info.");
	}

	public void setUnifyRotationMode(boolean unifyRotation) {		
		this.unifyRotation = unifyRotation;
		if(unifyRotation) {
			log.info("Unifying rotation");
			for(IItem item : expressionsContainer.getChildItems()) {
				if(item instanceof ILine) {
					// ignore
				}else{
					log.info("Rotating an item to 0");
					item.setRelativeRotation(0);
				}
			}
		}
	}
}
