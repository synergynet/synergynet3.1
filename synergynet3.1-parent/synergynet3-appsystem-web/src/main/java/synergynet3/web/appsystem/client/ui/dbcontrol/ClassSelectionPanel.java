package synergynet3.web.appsystem.client.ui.dbcontrol;

import java.util.ArrayList;
import java.util.Collections;

import synergynet3.web.appsystem.client.service.SynergyNetAppSystemService;
import synergynet3.web.commons.client.dialogs.MessageDialogBox;
import synergynet3.web.commons.client.ui.FixedSizeScrollableListBox;
import synergynet3.web.shared.ClassRoom;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.DisclosurePanel;

public class ClassSelectionPanel extends VerticalPanel {
	
	private FixedSizeScrollableListBox classroomBox;
	
	private ClassRoom selectedClass;	
	
	private TextBox createNameBox;
	
	private ArrayList<String> classesSelected = new ArrayList<String>();
	
	private ArrayList<ClassRoom> classrooms = new ArrayList<ClassRoom>();

	public ClassSelectionPanel() {
		setTitle("Class Selection");
		setSpacing(5);				
			
		SynergyNetAppSystemService.Util.get().getCurrentClasses(new AsyncCallback<ArrayList<ClassRoom>>() {
			@Override
			public void onFailure(Throwable caught) {
				new MessageDialogBox("Error loading classes: " + caught.getMessage()).show();					
			}
			@Override
			public void onSuccess(ArrayList<ClassRoom> result) {
				classrooms = result;
				fillClassList();
			}
		});
		
		DisclosurePanel pnlClassSettings = new DisclosurePanel("Classes");
		pnlClassSettings.setOpen(true);
		add(pnlClassSettings);
		pnlClassSettings.setWidth("272px");
		
		VerticalPanel verticalClassPanel = new VerticalPanel();
		pnlClassSettings.setContent(verticalClassPanel);
		
		classroomBox = new FixedSizeScrollableListBox();
		verticalClassPanel.add(classroomBox);
		classroomBox.setSize("270px", "100px");
		classroomBox.setMultipleSelect(false);
		classroomBox.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (classroomBox.getSelectedIndex() < 0){
					selectedClass = null;
				}else{
					selectedClass = classrooms.get(classroomBox.getSelectedIndex());					
				}
			}			
		});	
		
		HorizontalPanel horizontalBuffer = new HorizontalPanel();
		horizontalBuffer.setHeight("10px");
		verticalClassPanel.add(horizontalBuffer);

		HorizontalPanel horizontalClassPanel_1 = new HorizontalPanel();
		verticalClassPanel.add(horizontalClassPanel_1);
		
		Button btnStartClass = new Button("Start Class");
		btnStartClass.setWidth("272px");
		btnStartClass.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (selectedClass != null ){
					if (!selectedClass.isInSession()){
						selectedClass.setInSession(true);
						classesSelected.add(selectedClass.getName());						
						DatabaseControlUI.getStudentAdminPanel().loadStudentsFromClass(selectedClass.getName());
						fillClassList();
					}
				}
			}
		});
		horizontalClassPanel_1.add(btnStartClass);	
		
		HorizontalPanel horizontalClassPanel_2 = new HorizontalPanel();
		verticalClassPanel.add(horizontalClassPanel_2);
		
		Button btnStopClass = new Button("Stop Class");
		btnStopClass.setWidth("272px");
		btnStopClass.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (selectedClass != null ){
					if (selectedClass.isInSession()){
					
						selectedClass.setInSession(false);
						classesSelected.remove(selectedClass.getName());
					
						DatabaseControlUI.getStudentAdminPanel().removeStudentsOfClass(selectedClass.getName());
						
						SynergyNetAppSystemService.Util.get().removeStudentsOfClass(selectedClass.getName(), new AsyncCallback<Void>() {
							@Override public void onSuccess(Void result) {}
							
							@Override
							public void onFailure(Throwable caught) {
								new MessageDialogBox("Communication error when logging out class: " + caught.getMessage()).show();
							}
						});		
						
						fillClassList();
					}
				}
			}
		});
		horizontalClassPanel_2.add(btnStopClass);	

		
		DisclosurePanel pnlClassCreate = new DisclosurePanel("Create Class");
		pnlClassCreate.setOpen(false);
		add(pnlClassCreate);
		pnlClassCreate.setWidth("272px");
		
		VerticalPanel verticalCreatePanel = new VerticalPanel();
		pnlClassCreate.setContent(verticalCreatePanel);
		
		HorizontalPanel horizontalCreatePanel_1 = new HorizontalPanel();
		verticalCreatePanel.add(horizontalCreatePanel_1);
		
		Label lblNameCreate = new Label("Name: ");
		lblNameCreate.setWidth("60px");
		horizontalCreatePanel_1.add(lblNameCreate);

		createNameBox = new TextBox();
		createNameBox.setWidth("150px");
		horizontalCreatePanel_1.add(createNameBox);
		createNameBox.addKeyUpHandler(new KeyUpHandler() {			
			@Override
			public void onKeyUp(KeyUpEvent event) {
		        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
		        	createClassroom();
		        }			
			}
		});
		
		Button buttonCreateUser = new Button("Create");
		buttonCreateUser.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				createClassroom();
			}
		});
		horizontalCreatePanel_1.add(buttonCreateUser);
		
		DisclosurePanel pnlClassRemove = new DisclosurePanel("Remove Class");
		pnlClassRemove.setOpen(false);
		add(pnlClassRemove);
		pnlClassRemove.setWidth("272px");
		
		VerticalPanel verticalRemovePanel = new VerticalPanel();
		pnlClassRemove.setContent(verticalRemovePanel);
		
		HorizontalPanel horizontalRemovePanel_1 = new HorizontalPanel();
		verticalRemovePanel.add(horizontalRemovePanel_1);
		
		Button buttonRemoveClass = new Button("Remove Selected Class");
		buttonRemoveClass.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				if (selectedClass == null)return;
				
				if (selectedClass.isInSession()){
					new MessageDialogBox("This class cannot be removed because it is in session.").show();
					return;
				}
					
				 if (Window.confirm("Removing " +  selectedClass.getName() + " will also remove all its students.")){
					 removeClass();
				 }
			}
		});
		horizontalRemovePanel_1.add(buttonRemoveClass);

		DisclosurePanel pnlStopDB = new DisclosurePanel("Stop Database");
		pnlStopDB.setOpen(false);
		add(pnlStopDB);
		pnlStopDB.setWidth("272px");
		
		VerticalPanel stopDBVerticalPanel = new VerticalPanel();
		pnlStopDB.setContent(stopDBVerticalPanel);
		
		HorizontalPanel stopDBHorinzontalPanel = new HorizontalPanel();
		stopDBVerticalPanel.add(stopDBHorinzontalPanel);
		
		Button btnStopDB = new Button("Shut Down Database");
		btnStopDB.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
		    	shutDownDB();
			}
		});
		stopDBHorinzontalPanel.add(btnStopDB);	
		
		fillClassList();
		
		//start the db running
		SynergyNetAppSystemService.Util.get().startDatabase(new AsyncCallback<Void>() {
			@Override public void onSuccess(Void result) {}
			
			@Override
			public void onFailure(Throwable caught) {}
		});		
		
		
	}
	
	private void fillClassList() {
		classroomBox.removeAllItems();
		selectedClass = null;
		Collections.sort(classrooms);
		for (ClassRoom classroom: classrooms){
			String name = classroom.getName();
			if (classroom.isInSession())name += " (In Session)";
			classroomBox.addItem(name);
		}	
		DatabaseControlUI.getStudentAdminPanel().updateClassBox(this);
	}
	
	private void createClassroom(){
		String name = createNameBox.getText().replaceAll(" ", "");
		if (name.length() > 0){

			for (ClassRoom db: classrooms){
				if (db.getName().equalsIgnoreCase(name)){
					new MessageDialogBox("A class with this name already exists.").show();
					return;
				}
			}
			
			ClassRoom classroom = new ClassRoom();
			classroom.setName(name);
			
			SynergyNetAppSystemService.Util.get().addClassRoom(classroom, new AsyncCallback<Void>() {
				@Override public void onSuccess(Void result) {}
				
				@Override
				public void onFailure(Throwable caught) {
					new MessageDialogBox("Error adding new classroom: " + caught.getMessage()).show();
				}
			});			

			createNameBox.setText("");
			classrooms.add(classroom);
			fillClassList();
		}
	}
	
	private void removeClass(){
		
		classrooms.remove(selectedClass);
		
		SynergyNetAppSystemService.Util.get().removeClassRoom(selectedClass, new AsyncCallback<Void>() {
			@Override public void onSuccess(Void result) {}
			
			@Override
			public void onFailure(Throwable caught) {
				new MessageDialogBox("Error removing class: " + caught.getMessage()).show();
			}
		});		
		
		fillClassList();
	}
	
	private void shutDownDB(){
		if (classesSelected.size() > 0){
			new MessageDialogBox("Please end all class sessions before shutting down the database.").show();
			return;
		}
		
		if (Window.confirm("Stopping the database will no longer allow you to log in students.")){
			SynergyNetAppSystemService.Util.get().stopDatabase(new AsyncCallback<Void>() {
				@Override public void onSuccess(Void result) {
					new MessageDialogBox("The database has been stopped.  Refresh this page to restart it.").show();
					DatabaseControlUI.getClassSelectionPanel().setVisible(false);
					DatabaseControlUI.getStudentAdminPanel().setVisible(false);
					DatabaseControlUI.removeDatabaseTabs();
				}
				
				@Override
				public void onFailure(Throwable caught) {
					new MessageDialogBox("Error stopping database: " + caught.getMessage()).show();
				}
			});
		}
	}

	public ArrayList<String> getClassesSelected() {
		return classesSelected;
	}

	/**
	 * @return the classrooms
	 */
	public ArrayList<ClassRoom> getClassrooms() {
		return classrooms;
	}
	
}
