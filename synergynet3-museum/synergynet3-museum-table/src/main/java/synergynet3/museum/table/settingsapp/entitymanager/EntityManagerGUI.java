package synergynet3.museum.table.settingsapp.entitymanager;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
// import java.util.Iterator;
// import java.util.Map.Entry;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import multiplicity3.input.events.MultiTouchCursorEvent;

import org.apache.commons.io.FileUtils;

import synergynet3.additionalUtils.IgnoreDoubleClick;
import synergynet3.mediadetection.mediasearchtypes.AudioSearchType;
import synergynet3.museum.table.mainapp.EntityManager;
import synergynet3.museum.table.settingsapp.MuseumAppPreferences;
import synergynet3.museum.table.settingsapp.entitymanager.creationguis.EntityCreatorGUI;
import synergynet3.museum.table.utils.Entity;

/**
 * The Class EntityManagerGUI.
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class EntityManagerGUI {

	/** The entities. */
	public static HashMap<String, Entity> entities;

	/** The Constant AUDIO_CHECK. */
	private static final AudioSearchType AUDIO_CHECK = new AudioSearchType();

	/** The content folder loc. */
	private static String contentFolderLoc;

	/** The entity list. */
	private JList entityList;

	/** The instance. */
	private EntityManagerGUI instance;

	/** The jf. */
	private JFrame jf;

	/**
	 * Instantiates a new entity manager gui.
	 */
	public EntityManagerGUI() {
		entities = new HashMap<String, Entity>();
		instance = this;
		contentFolderLoc = MuseumAppPreferences.getContentFolder();

		File f = new File(contentFolderLoc);
		if (!f.isDirectory() || contentFolderLoc.equals("")) {
			JOptionPane.showMessageDialog(jf,
					"The shared content folder is not set up correctly.");
			return;
		}

		int w = 512;
		int h = 300;

		jf = new JFrame("Entity Manager");
		jf.getContentPane().setLayout(new BorderLayout());
		jf.setSize(w, h);
		jf.setResizable(false);

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int screenX = (dim.width - w) / 2;
		int screenY = (dim.height - h) / 2;
		jf.setLocation(screenX, screenY);

		jf.getContentPane().setLayout(null);

		int xPadding = 10;
		int yPadding = 10;

		int panelWidth = w - (xPadding * 2);
		int panelHeight = h - (yPadding * 2);

		JPanel panel = generateListBoxAndControls(panelWidth, panelHeight);
		panel.setBounds(new Rectangle(0, 0, panelWidth, panelHeight));
		jf.getContentPane().add(panel);

		jf.setVisible(true);
	}

	/**
	 * Gets the entities.
	 *
	 * @return the entities
	 */
	public static String[] getEntities() {
		return getEntities(true);
	}

	/**
	 * Gets the entities.
	 *
	 * @param tidy the tidy
	 * @return the entities
	 */
	public static String[] getEntities(boolean tidy) {
		ArrayList<Entity> entitiesArray = new ArrayList<Entity>();
		File entitiesFolder = new File(contentFolderLoc + File.separator
				+ EntityManager.ENTITIES);
		if (!entitiesFolder.isDirectory()) {
			entitiesFolder.mkdir();
		}

		for (File folder : entitiesFolder.listFiles()) {
			if (folder.isDirectory()) {
				Entity entity = new Entity(contentFolderLoc + File.separator
						+ EntityManager.ENTITIES + File.separator
						+ folder.getName());
				if (!entity.getName().equals("")) {
					entities.put(entity.getName(), entity);
					entitiesArray.add(entity);
				} else if (tidy) {
					deleteDirectory(folder);
				}
			}
		}
		String[] toReturn = new String[entities.size()];
		int i = 0;
		for (String s : entities.keySet()) {
			toReturn[i] = s;
			i++;
		}
		return toReturn;
	}

	/**
	 * Delete directory.
	 *
	 * @param directory the directory
	 */
	private static void deleteDirectory(File directory) {
		try {
			FileUtils.deleteDirectory(directory);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Checks if is visible.
	 *
	 * @return true, if is visible
	 */
	public boolean isVisible() {
		if (jf == null) {
			return false;
		}
		return jf.isVisible();
	}

	/**
	 * Show.
	 */
	public void show() {
		if (jf != null) {
			jf.setVisible(true);
		}
	}

	/**
	 * Update list.
	 */
	public void updateList() {
		String[] contents = getEntities();
		Arrays.sort(contents, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return o1.compareToIgnoreCase(o2);
			}
		});
		entityList.setListData(contents);
	}

	/**
	 * Generate list box and controls.
	 *
	 * @param panelWidth the panel width
	 * @param panelHeight the panel height
	 * @return the j panel
	 */
	private JPanel generateListBoxAndControls(int panelWidth, int panelHeight) {

		JPanel panel = new JPanel();
		String[] entitiesArray = getEntities();
		Arrays.sort(entitiesArray);

		entityList = new JList(entitiesArray);
		entityList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		entityList.setLayoutOrientation(JList.VERTICAL);
		entityList.setCellRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 7479206168623862158L;

			@Override
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				Component c = super.getListCellRendererComponent(list, value,
						index, isSelected, cellHasFocus);
				String name = (String) value;
				if (hasContentAwaitingApproval(name)) {
					c.setFont(c.getFont().deriveFont(Font.BOLD));
				} else {
					c.setFont(c.getFont().deriveFont(Font.PLAIN));
				}
				return c;
			}

		});
		entityList.setVisibleRowCount(-1);

		JScrollPane listScroller = new JScrollPane(entityList);

		JButton createButton = new JButton();
		createButton.setText("Create");

		final IgnoreDoubleClick createClicker = new IgnoreDoubleClick() {
			@Override
			public void onAction(MultiTouchCursorEvent event) {
				new EntityCreatorGUI(null, instance);
			}
		};

		createButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				createClicker.click(null);
			}
		});

		JButton editButton = new JButton();
		editButton.setText("Edit");

		final IgnoreDoubleClick editClicker = new IgnoreDoubleClick() {
			@Override
			public void onAction(MultiTouchCursorEvent event) {
				if (entityList.getSelectedIndex() != -1) {
					String entityName = (String) entityList.getSelectedValue();
					Entity entity = entities.get(entityName);
					entity.regenerate();
					new EntityCreatorGUI(entity, instance);
				}
			}
		};

		editButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				editClicker.click(null);
			}
		});

		JButton removeButton = new JButton();
		removeButton.setText("Remove");

		final IgnoreDoubleClick removeClicker = new IgnoreDoubleClick() {
			@Override
			public void onAction(MultiTouchCursorEvent event) {
				if (entityList.getSelectedIndex() != -1) {
					String entityName = (String) entityList.getSelectedValue();
					int reply = JOptionPane.showConfirmDialog(null,
							"Are you sure you want to remove \n " + entityName
									+ "?", "Remove from entities",
							JOptionPane.YES_NO_OPTION);
					if (reply == JOptionPane.YES_OPTION) {
						if (entities.containsKey(entityName)) {
							deleteDirectory(new File(entities.get(entityName)
									.getLocation()));
							entities.remove(entityName);
							updateList();
						}
					}
				}
			}
		};

		removeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				removeClicker.click(null);
			}
		});

		JButton moderateButton = new JButton();
		moderateButton.setText("Moderate");
		moderateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (entityList.getSelectedIndex() != -1) {
					String entityName = (String) entityList.getSelectedValue();
					String location = entities.get(entityName).getLocation();
					new ModerationGUI(entityName, location, instance);
				}
			}
		});

		jf.getContentPane().setLayout(null);

		int xPadding = 10;
		int yPadding = 10;

		int x = 0;
		int y = 0;

		int height = 24;
		int buttonWidth = (panelWidth / 2) - xPadding;
		int listBoxWidth = panelWidth / 2;
		int listBoxHeight = panelHeight;

		panel.setLayout(null);

		listScroller
				.setBounds(new Rectangle(x, y, listBoxWidth, listBoxHeight));

		x = panelWidth - buttonWidth;
		y = panelHeight / 4;

		createButton.setBounds(new Rectangle(x, y, buttonWidth, height));

		y += height + yPadding;

		editButton.setBounds(new Rectangle(x, y, buttonWidth, height));

		y += height + yPadding;

		removeButton.setBounds(new Rectangle(x, y, buttonWidth, height));

		y += height + (yPadding * 2);

		moderateButton.setBounds(new Rectangle(x, y, buttonWidth, height));

		panel.add(listScroller);
		panel.add(createButton);
		panel.add(editButton);
		panel.add(removeButton);
		panel.add(moderateButton);

		updateList();

		return panel;
	}

	/**
	 * Checks for content awaiting approval.
	 *
	 * @param s the s
	 * @return true, if successful
	 */
	private boolean hasContentAwaitingApproval(String s) {
		if (entities.containsKey(s)) {
			String loc = entities.get(s).getLocation();
			File recordingFolder = new File(loc + File.separator
					+ EntityManager.RECORDINGS);
			if (recordingFolder.isDirectory()) {
				File[] files = recordingFolder.listFiles();
				for (File f : files) {
					if (!f.isDirectory()) {
						if (AUDIO_CHECK.isFileOfSearchType(f)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

}
