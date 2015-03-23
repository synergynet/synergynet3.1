package synergynet3.museum.table.settingsapp.entitymanager.creationguis.mappositiongui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import synergynet3.museum.table.settingsapp.MuseumAppPreferences;
import synergynet3.museum.table.settingsapp.SettingsUtil;
import synergynet3.museum.table.settingsapp.entitymanager.creationguis.EntityCreatorGUI;

import com.jme3.math.ColorRGBA;

/**
 * The Class LocationMapper.
 */
public class LocationMapper {

	/** The map height. */
	private int mapHeight = 540;

	/** The map width. */
	private int mapWidth = 960;

	/** The x on map. */
	private float xOnMap = 0.5f;

	/** The y on map. */
	private float yOnMap = 0.5f;

	/**
	 * Instantiates a new location mapper.
	 *
	 * @param xOnMapIn the x on map in
	 * @param yOnMapIn the y on map in
	 * @param parentGUI the parent gui
	 * @param modifiableGUI the modifiable gui
	 */
	public LocationMapper(float xOnMapIn, float yOnMapIn,
			final EntityCreatorGUI parentGUI,
			final ModifiableXandYFields modifiableGUI) {
		try {
			this.xOnMap = xOnMapIn;
			this.yOnMap = yOnMapIn;

			final int xPadding = 10;
			final int yPadding = 10;

			final int height = 24;

			final int displayWidth = mapWidth + (xPadding * 2);
			final int displayHeight = mapHeight + (height * 3) + (yPadding * 4);

			final int pointLabelWidth = 25;
			final int pointLabelHeight = 25;
			int smallButtonWidth = (displayWidth / 4) - (xPadding / 2);
			int labelWidth = 300;

			final JFrame jf = new JFrame("Position on Background");

			Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
			int screenX = (dim.width - displayWidth) / 2;
			int screenY = (dim.height - displayHeight) / 2;
			jf.setLocation(screenX, screenY);

			jf.getContentPane().setLayout(null);
			jf.setSize(displayWidth, displayHeight);
			jf.setResizable(false);

			int x = (displayWidth / 2) - (labelWidth / 2);
			int y = yPadding;

			JLabel textLabel = new JLabel(
					"Click on the map to position the entity.");
			textLabel.setBounds(new Rectangle(x, y, labelWidth, height));
			textLabel.setHorizontalAlignment(SwingConstants.RIGHT);
			jf.getContentPane().add(textLabel);

			Image img = ImageIO.read(SettingsUtil.class.getResource("poi.png"));
			final JLabel pointLabel = new JLabel(new ImageIcon(img));

			int targetX = (int) (((xOnMap * mapWidth) + xPadding) - (pointLabelWidth / 2));
			int targetY = (int) ((((1 - yOnMap) * mapHeight) + height + (yPadding * 2)) - (pointLabelHeight / 2));

			pointLabel.setBounds(new Rectangle(targetX, targetY,
					pointLabelWidth, pointLabelHeight));

			MouseListener pointListener = new MouseListener() {
				public void mouseClicked(MouseEvent e) {
				}

				public void mouseEntered(MouseEvent e) {
				}

				public void mouseExited(MouseEvent e) {
				}

				public void mousePressed(MouseEvent e) {

					float eventX = e.getPoint().x;
					float eventY = e.getPoint().y;

					xOnMap = Float.parseFloat(String.format("%.3f", eventX
							/ mapWidth));
					yOnMap = Float.parseFloat(String.format("%.3f",
							1 - (eventY / (mapHeight))));

					int targetX = (e.getPoint().x + xPadding)
							- (pointLabelWidth / 2);
					int targetY = (e.getPoint().y + height + (yPadding * 2))
							- (pointLabelHeight / 2);

					pointLabel.setBounds(new Rectangle(targetX, targetY,
							pointLabelWidth, pointLabelHeight));
				}

				public void mouseReleased(MouseEvent e) {
				}
			};

			x = xPadding;
			y += height + yPadding;

			boolean backgroundImageFound = false;

			File file = MuseumAppPreferences.getBackgroundImage();
			if (file != null) {
				if (file.exists()) {
					BufferedImage backgroundImage = ImageIO.read(file);

					JLabel picLabel = new JLabel(new ImageIcon(((new ImageIcon(
							backgroundImage)).getImage()).getScaledInstance(
							mapWidth, mapHeight, java.awt.Image.SCALE_SMOOTH)));
					picLabel.addMouseListener(pointListener);
					jf.getContentPane().add(pointLabel);

					picLabel.setBounds(new Rectangle(x, y, mapWidth, mapHeight));
					jf.getContentPane().add(picLabel);

					backgroundImageFound = true;

				}
			}

			if (!backgroundImageFound) {
				ColorRGBA colourRGBA = MuseumAppPreferences
						.getBackgroundColour();
				Color colour = new Color(colourRGBA.r, colourRGBA.g,
						colourRGBA.b, colourRGBA.a);

				JPanel panel = new JPanel();
				panel.setBackground(colour);
				panel.setSize(mapWidth, mapHeight);

				panel.addMouseListener(pointListener);
				jf.getContentPane().add(pointLabel);

				panel.setBounds(new Rectangle(x, y, mapWidth, mapHeight));
				jf.getContentPane().add(panel);
			}

			x = (displayWidth / 2) - ((xPadding + (smallButtonWidth * 2)) / 2);
			y += mapHeight + yPadding;

			JButton okButton = new JButton();
			okButton.setText("OK");
			okButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					modifiableGUI.updateXandYFields(xOnMap, yOnMap);

					jf.setVisible(false);
					parentGUI.relatedFrames.remove(jf);
				}
			});
			okButton.setBounds(new Rectangle(x, y, smallButtonWidth, height));
			jf.getContentPane().add(okButton);

			x += smallButtonWidth + xPadding;

			JButton cancelButton = new JButton();
			cancelButton.setText("Cancel");
			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					jf.setVisible(false);
					parentGUI.relatedFrames.remove(jf);
				}
			});
			cancelButton.setBounds(new Rectangle(x, y, smallButtonWidth
					- xPadding, height));
			jf.getContentPane().add(cancelButton);

			jf.setVisible(true);

			parentGUI.relatedFrames.add(jf);

		} catch (IOException e) {
		}
	}

	/**
	 * @return the xOnMap
	 */
	public float getxOnMap() {
		return xOnMap;
	}

	/**
	 * @return the yOnMap
	 */
	public float getyOnMap() {
		return yOnMap;
	}

}
