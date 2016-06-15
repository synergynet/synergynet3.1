package synergynet3.apps.numbernet.ui.expression;

import java.awt.Color;
import java.util.UUID;

import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.gfx.ColourUtils;
import multiplicity3.csys.items.container.IContainer;
import multiplicity3.csys.stage.IStage;
import multiplicity3.input.MultiTouchEventAdapter;
import multiplicity3.input.events.MultiTouchCursorEvent;
import synergynet3.additionalitems.interfaces.ITextbox;
import synergynet3.fonts.FontColour;
import synergynet3.projector.network.ProjectorTransferUtilities;
import synergynet3.web.apps.numbernet.shared.Expression;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;

/**
 * A factory for creating ExpressionVisualRepresentation objects.
 */
public class ExpressionVisualRepresentationFactory
{

	/** The Constant EXPRESSION_HEIGHT. */
	private static final float EXPRESSION_HEIGHT = 45f;

	/** The parent container. */
	private IContainer parentContainer;

	/** The stage. */
	private IStage stage;

	/**
	 * Instantiates a new expression visual representation factory.
	 *
	 * @param parentContainer
	 *            the parent container
	 * @param stage
	 *            the stage
	 */
	public ExpressionVisualRepresentationFactory(IContainer parentContainer, IStage stage)
	{
		this.parentContainer = parentContainer;
		this.stage = stage;
	}

	/**
	 * Creates a new ExpressionVisualRepresentation object.
	 *
	 * @param expression
	 *            the expression
	 * @param rotation
	 *            the rotation
	 * @param worldLocation
	 *            the world location
	 * @return the i container
	 * @throws ContentTypeNotBoundException
	 *             the content type not bound exception
	 */
	public IContainer createExpressionVisualRepresentation(Expression expression, float rotation, Vector2f worldLocation) throws ContentTypeNotBoundException
	{

		UUID idToUse = UUID.fromString(expression.getId());

		Color color = ColourUtils.colorFromString(expression.getCreatedOnTable());
		ColorRGBA crgba = ColourUtils.getColorRGBAFromColor(color);
		if (color != null)
		{
			crgba.a = 0.5f;
		}
		else
		{
			crgba = new ColorRGBA(1, 1, 1, 1f);
		}

		final ITextbox textItem = stage.getContentFactory().create(ITextbox.class, "", idToUse);
		textItem.setMovable(true);
		textItem.setColours(ColorRGBA.DarkGray, crgba, FontColour.White);
		textItem.setHeight(EXPRESSION_HEIGHT);
		textItem.setText(expression.getExpression(), stage);
		textItem.setScaleLimits(1f, 1f);

		ProjectorTransferUtilities.get().addToTransferableContents(textItem, textItem.getWidth(), textItem.getHeight(), idToUse.toString());

		this.parentContainer.addItem(textItem);
		textItem.setRelativeRotation(rotation);
		textItem.setWorldLocation(worldLocation);

		stage.getDragAndDropSystem().registerDragSource(textItem.getListenBlock());

		textItem.getListenBlock().getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter()
		{
			@Override
			public void cursorPressed(MultiTouchCursorEvent event)
			{
				textItem.getMultiTouchDispatcher().cursorPressed(event);
			}

			@Override
			public void cursorReleased(MultiTouchCursorEvent event)
			{
				textItem.getMultiTouchDispatcher().cursorReleased(event);
			}
		});

		return (IContainer) textItem;
	}

}
