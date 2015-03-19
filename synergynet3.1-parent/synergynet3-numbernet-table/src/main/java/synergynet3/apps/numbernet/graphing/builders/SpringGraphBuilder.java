package synergynet3.apps.numbernet.graphing.builders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.factory.IContentFactory;
import multiplicity3.csys.items.container.IContainer;
import multiplicity3.csys.items.events.IItemListener;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.stage.IStage;
import multiplicity3.input.events.MultiTouchCursorEvent;
import synergynet3.additionalitems.interfaces.ICachableLine;
import synergynet3.apps.numbernet.model.ExpressionSession;
import synergynet3.apps.numbernet.model.IExpressionSessionChangeListener;
import synergynet3.apps.numbernet.ui.expression.ExpressionDisplay;
import synergynet3.cluster.sharedmemory.DistributedMap;
import synergynet3.projector.network.ProjectorTransferUtilities;
import synergynet3.web.apps.numbernet.comms.table.TargetMaps;
import synergynet3.web.apps.numbernet.shared.Edge;
import synergynet3.web.apps.numbernet.shared.Expression;
import synergynet3.web.apps.numbernet.shared.Graph;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;


public class SpringGraphBuilder implements IItemListener, IExpressionSessionChangeListener {
	private static final Logger log = Logger.getLogger(SpringGraphBuilder.class.getName());

	private List<IItem> registeredItems = new ArrayList<IItem>();
	private float distanceForConnection;
	private float distanceForBreak;

	private IContentFactory contentFactory;
	private Graph linkingGraph;
	private ColorRGBA lineColour = new ColorRGBA(1f, 1f, 1f, 0.4f);
	private Vector2f lineShift;
	//	private boolean active = true;
	//	private SpringLayout layout;

	private ExpressionSession session;
	private ExpressionDisplay expressionDisplay;
	
	
	private Map<String,ICachableLine> edgeToLine = new HashMap<String,ICachableLine>();

	private double target;

	private IContainer graphingLinesContainer;

	/**
	 * 
	 * @param expressionSession 
	 * @param app
	 * @param distanceForConnection
	 * @param distanceForBreak
	 */
	public SpringGraphBuilder(IStage stage, float distanceForConnection, float distanceForBreak, float desiredLineLength) {
		log.fine("Creating a Spring Graph Builder");
		this.contentFactory = stage.getContentFactory();
		this.distanceForConnection = distanceForConnection;
		this.distanceForBreak = distanceForBreak;
		linkingGraph = new Graph();
		//layout = new SpringLayout(stage, linkingGraph, desiredLineLength);

		// shift lines so that they appear properly
		lineShift = new Vector2f(0, 0);
		//-ContentSystem.getContentSystem().getDisplayManager().getDisplayWidth() / 2,
		//-ContentSystem.getContentSystem().getDisplayManager().getDisplayHeight() / 2);

	}

	public void setActive(boolean active, double forTarget) {
		if(active) {
			this.target = forTarget;
			registerAllCurrentlyOnScreenItemsForConnecting();
			buildGraphFromNetwork();
			try {
				buildLinesFromGraph();
			} catch (ContentTypeNotBoundException e) {
				e.printStackTrace();
			}
		}else{
			unregisterAllItems();
			removeAllLines();
		}
	}

	private void buildGraphFromNetwork() {
		log.fine("Finding edges on network...");
		DistributedMap<String,Edge> edges = TargetMaps.get().getEdgesMapForTarget(target);
		log.fine("Found " + edges.size() + " edges.");
		for(Edge e : edges.values()) {
			linkingGraph.forceConnect(e.a.getID(), e.b.getID());
		}
	}

	private void buildLinesFromGraph() throws ContentTypeNotBoundException {
		log.info("Creating visible edges...");
		for(Edge e : linkingGraph.getEdges()) {
			log.fine("Adding visible line for " + e.getKey());
			addLine(e);
		}
	}

	private void removeAllLines() {
		log.info("Removing " + edgeToLine.size() + " lines");
		for(ICachableLine line : edgeToLine.values()) {
			graphingLinesContainer.removeItem(line);
			ProjectorTransferUtilities.get().removeFromTransferableContents(line);
		}
		edgeToLine.clear();
	}

	public void setLineColour(ColorRGBA lineColour) {
		this.lineColour = lineColour;
	}

	public ColorRGBA getLineColour() {
		return lineColour;
	}

	/**
	 * Will only try and connect together those items
	 * that are registered.
	 * @param item
	 */
	public void registerItemForConnecting(IItem item) {		
		if(!registeredItems.contains(item)) {
			log.fine("Registering item for connecting: " + item.getClass().getName());
			this.registeredItems.add(item);
			item.addItemListener(this);
		}else{
			log.fine("Did not register item for connecting: " + item.getClass().getName());
		}
	}

	/**
	 * Unregister an item - will ensure that it will no longer
	 * be able to be connected.
	 * @param item
	 */
	public void unregisterItemForConnecting(IItem item) {
		item.removeItemListener(this);
		registeredItems.remove(item);		
	}

	public void unregisterAllItems() {
		for(IItem item : registeredItems) {
			item.removeItemListener(this);
		}
		registeredItems.clear();
	}

	@Override
	public void itemCursorPressed(IItem item, MultiTouchCursorEvent event) {
		if(!item.isVisible()) return;
		linkingGraph.pressedOn(item.getUUID().toString());
	}

	@Override
	public void itemCursorReleased(IItem item, MultiTouchCursorEvent event) {
		linkingGraph.releasedOn(item.getUUID().toString());
	}

