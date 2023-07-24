package ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import business.Address;
import business.ControllerInterface;
import business.LibraryMember;
import business.SystemController;
import business.constant.Constants;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import ui.util.NumberTextField;


public class AddMemberWindow extends Stage implements LibWindow{

	public static final AddMemberWindow INSTANCE = new AddMemberWindow();
	private TableView<LibraryMember> tableMemberView = new TableView<LibraryMember>();
	TextField memberIDTextField;
	TextField firstNameTextField;
	TextField lastNameTextField;
	TextField streetTextField;
	TextField cityTextField;
	TextField stateTextField;
	TextField zipCodeTextField;
	TextField telePhoneTextField;
	private Label errorMessageLabel = new Label("");
	private boolean isInitialized = false;
	private ControllerInterface sysController = new SystemController();
	public boolean isInitialized() {
		return isInitialized;
	}
	public void isInitialized(boolean val) {
		isInitialized = val;
	}
	
	public void setData(ObservableList<LibraryMember> prods) {
		tableMemberView.setItems(prods);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void init() {
		
		this.setTitle("Member Management");
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(5);
		grid.setPadding(new Insets(15, 15, 15, 15));
		grid.setStyle(Constants.GRID_COLOR);

		Text label = new Text("Add/Edit Library Member");
		label.setFont(Font.font(Constants.FONT_TYPE, FontWeight.BOLD, 30));
		label.setFill(Color.DARKRED);
		HBox labelBox = new HBox(10);
		labelBox.setAlignment(Pos.CENTER);
		labelBox.getChildren().add(label);
		int row = 0;
		grid.add(labelBox, 0, row, 4, 1);
		
		row++;
		Label isbnLabel = new Label("Member ID:");
		grid.add(isbnLabel, 0, row, 1, 1);

		memberIDTextField = new NumberTextField();
		grid.add(memberIDTextField, 1, row, 1, 1);
		memberIDTextField.setMaxWidth(300);
		
		Label telephoneLabel = new Label("Telephone:");
		grid.add(telephoneLabel, 2, row, 1, 1);

		telePhoneTextField = new NumberTextField();
		grid.add(telePhoneTextField, 3, row, 1, 1);
		telePhoneTextField.setMaxWidth(200);		
		
		row++;
		Label firstNameLabel = new Label("First Name:");
		grid.add(firstNameLabel, 0, row, 1, 1);

		firstNameTextField = new TextField();
		grid.add(firstNameTextField, 1, row, 1, 1);
		
		Label lastNameLabel = new Label("Last Name:");
		grid.add(lastNameLabel, 2, row, 1, 1);

		lastNameTextField = new TextField();
		grid.add(lastNameTextField, 3,row, 1, 1);
		
		
		row++;
		Label streetLabel = new Label("Street:");
		grid.add(streetLabel, 0, row, 1, 1);

		streetTextField = new TextField();
		grid.add(streetTextField, 1, row, 1, 1);
		
		Label cityLabel = new Label("City:");
		grid.add(cityLabel, 2, row, 1, 1);

		cityTextField = new TextField();
		grid.add(cityTextField, 3, row, 1, 1);
		
		row++;
		Label stateLabel = new Label("State:");
		grid.add(stateLabel, 0, row, 1, 1);

		stateTextField = new TextField();
		grid.add(stateTextField, 1, row, 1, 1);
		
		Label zipCodeLabel = new Label("Zip Code:");
		grid.add(zipCodeLabel, 2, row, 1, 1);

		zipCodeTextField = new NumberTextField();
		grid.add(zipCodeTextField, 3, row, 1, 1);	
	
		row++;		
		
		errorMessageLabel.setTextFill(Color.RED);
		grid.add(errorMessageLabel, 0, row, 2, 1);
		
		Button saveBtn = new Button("Save");
		saveBtn.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
        	public void handle(ActionEvent e) {
        		showErrorMessage("");
        		String memberId = memberIDTextField.getText().trim();
        		List<LibraryMember> libraryMembers = getLibraryMembers();
        		List<String> libraryMemberIds = libraryMembers.stream()        				
        				.map(x -> x.getMemberId())
        				.collect(Collectors.toList());
        		if(libraryMemberIds.contains(memberId)) {        			
        			showErrorMessage("Member with this Id already exists.");
        			memberIDTextField.requestFocus();
        			return;
        		}
        		
        		Address add = new Address(streetTextField.getText().trim(), cityTextField.getText().trim(), 
        				stateTextField.getText().trim(), zipCodeTextField.getText().trim());
        		LibraryMember newMember = new LibraryMember(memberIDTextField.getText().trim(), firstNameTextField.getText().trim(), 
        				lastNameTextField.getText().trim(), telePhoneTextField.getText().trim(), add);
        		boolean success = addNewMember(newMember);
        		if(success==true) textFieldClear();        		
        		bindMemberToList(getLibraryMembers());
        	}
        });
		Button editBtn = new Button("Edit");
		editBtn.setDisable(true);
		editBtn.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
        	public void handle(ActionEvent e) {
        		Address add = new Address(streetTextField.getText().trim(), cityTextField.getText().trim(), 
        				stateTextField.getText().trim(), zipCodeTextField.getText().trim());
        		LibraryMember newMember = new LibraryMember(memberIDTextField.getText().trim(), firstNameTextField.getText().trim(), 
        				lastNameTextField.getText().trim(), telePhoneTextField.getText().trim(), add);
        		addNewMember(newMember);
        		textFieldClear();
        		memberIDTextField.setDisable(false);
        		saveBtn.setDisable(false);
        		editBtn.setDisable(true);
        		bindMemberToList(getLibraryMembers());
        	}
        });
		Button clearBtn = new Button("Clear");
		clearBtn.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
        	public void handle(ActionEvent e) {        		
        		textFieldClear();
        		memberIDTextField.setDisable(false);
        		errorMessageLabel.setText("");
        		saveBtn.setDisable(false);	
	        	editBtn.setDisable(true);
        		
        	}
        });
		
		
		
		HBox hbBtn = new HBox(10);
		hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
		
		hbBtn.getChildren().add(saveBtn);
		hbBtn.getChildren().add(editBtn);
		hbBtn.getChildren().add(clearBtn);		
		
		grid.add(hbBtn, 2, row, 2, 1);
		
		HBox righttBox = new HBox(10);
		righttBox.setAlignment(Pos.TOP_RIGHT);
		
		Label membersLabel = new Label("Members:");
		
		row++;
		grid.add(membersLabel, 0, row, 1, 1);
		
		TableColumn<LibraryMember, String> memberIDCol = new TableColumn<>("Member Id");
		memberIDCol.setMinWidth(105);
		memberIDCol.setCellValueFactory(new PropertyValueFactory<LibraryMember, String>("memberId"));
		memberIDCol.setCellFactory(TextFieldTableCell.forTableColumn());
		
		TableColumn<LibraryMember, String> memberFirstNameCol = new TableColumn<>("First Name");
		memberFirstNameCol.setMinWidth(110);
		memberFirstNameCol.setCellValueFactory(new PropertyValueFactory<LibraryMember, String>("firstName"));
		memberFirstNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
		
		TableColumn<LibraryMember, String> memberLastNameCol = new TableColumn<>("Last Name");
		memberLastNameCol.setMinWidth(120);
		memberLastNameCol.setCellValueFactory(new PropertyValueFactory<LibraryMember, String>("lastName"));
		memberLastNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
		
		TableColumn<LibraryMember, String> telephoneCol = new TableColumn<>("Telephone");
		telephoneCol.setMinWidth(110);
		telephoneCol.setCellValueFactory(new PropertyValueFactory<LibraryMember, String>("telephone"));
		telephoneCol.setCellFactory(TextFieldTableCell.forTableColumn());
		
		tableMemberView.getColumns().clear();
		tableMemberView.getColumns().addAll(memberIDCol, memberFirstNameCol, memberLastNameCol, telephoneCol);
		
		righttBox.getChildren().add(tableMemberView);
		row++;
		grid.add(righttBox, 0, row, 4, 1);
		
		tableMemberView.setOnMouseClicked((MouseEvent event) -> {
			showErrorMessage("");
	        if(event.getButton().equals(MouseButton.PRIMARY)){
	        	if(!doSelectMember()) return;
	        	memberIDTextField.setDisable(true);
	        	saveBtn.setDisable(true);	
	        	editBtn.setDisable(false);
	        	
	        }
	    });
		tableMemberView.setOnKeyReleased(new EventHandler<KeyEvent>() {
	        @Override
	        public void handle(KeyEvent ke)
	        {
	            if (ke.getCode().equals(KeyCode.UP) || ke.getCode().equals(KeyCode.DOWN)) {
		            doSelectMember();		                  
	            }
	        }
	    });
		this.bindMemberToList(getLibraryMembers());
		
		Screen screen = Screen.getPrimary();
		Rectangle2D bounds = screen.getVisualBounds();

		Scene scene = new Scene(grid, bounds.getWidth()*0.55, bounds.getHeight()*0.955);
        setScene(scene);
	}
	
	private List<LibraryMember> getLibraryMembers() {		
		return sysController.getAllMemebers();
	}
	
	private void bindMemberToList(List<LibraryMember> members) {
		this.tableMemberView.getItems().clear();
		this.tableMemberView.getItems().setAll(members);
	}
	
	private boolean addNewMember(LibraryMember newMember) {
		if(newMember.getMemberId().isEmpty()) {			
			showErrorMessage("Missing member id!");
			memberIDTextField.requestFocus();
			return false;
		}
		if(newMember.getFirstName().isEmpty()) {
			showErrorMessage("Missing first name!");
			firstNameTextField.requestFocus();
			return false;
		}
		if(newMember.getLastName().isEmpty()) {
			showErrorMessage("Missing last name!");
			lastNameTextField.requestFocus();
			return false;
		}
		if(newMember.getAddress().getStreet().isEmpty()) {
			showErrorMessage("Missing street!");
			streetTextField.requestFocus();
			return false;
		}
		if(newMember.getAddress().getCity().isEmpty()) {
			showErrorMessage("Missing city!");
			cityTextField.requestFocus();
			return false;
		}
		if(newMember.getAddress().getState().isEmpty()) {
			showErrorMessage("Missing state!");
			stateTextField.requestFocus();
			return false;
		}
		if(newMember.getAddress().getZip().isEmpty()) {
			showErrorMessage("Missing zip code!");
			zipCodeTextField.requestFocus();
			return false;
		}
		if(newMember.getTelephone().isEmpty()) {
			showErrorMessage("Missing telephone!");
			telePhoneTextField.requestFocus();
			return false;
		}
		
		sysController.addLibraryMember(newMember);
		return true;
	}
	private boolean doSelectMember() {
		LibraryMember selMember = tableMemberView.getSelectionModel().getSelectedItem();
		if(selMember==null) {
			showErrorMessage("Invalid cell selected!!!");
			return false;
		}
        memberIDTextField.setText(selMember.getMemberId());
        firstNameTextField.setText(selMember.getFirstName());
        lastNameTextField.setText(selMember.getLastName());
        streetTextField.setText(selMember.getAddress().getStreet());
        cityTextField.setText(selMember.getAddress().getCity());
        stateTextField.setText(selMember.getAddress().getState());
        zipCodeTextField.setText(selMember.getAddress().getZip());
        telePhoneTextField.setText(selMember.getTelephone());
        
        return true;
	}
	
	public void textFieldClear() {
		memberIDTextField.clear();
		firstNameTextField.clear();
        lastNameTextField.clear();
        streetTextField.clear();
        cityTextField.clear();
        stateTextField.clear();
        zipCodeTextField.clear();
        telePhoneTextField.clear();
	}
	
	private void showErrorMessage(String errorMsg) {		
		errorMessageLabel.setText(errorMsg);
	}
}
