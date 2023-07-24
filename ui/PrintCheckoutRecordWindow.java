package ui;

import java.util.List;

import business.ControllerInterface;
import business.LibraryMember;
import business.SystemController;
import business.constant.Constants;
import business.util.Utils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class PrintCheckoutRecordWindow extends Stage implements LibWindow {
	public static final PrintCheckoutRecordWindow INSTANCE = new PrintCheckoutRecordWindow();
	private TableView<LibraryMember> tableMemberView = new TableView<LibraryMember>();
	private boolean isInitialized = false;
	private ControllerInterface ci;

	public boolean isInitialized() {
		return isInitialized;
	}

	public void isInitialized(boolean val) {
		isInitialized = val;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void init() {
		this.setTitle("Print Checkout Record");
		ci = new SystemController();
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));
		grid.setStyle(Constants.GRID_COLOR);

		Text label = new Text("Member Checkout Records");
		label.setFont(Font.font(Constants.FONT_TYPE, FontWeight.BOLD, 30));
		label.setFill(Color.DARKRED);
		HBox labelBox = new HBox(10);
		labelBox.setAlignment(Pos.CENTER);
		labelBox.getChildren().add(label);
		grid.add(labelBox, 0, 0, 3, 1);

		Label memberIdLabel = new Label("Member Id:");
		grid.add(memberIdLabel, 0, 1, 1, 1);

		TextField memberIdTextField = new TextField();
		grid.add(memberIdTextField, 1, 1, 1, 1);

		memberIdTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent ke) {
				if (ke.getCode().equals(KeyCode.ENTER)) {
					doSearchMember(memberIdTextField.getText());

				}
			}
		});
		
		Button searchMemberBtn = new Button("Search member");
		HBox hbBtn = new HBox(10);
		hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
		hbBtn.getChildren().add(searchMemberBtn);
		grid.add(hbBtn, 2, 1, 1, 1);

		TableColumn<LibraryMember, String> memberIdCol = new TableColumn<>("ID");
		memberIdCol.setMinWidth(150);		
		memberIdCol.setCellValueFactory(new PropertyValueFactory<LibraryMember, String>("memberId"));
		memberIdCol.setCellFactory(TextFieldTableCell.forTableColumn());

		TableColumn<LibraryMember, String> memberFirstNameCol = new TableColumn<>("First name");
		memberFirstNameCol.setMinWidth(150);		
		memberFirstNameCol.setCellValueFactory(new PropertyValueFactory<LibraryMember, String>("firstName"));
		memberFirstNameCol.setCellFactory(TextFieldTableCell.forTableColumn());

		TableColumn<LibraryMember, String> memberLastNameCol = new TableColumn<>("Last name");
		memberLastNameCol.setMinWidth(150);		
		memberLastNameCol.setCellValueFactory(new PropertyValueFactory<LibraryMember, String>("lastName"));
		memberLastNameCol.setCellFactory(TextFieldTableCell.forTableColumn());

		tableMemberView.getColumns().clear();
		tableMemberView.getColumns().addAll(memberIdCol, memberFirstNameCol, memberLastNameCol);
		grid.add(tableMemberView, 0, 2, 3, 1);

		
		this.bindMemberToList(ci.getAllMemebers());

		Text printToConsoleLabel = new Text("Hint: Double click on member to print checkout details");
		printToConsoleLabel.setFill(Color.RED);
		grid.add(printToConsoleLabel, 0, 3, 3, 1);
		

		Scene scene = new Scene(grid);

		setScene(scene);

		searchMemberBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				String memberId = memberIdTextField.getText();
				doSearchMember(memberId);
			}
		});

		tableMemberView.setRowFactory(tv -> {
			TableRow<LibraryMember> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!row.isEmpty())) {
					LibraryMember member = row.getItem();
					ConsoleViewWindow.INSTANCE.setValue(member);
					ConsoleViewWindow.INSTANCE.init();
					ConsoleViewWindow.INSTANCE.show();
				}
			});
			return row;
		});

	}
	
	private void doSearchMember(String memberId) {		
		if (!Utils.isEmpty(memberId)) {
			bindMemberToList(ci.getLibraryMemberByID(memberId));
		} else {			
			bindMemberToList(ci.getAllMemebers());
		}
	}

	private void bindMemberToList(List<LibraryMember> members) {
		this.tableMemberView.getItems().clear();
		this.tableMemberView.getItems().setAll(members);
	}

}