	@Override
	public void itemMoved(IItem item) {
		try {
			connectOrBreak(item);
			Vector2f coords = item.getWorldLocation();
			linkingGraph.getNodeForItem(item.getUUID().toString()).setPosition(coords.x, coords.y);
		} catch (ContentTypeNotBoundException e) {
			e.printStackTrace();
		}
	}

	// ********* unneeded methods ********

	@Override
	public void itemCursorChanged(IItem item, MultiTouchCursorEvent event) {} // unneeded

	@Override
	public void itemCursorClicked(IItem item, MultiTouchCursorEvent event) {} // unneeded

	@Override
	public void itemRotated(IItem item) {} // unneeded

	@Override
	public void itemScaled(IItem item) {} // unneeded

	@Override
	public void itemZOrderChanged(IItem item) {} // unneeded

	// ********** private methods *********

	private void connectOrBreak(IItem item) throws ContentTypeNotBoundException {
		IItem closest = findClosestItemTo(item, distanceForConnection);		
		if(closest != null) {
			if(!closest.isVisible()) {
				return;
			}
			
			if(!linkingGraph.isConnected(item.getUUID().toString(), closest.getUUID().toString())) {
				Edge e = linkingGraph.connect(item.getUUID().toString(), closest.getUUID().toString());
				if(e != null) {					
					addLine(e);
				}
			}
		}else{
			List<Edge> linesToRemove = linkingGraph.getEdgesLongerThanDistanceIgnoringCurrentlyInteractingEdges(distanceForBreak);
			linkingGraph.removeEdges(linesToRemove);

			for(Edge e : linesToRemove) {
				ICachableLine line = getLineForEdge(e);
				graphingLinesContainer.removeItem(line);
				ProjectorTransferUtilities.get().removeFromTransferableContents(line);
				DistributedMap<String,Edge> distmap = TargetMaps.get().getEdgesMapForTarget(target);
				distmap.remove(e.getKey());
			}			
			
		}
	}
	


	private void addLine(Edge e) throws ContentTypeNotBoundException {
		DistributedMap<String,Edge> distmap = TargetMaps.get().getEdgesMapForTarget(target);
		distmap.put(e.getKey(), e);
		
		IItem item = getItemByID(e.a.getID());
		IItem closest = getItemByID(e.b.getID());
		if(item == null || closest == null) {
			log.warning("Could not find items for edge");
			log.warning("Will not be able to add a line.");
			return;
		}
		ICachableLine linkLine = contentFactory.create(ICachableLine.class, item.getUUID() + " to " + closest.getUUID(), UUID.randomUUID());
		linkLine.setLineWidth(4f);
		linkLine.setLineColour(getLineColour() );
		linkLine.setSourceItem(item);
		linkLine.setDestinationItem(closest);
		linkLine.setLineVisibilityChangesWithItemVisibility(true);
		linkLine.setInteractionEnabled(false);

		linkLine.setRelativeLocation(lineShift);
		graphingLinesContainer.addItem(linkLine);
		graphingLinesContainer.getZOrderManager().sendToBottom(linkLine);
		associateLineWithEdge(e, linkLine);
		
		item.setWorldLocation(item.getWorldLocation()); // force line to update
		closest.setWorldLocation(closest.getWorldLocation()); // force line to update
		
		ProjectorTransferUtilities.get().addToTransferableContents(linkLine, 4f, 4f, item.getUUID() + " to " + closest.getUUID());
		
		//app.getZOrderManager().registerForZOrdering(linkLine);
		//app.getZOrderManager().bringToTop(item, null);
		//app.getZOrderManager().bringToTop(closest, null);

	}

	private IItem getItemByID(String id) {
		for(IItem item : registeredItems) {
			if(item.getUUID().toString().equals(id)) {
				return item;
			}
		}
		return null;
	}

	private ICachableLine getLineForEdge(Edge e) {
		return edgeToLine.get(e.getKey());
	}
	
	private void associateLineWithEdge(Edge e, ICachableLine line) {
		edgeToLine.put(e.getKey(), line);
	}

	private IItem findClosestItemTo(IItem item, float maxDistance) {
		float minDist = Float.MAX_VALUE;
		IItem closest = null;
		for(IItem x : registeredItems) {
			if(item == x) continue;
			float distBetween = item.getRelativeLocation().distance(x.getRelativeLocation());
			if(distBetween < minDist && distBetween <= maxDistance) {
				closest = x;
				minDist = distBetween;
			}
		}
		return closest;
	}

	public void setExpressionSessionAndDisplay(ExpressionSession session, ExpressionDisplay expressionDisplay, IContainer graphingLinesContainer) {
		this.graphingLinesContainer = graphingLinesContainer;
		if(this.session != null) {
			this.session.removeChangeListener(this);
		}

		this.session = session;
		this.session.addChangeListener(this);
		this.expressionDisplay = expressionDisplay;
	}

	public void registerAllCurrentlyOnScreenItemsForConnecting() {
		log.info("Registering items for connecting...");
		for(IContainer container : expressionDisplay.getAllItems()) {
			log.info("  item " + container);
			registerItemForConnecting(container);
		}
	}

	@Override
	public void expressionAddedFromCalculator(Expression e) {
		log.info("SGB registering latest expression " + e.getExpression());
		IContainer container = expressionDisplay.getExpressionContainerForExpression(e);
		if(container == null) {
			log.warning("Could not get an expression visual for the expression just added.");
			return;
		}
		registerItemForConnecting(container);
	}

	@Override
	public void expressionAddedFromNetwork(Expression e) {}

	@Override
	public void expressionRemoved(Expression expression) {}

	@Override
	public void allExpressionsRemoved() {}

	@Override
	public void targetChanged(Double newValue) {}

	@Override
	public void itemVisibilityChanged(IItem item, boolean isVisible) {}


}
