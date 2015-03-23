package synergynet3.personalcontentcontrol;

import java.io.File;
import java.util.UUID;

import multiplicity3.csys.behaviours.RotateTranslateScaleBehaviour;
import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.items.border.IRoundedBorder;
import multiplicity3.csys.items.container.IContainer;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.items.shapes.IColourRectangle;
import multiplicity3.csys.stage.IStage;
import synergynet3.additionalitems.interfaces.ICachableImage;
import synergynet3.cluster.SynergyNetCluster;
import synergynet3.config.web.CacheOrganisation;
import synergynet3.databasemanagement.DatabaseActivity;
import synergynet3.web.shared.ColourManager;

import com.jme3.math.ColorRGBA;

/**
 * The Class StudentIconGenerator.
 */
public class StudentIconGenerator {

	/** The Constant DEFAULT_ICON_LOCATION. */
	private static final String DEFAULT_ICON_LOCATION = "synergynet3/personalcontentcontrol/defaultUserIcon.png";

	/**
	 * Generate icon.
	 *
	 * @param stage the stage
	 * @param width the width
	 * @param height the height
	 * @param borderWidth the border width
	 * @param movable the movable
	 * @param studentID the student id
	 * @return the i item
	 */
	public static IItem generateIcon(IStage stage, int width, int height,
			int borderWidth, boolean movable, String studentID) {
		try {

			IContainer wrapperFrame = stage.getContentFactory().create(
					IContainer.class, "userIconWrap", UUID.randomUUID());

			IColourRectangle rect = stage.getContentFactory().create(
					IColourRectangle.class, "solidbox", UUID.randomUUID());
			rect.setSolidBackgroundColour(ColorRGBA.Black);
			rect.setSize(width, height);
			rect.setInteractionEnabled(false);
			wrapperFrame.addItem(rect);

			ICachableImage studentImage = stage.getContentFactory().create(
					ICachableImage.class, "userImage", UUID.randomUUID());

			try {
				studentImage.setImage(getIconLocation(studentID));
			} catch (Exception e) {
				try {
					studentImage.setImage(new File(getIconLocation(studentID)));
				} catch (NullPointerException n) {
					studentImage
							.setImage(getIconLocation(DEFAULT_ICON_LOCATION));
				}
			}
			studentImage.setSize(width, height);
			wrapperFrame.addItem(studentImage);

			if (movable) {
				RotateTranslateScaleBehaviour rts = stage.getBehaviourMaker()
						.addBehaviour(studentImage,
								RotateTranslateScaleBehaviour.class);
				rts.setItemActingOn(wrapperFrame);
				rts.setScaleEnabled(false);
			} else {
				studentImage.setInteractionEnabled(false);
			}

			IRoundedBorder frameBorder = stage.getContentFactory().create(
					IRoundedBorder.class, "border", UUID.randomUUID());
			frameBorder.setBorderWidth(borderWidth);
			frameBorder.setSize(width, height);
			frameBorder.setColor(getStudentColour(studentID));
			wrapperFrame.addItem(frameBorder);

			if (movable) {
				RotateTranslateScaleBehaviour rtsTwo = stage
						.getBehaviourMaker().addBehaviour(frameBorder,
								RotateTranslateScaleBehaviour.class);
				rtsTwo.setItemActingOn(wrapperFrame);
				rtsTwo.setScaleEnabled(false);
			} else {
				frameBorder.setInteractionEnabled(false);
			}

			return wrapperFrame;
		} catch (ContentTypeNotBoundException e) {
			return null;
		}
	}

	/**
	 * Gets the extension.
	 *
	 * @param f the f
	 * @return the extension
	 */
	public static String getExtension(File f) {
		String ext = null;
		String s = f.getName();
		int i = s.lastIndexOf('.');

		if ((i > 0) && (i < (s.length() - 1))) {
			ext = s.substring(i + 1).toLowerCase();
		}

		if (ext == null) {
			return "";
		}

		return ext;
	}

	/**
	 * Gets the icon location.
	 *
	 * @param studentID the student id
	 * @return the icon location
	 */
	private static String getIconLocation(String studentID) {
		String iconLocation = DEFAULT_ICON_LOCATION;
		File iconCacheDir = new File(
				CacheOrganisation.getSpecificStudentIconDir(studentID));
		for (File f : iconCacheDir.listFiles()) {
			if (f.isFile()) {
				String ext = getExtension(f);
				if (!ext.equals("")) {
					if (ext.equalsIgnoreCase("jpg")
							|| ext.equalsIgnoreCase("png")) {
						iconLocation = CacheOrganisation
								.getSpecificStudentIconDir(studentID)
								+ File.separator + f.getName();
						break;
					}
				}
			}
		}

		return iconLocation;
	}

	/**
	 * Gets the student colour.
	 *
	 * @param studentID the student id
	 * @return the student colour
	 */
	private static ColorRGBA getStudentColour(String studentID) {

		String colour = DatabaseActivity.getStudentColour(studentID,
				SynergyNetCluster.get().getXMPPConnection().getHost());
		float[] rgb = ColourManager.getRGBForColour(colour);
		return new ColorRGBA(rgb[0], rgb[1], rgb[2], 1f);
	}

}
