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

/**
 * The Class SpringGraphBuilder.
 */
public class SpringGraphBuilder implements IItemListener,
		IExpressionSessionChangeListener {

	/** The Constant log. */
	private static final Logger log = Logger.getLogger(SpringGraphBuilder.class
			.getName());

	/** The content factory. */
	private IContentFactory contentFactory;

	/** The distance for break. */
	private float distanceForBreak;

	/** The distance for connection. */
	private float distanceForConnection;

	/** The edge to line. */
	private Map<String, ICachableLine> edgeToLine = new HashMap<String, ICachableLine>();

	/** The expression display. */
	private ExpressionDisplay expressionDisplay;

	/** The graphing lines container. */
	private IContainer graphingLinesContainer;

	/** The line colour. */
	private ColorRGBA lineColour = new ColorRGBA(1f, 1f, 1f, 0.4f);

	/** The line shift. */
	private Vector2f lineShift;
	// private boolean active = true;
	// private SpringLayout layout;

	/** The linking graph. */
	private Graph linkingGraph;

	/** The registered items. */
	private List<IItem> registeredItems = new ArrayList<IItem>();

	/** The session. */
	private ExpressionSession session;

	/** The target. */
	private double target;

	/**
	 * @param expressionSession
	 * @param app
	 * @param distanceForConnection
	 * @param distanceForBreak
	 */
	public SpringGraphBuilder(IStage stage, float distanceForConnection,
			float distanceForBreak, float desiredLineLength) {
		log.fine("Creating a Spring Graph Builder");
		this.contentFactory = stage.getContentFactory();
		this.distanceForConnection = distanceForConnection;
		this.distanceForBreak = distanceForBreak;
		linkingGraph = new Graph();
		// layout = new SpringLayout(stage, linkingGraph, desiredLineLength);

		// shift lines so that they appear properly
		lineShift = new Vector2f(0, 0);
		// -ContentSystem.getContentSystem().getDisplayManager().getDisplayWidth()
		// / 2,
		// -ContentSystem.getContentSystem().getDisplayManager().getDisplayHeight()
		// / 2);

	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.apps.numbernet.model.IExpressionSessionChangeListener#
	 * allExpressionsRemoved()
	 */
	@Override
	public void allExpressionsRemoved() {
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.apps.numbernet.model.IExpressionSessionChangeListener#
	 * expressionAddedFromCalculator
	 * (synergynet3.web.apps.numbernet.shared.Expression)
	 */
	@Override
	public void expressionAddedFromCalculator(Expression e) {
		log.info("SGB registering latest expression " + e.getExpression());
		IContainer container = expressionDisplay
				.getExpressionContainerForExpression(e);
		if (container == null) {
			log.warning("Could not get an expression visual for the expression just added.");
			return;
		}
		registerItemForConnecting(container);
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.apps.numbernet.model.IExpressionSessionChangeListener#
	 * expressionAddedFromNetwork
	 * (synergynet3.web.apps.numbernet.shared.Expression)
	 */
	@Override
	public void expressionAddedFromNetwork(Expression e) {
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.apps.numbernet.model.IExpressionSessionChangeListener#
	 * expressionRemoved(synergynet3.web.apps.numbernet.shared.Expression)
	 */
	@Override
	public void expressionRemoved(Expression expression) {
	}

	/**
	 * Gets the line colour.
	 *
	 * @return the line colour
	 */
	public ColorRGBA getLineColour() {
		return lineColour;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.items.events.IItemListener#itemCursorChanged(multiplicity3
	 * .csys.items.item.IItem, multiplicity3.input.events.MultiTouchCursorEvent)
	 */
	@Override
	public void itemCursorChanged(IItem item, MultiTouchCursorEvent event) {
	} // unneeded

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.items.events.IItemListener#itemCursorClicked(multiplicity3
	 * .csys.items.item.IItem, multiplicity3.input.events.MultiTouchCursorEvent)
	 */
	@Override
	public void itemCursorClicked(IItem item, MultiTouchCursorEvent event) {
	} // unneeded

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.items.events.IItemListener#itemCursorPressed(multiplicity3
	 * .csys.items.item.IItem, multiplicity3.input.events.MultiTouchCursorEvent)
	 */
	@Override
	public void itemCursorPressed(IItem item, MultiTouchCursorEvent event) {
		if (!item.isVisible()) {
			return;
		}
		linkingGraph.pressedOn(item.getUUID().toString());
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.items.events.IItemListener#itemCursorReleased(
	 * multiplicity3.csys.items.item.IItem,
	 * multiplicity3.input.events.MultiTouchCursorEvent)
	 */
	@Override
	public void itemCursorReleased(IItem item, MultiTouchCursorEvent event) {
		linkingGraph.releasedOn(item.getUUID().toString());
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.items.events.IItemListener#itemMoved(multiplicity3
	 * .csys.items.item.IItem)
	 */
	@Override
	public void itemMoved(IItem item) {
		try {
			connectOrBreak(item);
			Vector2f coords = item.getWorldLocation();
			linkingGraph.getNodeForItem(item.getUUID().toString()).setPosition(
					coords.x, coords.y);
		} catch (ContentTypeNotBoundException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.items.events.IItemListener#itemRotated(multiplicity3
	 * .csys.items.item.IItem)
	 */
	@Override
	public void itemRotated(IItem item) {
	} // unneeded

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.items.events.IItemListener#itemScaled(multiplicity3
	 * .csys.items.item.IItem)
	 */
	@Override
	public void itemScaled(IItem item) {
	} // unneeded

	// ********* unneeded methods ********

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.items.events.IItemListener#itemVisibilityChanged(
	 * multiplicity3.csys.items.item.IItem, boolean)
	 */
	@Override
	public void itemVisibilityChanged(IItem item, boolean isVisible) {
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.items.events.IItemListener#itemZOrderChanged(multiplicity3
	 * .csys.items.item.IItem)
	 */
	@Override
	public void itemZOrderChanged(IItem item) {
	} // unneeded

	/**
	 * Register all currently on screen items for connecting.
	 */
	public void registerAllCurrentlyOnScreenItemsForConnecting() {
		log.info("Registering items for connecting...");
		for (IContainer container : expressionDisplay.getAllItems()) {
			log.info("  item " + container);
			registerItemForConnecting(container);
		}
	}

	/**
	 * Will only try and connect together those items that are registered.
	 * 
	 * @param item
	 */
	public void registerItemForConnecting(IItem item) {
		if (!registeredItems.contains(item)) {
			log.fine("Registering item for connecting: "
					+ item.getClass().getName());
			this.registeredItems.add(item);
			item.addItemListener(this);
		} else {
			log.fine("Did not register item for connecting: "
					+ item.getClass().getName());
		}
	}

	/**
	 * Sets the active.
	 *
	 * @param active the active
	 * @param forTarget the for target
	 */
	public void setActive(boolean active, double forTarget) {
		if (active) {
			this.target = forTarget;
			registerAllCurrentlyOnScreenItemsForConnecting();
			buildGraphFromNetwork();
			try {
				buildLinesFromGraph();
			} catch (ContentTypeNotBoundException e) {
				e.printStackTrace();
			}
		} else {
			unregisterAllItems();
			removeAllLines();
		}
	}

	// ********** private methods *********

	/**
	 * Sets the expression session and display.
	 *
	 * @param session the session
	 * @param expressionDisplay the expression display
	 * @param graphingLinesContainer the graphing lines container
	 */
	public void setExpressionSessionAndDisplay(ExpressionSession session,
			ExpressionDisplay expressionDisplay,
			IContainer graphingLinesContainer) {
		this.graphingLinesContainer = graphingLinesContainer;
		if (this.session != null) {
			this.session.removeChangeListener(this);
		}

		this.session = session;
		this.session.addChangeListener(this);
		this.expressionDisplay = expressionDisplay;
	}

	/**
	 * Sets the line colour.
	 *
	 * @param lineColour the new line colour
	 */
	public void setLineColour(ColorRGBA lineColour) {
		this.lineColour = lineColour;
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.apps.numbernet.model.IExpressionSessionChangeListener#
	 * targetChanged(java.lang.Double)
	 */
	@Override
	public void targetChanged(Double newValue) {
	}

	/**
	 * Unregister all items.
	 */
	public void unregisterAllItems() {
		for (IItem item : registeredItems) {
			item.removeItemListener(this);
		}
		registeredItems.clear();
	}

	/**
	 * Unregister an item - will ensure that it will no longer be able to be
	 * connected.
	 * 
	 * @param item
	 */
	public void unregisterItemForConnecting(IItem item) {
		item.removeItemListener(this);
		registeredItems.remove(item);
	}

	/**
	 * Adds the line.
	 *
	 * @param e the e
	 * @throws ContentTypeNotBoundException the content type not bound exception
	 */
	private void addLine(Edge e) throws ContentTypeNotBoundException {
		DistributedMap<String, Edge> distmap = TargetMaps.get()
				.getEdgesMapForTarget(target);
		distmap.put(e.getKey(), e);

		IItem item = getItemByID(e.a.getID());
		IItem closest = getItemByID(e.b.getID());
		if ((item == null) || (closest == null)) {
			log.warning("Could not find items for edge");
			log.warning("Will not be able to add a line.");
			return;
		}
		ICachableLine linkLine = contentFactory.create(ICachableLine.class,
				item.getUUID() + " to " + closest.getUUID(), UUID.randomUUID());
		linkLine.setLineWidth(4f);
		linkLine.setLineColour(getLineColour());
		linkLine.setSourceItem(item);
		linkLine.setDestinationItem(closest);
		linkLine.setLineVisibilityChangesWithItemVisibility(true);
		linkLine.setInteractionEnabled(false);

		linkLine.setRelativeLocation(lineShift);
		graphingLinesContainer.addItem(linkLine);
		graphingLinesContainer.getZOrderManager().sendToBottom(linkLine);
		associateLineWithEdge(e, linkLine);

		item.setWorldLocation(item.getWorldLocation()); // force line to update
		closest.setWorldLocation(closest.getWorldLocation()); // force line to
																// update

		ProjectorTransferUtilities.get().addToTransferableContents(linkLine,
				4f, 4f, item.getUUID() + " to " + closest.getUUID());

		// app.getZOrderManager().registerForZOrdering(linkLine);
		// app.getZOrderManager().bringToTop(item, null);
		// app.getZOrderManager().bringToTop(closest, null);

	}

	/**
	 * Associate line with edge.
	 *
	 * @param e the e
	 * @param line the line
	 */
	private void associateLineWithEdge(Edge e, ICachableLine line) {
		edgeToLine.put(e.getKey(), line);
	}

	/**
	 * Builds the graph from network.
	 */
	private void buildGraphFromNetwork() {
		log.fine("Finding edges on network...");
		DistributedMap<String, Edge> edges = TargetMaps.get()
				.getEdgesMapForTarget(target);
		log.fine("Found " + edges.size() + " edges.");
		for (Edge e : edges.values()) {
			linkingGraph.forceConnect(e.a.getID(), e.b.getID());
		}
	}

	/**
	 * Builds the lines from graph.
	 *
	 * @throws ContentTypeNotBoundException the content type not bound exception
	 */
	private void buildLinesFromGraph() throws ContentTypeNotBoundException {
		log.info("Creating visible edges...");
		for (Edge e : linkingGraph.getEdges()) {
			log.fine("Adding visible line for " + e.getKey());
			addLine(e);
		}
	}

	/**
	 * Connect or break.
	 *
	 * @param item the item
	 * @throws ContentTypeNotBoundException the content type not bound exception
	 */
	private void connectOrBreak(IItem item) throws ContentTypeNotBoundException {
		IItem closest = findClosestItemTo(item, distanceForConnection);
		if (closest != null) {
			if (!closest.isVisible()) {
				return;
			}

			if (!linkingGraph.isConnected(item.getUUID().toString(), closest
					.getUUID().toString())) {
				Edge e = linkingGraph.connect(item.getUUID().toString(),
						closest.getUUID().toString());
				if (e != null) {
					addLine(e);
				}
			}
		} else {
			List<Edge> linesToRemove = linkingGraph
					.getEdgesLongerThanDistanceIgnoringCurrentlyInteractingEdges(distanceForBreak);
			linkingGraph.removeEdges(linesToRemove);

			for (Edge e : linesToRemove) {
				ICachableLine line = getLineForEdge(e);
				graphingLinesContainer.removeItem(line);
				ProjectorTransferUtilities.get()
						.removeFromTransferableContents(line);
				DistributedMap<String, Edge> distmap = TargetMaps.get()
						.getEdgesMapForTarget(target);
				distmap.remove(e.getKey());
			}

		}
	}

	/**
	 * Find closest item to.
	 *
	 * @param item the item
	 * @param maxDistance the max distance
	 * @return the i item
	 */
	private IItem findClosestItemTo(IItem item, float maxDistance) {
		float minDist = Float.MAX_VALUE;
		IItem closest = null;
		for (IItem x : registeredItems) {
			if (item == x) {
				continue;
			}
			float distBetween = item.getRelativeLocation().distance(
					x.getRelativeLocation());
			if ((distBetween < minDist) && (distBetween <= maxDistance)) {
				closest = x;
				minDist = distBetween;
			}
		}
		return closest;
	}

	/**
	 * Gets the item by id.
	 *
	 * @param id the id
	 * @return the item by id
	 */
	private IItem getItemByID(String id) {
		for (IItem item : registeredItems) {
			if (item.getUUID().toString().equals(id)) {
				return item;
			}
		}
		return null;
	}

	/**
	 * Gets the line for edge.
	 *
	 * @param e the e
	 * @return the line for edge
	 */
	private ICachableLine getLineForEdge(Edge e) {
		return edgeToLine.get(e.getKey());
	}

	/**
	 * Removes the all lines.
	 */
	private void removeAllLines() {
		log.info("Removing " + edgeToLine.size() + " lines");
		for (ICachableLine line : edgeToLine.values()) {
			graphingLinesContainer.removeItem(line);
			ProjectorTransferUtilities.get().removeFromTransferableContents(
					line);
		}
		edgeToLine.clear();
	}

}
