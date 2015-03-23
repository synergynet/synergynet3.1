package synergynet3.web.appsystem.client.ui.dbcontrol;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import synergynet3.web.appsystem.client.service.SynergyNetAppSystemService;
import synergynet3.web.commons.client.devicesonline.DevicesOnlineWidget;
import synergynet3.web.commons.client.dialogs.MessageDialogBox;
import synergynet3.web.commons.client.ui.FixedSizeScrollableListBox;
import synergynet3.web.shared.ClassRoom;
import synergynet3.web.shared.ColourManager;
import synergynet3.web.shared.Student;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * The Class StudentAdministationPanel.
 */
public class StudentAdministationPanel extends VerticalPanel {

	/** The students. */
	private static ArrayList<Student> students = new ArrayList<Student>();

	/** The create class box. */
	private ListBox createClassBox;

	/** The create colour box. */
	private ListBox createColourBox;

	/** The create name box. */
	private TextBox createNameBox;

	/** The devices online. */
	private DevicesOnlineWidget devicesOnline;

	/** The modify class box. */
	private ListBox modifyClassBox;

	/** The modify colour box. */
	private ListBox modifyColourBox;

	/** The modify name box. */
	private TextBox modifyNameBox;

	/** The selected student. */
	private Student selectedStudent;

	/** The student box. */
	private FixedSizeScrollableListBox studentBox;

	/**
	 * Instantiates a new student administation panel.
	 */
	public StudentAdministationPanel() {

		setTitle("Student Administration");
		setSpacing(5);

		DisclosurePanel tableSelectionDisclosurePanel = new DisclosurePanel(
				"Tables Online");
		add(tableSelectionDisclosurePanel);
		tableSelectionDisclosurePanel.setOpen(true);
		tableSelectionDisclosurePanel.setWidth("272");

		devicesOnline = new DevicesOnlineWidget() {
			@Override
			protected void onRefresh() {
				fillStudentList();
			};
		};
		tableSelectionDisclosurePanel.setContent(devicesOnline);
		devicesOnline.setSize("270px", "100px");
		devicesOnline.setDeviceType("tables");
		devicesOnline.setMultipleSelectionAllowed(false);

		DisclosurePanel pnlStudentSettings = new DisclosurePanel("Students");
		pnlStudentSettings.setOpen(true);
		add(pnlStudentSettings);
		pnlStudentSettings.setWidth("272px");

		VerticalPanel verticalStudentPanel = new VerticalPanel();
		pnlStudentSettings.setContent(verticalStudentPanel);

		studentBox = new FixedSizeScrollableListBox();
		studentBox.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (studentBox.getSelectedIndex() < 0) {
					selectedStudent = null;
					modifyNameBox.setText("");
					modifyClassBox.setSelectedIndex(0);
					modifyColourBox.setSelectedIndex(0);
				} else {
					selectedStudent = students.get(studentBox
							.getSelectedIndex());
					modifyNameBox.setText(selectedStudent.getName());
					modifyClassBox
							.setSelectedIndex(getIndexOfClass(selectedStudent
									.getClassName()));
					modifyColourBox
							.setSelectedIndex(getIndexOfColour(selectedStudent
									.getColour()));
				}
			}

		});
		verticalStudentPanel.add(studentBox);
		studentBox.setSize("270px", "100px");
		studentBox.setMultipleSelect(false);

		HorizontalPanel refreshStudentsPanel = new HorizontalPanel();

		Button btnRefreshStudents = new Button("Refresh");
		btnRefreshStudents.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				fillStudentList();
			}
		});
		refreshStudentsPanel.add(btnRefreshStudents);
		verticalStudentPanel.add(refreshStudentsPanel);

		HorizontalPanel horizontalBuffer = new HorizontalPanel();
		horizontalBuffer.setHeight("10px");
		verticalStudentPanel.add(horizontalBuffer);

		HorizontalPanel horizontalStudentPanel_1 = new HorizontalPanel();

		Button btnSendToTable = new Button(
				"Log selected student in on selected table");
		btnSendToTable.setWidth("272px");
		btnSendToTable.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (devicesOnline.getDeviceSelected() != null) {
					if (selectedStudent != null) {
						if (selectedStudent.getTable().equals("none")) {
							SynergyNetAppSystemService.Util.get()
									.sendStudentToTable(
											selectedStudent.getStudentID(),
											devicesOnline.getDeviceSelected(),
											new AsyncCallback<Void>() {
												@Override
												public void onFailure(
														Throwable caught) {
													new MessageDialogBox(
															"Communication error logging student in: "
																	+ caught.getMessage())
															.show();
												}

												@Override
												public void onSuccess(
														Void result) {
												}
											});
						}
						selectedStudent.setTable(devicesOnline
								.getDeviceSelected());
						fillStudentList();
					}
				}
			}
		});
		horizontalStudentPanel_1.add(btnSendToTable);
		verticalStudentPanel.add(horizontalStudentPanel_1);

		HorizontalPanel horizontalStudentPanel_2 = new HorizontalPanel();

		Button btnRemoveFromTable = new Button(
				"Log selected student out from table");
		btnRemoveFromTable.setWidth("272px");
		btnRemoveFromTable.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (selectedStudent != null) {
					SynergyNetAppSystemService.Util.get()
							.removeStudentFromTable(
									selectedStudent.getStudentID(),
									selectedStudent.getTable(),
									new AsyncCallback<Void>() {
										@Override
										public void onFailure(Throwable caught) {
											new MessageDialogBox(
													"Communication error logging student out: "
															+ caught.getMessage())
													.show();
										}

										@Override
										public void onSuccess(Void result) {
										}
									});
				}
				selectedStudent.setTable("none");
				fillStudentList();
			}
		});
		horizontalStudentPanel_2.add(btnRemoveFromTable);
		verticalStudentPanel.add(horizontalStudentPanel_2);

		DisclosurePanel pnlStudentModify = new DisclosurePanel(
				"Modify Student Account");
		pnlStudentModify.setOpen(false);
		add(pnlStudentModify);
		pnlStudentModify.setWidth("272px");

		VerticalPanel verticalModifyPanel = new VerticalPanel();
		pnlStudentModify.setContent(verticalModifyPanel);

		HorizontalPanel horizontalModifyPanel_1 = new HorizontalPanel();
		verticalModifyPanel.add(horizontalModifyPanel_1);

		Label lblNameModify = new Label("Name: ");
		lblNameModify.setWidth("80px");
		horizontalModifyPanel_1.add(lblNameModify);

		modifyNameBox = new TextBox();
		modifyNameBox.setWidth("150px");
		horizontalModifyPanel_1.add(modifyNameBox);

		HorizontalPanel horizontalModifyPanel_2 = new HorizontalPanel();
		verticalModifyPanel.add(horizontalModifyPanel_2);

		Label lblClassModify = new Label("Class: ");
		lblClassModify.setWidth("80px");
		horizontalModifyPanel_2.add(lblClassModify);

		modifyClassBox = new ListBox();
		modifyClassBox.setVisibleItemCount(1);
		modifyClassBox.setWidth("150px");
		horizontalModifyPanel_2.add(modifyClassBox);

		HorizontalPanel horizontalModifyPanel_3 = new HorizontalPanel();
		verticalModifyPanel.add(horizontalModifyPanel_3);

		Label lblColourModify = new Label("Icon Colour: ");
		lblColourModify.setWidth("80px");
		horizontalModifyPanel_3.add(lblColourModify);

		modifyColourBox = new ListBox();
		modifyColourBox.setVisibleItemCount(1);
		modifyColourBox.setWidth("80px");
		for (String colour : ColourManager.getColours()) {
			modifyColourBox.addItem(colour);
		}
		horizontalModifyPanel_3.add(modifyColourBox);

		HorizontalPanel horizontalModifyPanel_4 = new HorizontalPanel();
		verticalModifyPanel.add(horizontalModifyPanel_4);

		Label lblGapModify = new Label(" ");
		lblGapModify.setWidth("80px");
		horizontalModifyPanel_4.add(lblGapModify);

		Button buttonModifyUser = new Button("Modify");
		buttonModifyUser.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				modifyStudent();
			}
		});
		horizontalModifyPanel_4.add(buttonModifyUser);

		DisclosurePanel pnlStudentCreate = new DisclosurePanel(
				"Create Student Account");
		pnlStudentCreate.setOpen(false);
		add(pnlStudentCreate);
		pnlStudentCreate.setWidth("272px");

		VerticalPanel verticalCreatePanel = new VerticalPanel();
		pnlStudentCreate.setContent(verticalCreatePanel);

		HorizontalPanel horizontalCreatePanel_1 = new HorizontalPanel();
		verticalCreatePanel.add(horizontalCreatePanel_1);

		Label lblNameCreate = new Label("Name: ");
		lblNameCreate.setWidth("80px");
		horizontalCreatePanel_1.add(lblNameCreate);

		createNameBox = new TextBox();
		createNameBox.setWidth("150px");
		horizontalCreatePanel_1.add(createNameBox);

		HorizontalPanel horizontalCreatePanel_2 = new HorizontalPanel();
		verticalCreatePanel.add(horizontalCreatePanel_2);

		Label lblClassCreate = new Label("Class: ");
		lblClassCreate.setWidth("80px");
		horizontalCreatePanel_2.add(lblClassCreate);

		createClassBox = new ListBox();
		createClassBox.setVisibleItemCount(1);
		createClassBox.setWidth("150px");
		horizontalCreatePanel_2.add(createClassBox);

		HorizontalPanel horizontalCreatePanel_3 = new HorizontalPanel();
		verticalCreatePanel.add(horizontalCreatePanel_3);

		Label lblColourCreate = new Label("Icon Colour:");
		lblColourCreate.setWidth("80px");
		horizontalCreatePanel_3.add(lblColourCreate);

		createColourBox = new ListBox();
		createColourBox.setVisibleItemCount(1);
		createColourBox.setWidth("80px");
		for (String colour : ColourManager.getColours()) {
			createColourBox.addItem(colour);
		}
		horizontalCreatePanel_3.add(createColourBox);

		HorizontalPanel horizontalCreatePanel_4 = new HorizontalPanel();
		verticalCreatePanel.add(horizontalCreatePanel_4);

		Label lblCreateGap = new Label(" ");
		lblCreateGap.setWidth("80px");
		horizontalCreatePanel_4.add(lblCreateGap);

		Button buttonCreateUser = new Button("Create");
		buttonCreateUser.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				createStudent();
			}
		});
		horizontalCreatePanel_4.add(buttonCreateUser);

		DisclosurePanel pnlStudentRemove = new DisclosurePanel(
				"Remove Student Account");
		pnlStudentRemove.setOpen(false);
		add(pnlStudentRemove);
		pnlStudentRemove.setWidth("272px");

		VerticalPanel verticalRemovePanel = new VerticalPanel();
		pnlStudentRemove.setContent(verticalRemovePanel);

		HorizontalPanel horizontalRemovePanel_1 = new HorizontalPanel();
		verticalRemovePanel.add(horizontalRemovePanel_1);

		Button buttonRemoveStudent = new Button(
				"Remove Selected Student Account");
		buttonRemoveStudent.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (selectedStudent == null) {
					return;
				}

				if (!selectedStudent.getTable().equals("none")) {
					new MessageDialogBox(
							"Please remove this student from their table before modifying their account.")
							.show();
					return;
				}

				if (Window.confirm("Remove account for "
						+ selectedStudent.getName() + "?")) {
					removeStudent();
				}
			}
		});
		horizontalRemovePanel_1.add(buttonRemoveStudent);

		fillStudentList();
		devicesOnline.updateList();

	}

	/**
	 * Load students from class.
	 *
	 * @param classname the classname
	 */
	public void loadStudentsFromClass(String classname) {
		if (DatabaseControlUI.getClassSelectionPanel().getClassesSelected()
				.size() > 0) {
			// Get student list from database and add to students array
			SynergyNetAppSystemService.Util.get().getStudentsFromClass(
					classname, new AsyncCallback<ArrayList<Student>>() {
						@Override
						public void onFailure(Throwable caught) {
							new MessageDialogBox(
									"Error loading students from database: "
											+ caught.getMessage()).show();
						}

						@Override
						public void onSuccess(ArrayList<Student> result) {
							for (Student student : result) {
								students.add(student);
							}
							fillStudentList();
						}
					});
		}

	}

	/**
	 * Removes the students of class.
	 *
	 * @param className the class name
	 */
	public void removeStudentsOfClass(String className) {
		ArrayList<Student> toRemove = new ArrayList<Student>();
		for (int i = 0; i < students.size(); i++) {
			if (!DatabaseControlUI.getClassSelectionPanel()
					.getClassesSelected()
					.contains(students.get(i).getClassName())) {
				toRemove.add(students.get(i));
			}
		}

		for (Student student : toRemove) {
			students.remove(student);
		}
		toRemove.clear();
		fillStudentList();
	}

	/**
	 * Update class box.
	 *
	 * @param classSelectionPanel the class selection panel
	 */
	public void updateClassBox(ClassSelectionPanel classSelectionPanel) {
		modifyClassBox.clear();
		createClassBox.clear();
		for (ClassRoom classroom : classSelectionPanel.getClassrooms()) {
			String name = classroom.getName();
			modifyClassBox.addItem(name);
			createClassBox.addItem(name);
		}
	}

	/**
	 * Creates the student.
	 */
	private void createStudent() {
		if (createNameBox.getText().replaceAll(" ", "").length() > 0) {

			Student student = new Student();
			student.setName(createNameBox.getText());
			student.setClassName(createClassBox.getItemText(createClassBox
					.getSelectedIndex()));
			student.setColour(createColourBox.getItemText(createColourBox
					.getSelectedIndex()));
			DateTimeFormat yearFormat = DateTimeFormat
					.getFormat("yyyyMMddHHmmssSS");
			String studentID = student.getName().replace(" ", "");
			studentID += "_" + yearFormat.format(new Date());
			student.setStudentID(studentID);

			createNameBox.setText("");

			if (DatabaseControlUI.getClassSelectionPanel().getClassesSelected()
					.contains(student.getClassName())) {
				students.add(student);
			}

			fillStudentList();

			SynergyNetAppSystemService.Util.get().createStudent(student,
					new AsyncCallback<Void>() {
						@Override
						public void onFailure(Throwable caught) {
							new MessageDialogBox(
									"Communication error when creating student: "
											+ caught.getMessage()).show();
						}

						@Override
						public void onSuccess(Void result) {
						}
					});
		}
	}

	/**
	 * Fill student list.
	 */
	private void fillStudentList() {
		modifyNameBox.setText("");
		studentBox.removeAllItems();
		selectedStudent = null;
		Collections.sort(students);
		for (Student student : students) {
			String studentName = student.getName();
			if (!student.getTable().equals("none")) {
				if (devicesOnline.getDevicesPresent().contains(
						student.getTable())) {
					studentName += " (" + student.getTable() + ")";
				} else {
					student.setTable("none");
				}
			}
			studentBox.addItem(studentName);
		}
	}

	/**
	 * Gets the index of class.
	 *
	 * @param className the class name
	 * @return the index of class
	 */
	private int getIndexOfClass(String className) {
		for (int i = 0; i < modifyClassBox.getItemCount(); i++) {
			if (modifyClassBox.getItemText(i).equalsIgnoreCase(className)) {
				return i;
			}
		}
		return 0;
	}

	/**
	 * Gets the index of colour.
	 *
	 * @param colour the colour
	 * @return the index of colour
	 */
	private int getIndexOfColour(String colour) {
		for (int i = 0; i < modifyColourBox.getItemCount(); i++) {
			if (modifyColourBox.getItemText(i).equalsIgnoreCase(colour)) {
				return i;
			}
		}
		return 0;
	}

	/**
	 * Modify student.
	 */
	private void modifyStudent() {
		if ((modifyNameBox.getText().replaceAll(" ", "").length() > 0)
				&& (selectedStudent != null)) {

			if (!selectedStudent.getTable().equals("none")) {
				new MessageDialogBox(
						"Please remove this student from their table before modifying their account.")
						.show();
				return;
			}

			for (Student student : students) {
				if (!student.equals(selectedStudent)) {
					if (student.getName().equalsIgnoreCase(
							createNameBox.getText())) {
						new MessageDialogBox(
								"A student with this name already exists.")
								.show();
						return;
					}
				}
			}

			selectedStudent.setName(modifyNameBox.getText());
			selectedStudent.setClassName(modifyClassBox
					.getItemText(modifyClassBox.getSelectedIndex()));
			selectedStudent.setColour(modifyColourBox
					.getItemText(modifyColourBox.getSelectedIndex()));

			SynergyNetAppSystemService.Util.get().modifyStudent(
					selectedStudent, new AsyncCallback<Void>() {
						@Override
						public void onFailure(Throwable caught) {
							new MessageDialogBox(
									"Communication error when modifying student: "
											+ caught.getMessage()).show();
						}

						@Override
						public void onSuccess(Void result) {
						}
					});

			if (!DatabaseControlUI.getClassSelectionPanel()
					.getClassesSelected()
					.contains(selectedStudent.getClassName())) {
				students.remove(selectedStudent);
			}

			fillStudentList();
		}
	}

	/**
	 * Removes the student.
	 */
	private void removeStudent() {

		if (!selectedStudent.getTable().equals("none")) {
			SynergyNetAppSystemService.Util.get().removeStudentFromTable(
					selectedStudent.getStudentID(), selectedStudent.getTable(),
					new AsyncCallback<Void>() {
						@Override
						public void onFailure(Throwable caught) {
							new MessageDialogBox(
									"Communication error when logging out student: "
											+ caught.getMessage()).show();
						}

						@Override
						public void onSuccess(Void result) {
						}
					});
		}
		students.remove(selectedStudent);

		SynergyNetAppSystemService.Util.get().removeStudent(
				selectedStudent.getStudentID(), new AsyncCallback<Void>() {
					@Override
					public void onFailure(Throwable caught) {
						new MessageDialogBox(
								"Communication error when removing student: "
										+ caught.getMessage()).show();
					}

					@Override
					public void onSuccess(Void result) {
					}
				});

		fillStudentList();
	}

}
