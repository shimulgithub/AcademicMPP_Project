package ui;

import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

import business.Author;
import business.Book;
import business.ControllerInterface;
import business.SystemController;
import business.constant.Constants;
import dataaccess.DataAccess;
import dataaccess.DataAccessFacade;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AddBookWindow extends Stage implements LibWindow {

	public static final AddBookWindow INSTANCE = new AddBookWindow();
	private TableView<Author> tableAuthorView = new TableView<Author>();
	private Label errorMessageLabel = new Label("");

	private boolean isInitialized = false;

	public boolean isInitialized() {
		return isInitialized;
	}

	public void isInitialized(boolean val) {
		isInitialized = val;
	}

	public void setData(ObservableList<Author> prods) {
		tableAuthorView.setItems(prods);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void init() {	

		this.setTitle("Add Book");
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));
		grid.setStyle(Constants.GRID_COLOR);

		Text label = new Text("Add Book");
		label.setFont(Font.font(Constants.FONT_TYPE, FontWeight.BOLD, 30));
		label.setFill(Color.DARKRED);
		HBox labelBox = new HBox(10);
		labelBox.setAlignment(Pos.CENTER);
		labelBox.getChildren().add(label);
		grid.add(labelBox, 0, 0, 2, 1);

		Label isbnLabel = new Label("ISBN number:");
		grid.add(isbnLabel, 0, 1, 1, 1);

		TextField isbnTextField = new TextField();
		grid.add(isbnTextField, 1, 1, 1, 1);

		Label titleLabel = new Label("Title:");
		grid.add(titleLabel, 0, 2, 1, 1);

		TextField titleTextField = new TextField();
		grid.add(titleTextField, 1, 2, 1, 1);

		Label checkoutLengthLabel = new Label("Checkout length:");
		grid.add(checkoutLengthLabel, 0, 3, 1, 1);		
		
		ComboBox checkoutLengthComboBox = new ComboBox(FXCollections
				 .observableArrayList(Constants.CHECK_OUT_LENGTH));
		checkoutLengthComboBox.getSelectionModel().selectFirst();
		grid.add(checkoutLengthComboBox, 1, 3, 1, 1);

		Label nbOfCopiesLabel = new Label("# Copies:");
		grid.add(nbOfCopiesLabel, 0, 4, 1, 1);

		TextField nbCopiesTextField = new TextField();
		grid.add(nbCopiesTextField, 1, 4, 1, 1);

		Label authorLabel = new Label("Authors:");
		grid.add(authorLabel, 0, 5, 1, 1);

		TableColumn<Author, String> authortFirstNameCol = new TableColumn<>("First name");
		authortFirstNameCol.setMinWidth(130);
		authortFirstNameCol.setCellValueFactory(new PropertyValueFactory<Author, String>("firstName"));

		TableColumn<Author, String> authortLastNameCol = new TableColumn<>("Last name");
		authortLastNameCol.setMinWidth(130);
		authortLastNameCol.setCellValueFactory(new PropertyValueFactory<Author, String>("firstName"));

		TableColumn<Author, String> authorPhoneNameCol = new TableColumn<>("Phone");
		authorPhoneNameCol.setMinWidth(130);
		authorPhoneNameCol.setCellValueFactory(new PropertyValueFactory<Author, String>("telephone"));

		tableAuthorView.getColumns().clear();
		tableAuthorView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		tableAuthorView.getColumns().addAll(authortFirstNameCol, authortLastNameCol, authorPhoneNameCol);
		grid.add(tableAuthorView, 0, 6, 2, 1);
		
		
		errorMessageLabel.setTextFill(Color.RED);
		grid.add(errorMessageLabel, 0, 7, 1, 1);
		
		Button saveBtn = new Button("Save");
		HBox hbBtn = new HBox(10);
		hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
		hbBtn.getChildren().add(saveBtn);
		grid.add(hbBtn, 1, 7, 3, 1);

		Scene scene = new Scene(grid);

		setScene(scene);

		this.bindAuthorToList();

		saveBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				String isbn = isbnTextField.getText();
				String title = titleTextField.getText();
				String maxCheckoutLengthString = checkoutLengthComboBox.getValue().toString();

				if (isbn.equals("") || title.equals("") || maxCheckoutLengthString.equals("")) {
					showErrorMessage("All input fields are required.");
					return;
				}			
				
				int maxLength = Integer.parseInt(maxCheckoutLengthString);
				String numberOfCopiesString = nbCopiesTextField.getText();
				int nbOfcopy = 1;
				if (numberOfCopiesString != null)
					nbOfcopy = Integer.parseInt(numberOfCopiesString) - 1;
				
				tableAuthorView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
				Collection<Author> authors = tableAuthorView.getSelectionModel().getSelectedItems();
				
				if(authors.isEmpty()) {					
					showErrorMessage("Author is not selected.");			
					return;
				}

				ControllerInterface c = new SystemController();
				Book book = new Book(isbn, title, maxLength, new ArrayList<>(authors));
				for (int i = 0; i < nbOfcopy; i++)
					book.addCopy();

				c.addBook(book);
				showErrorMessage("");

				Alert savedAlert = new Alert(AlertType.INFORMATION);
				savedAlert.setHeaderText("The book was added successfully.");
				Optional<ButtonType> option = savedAlert.showAndWait();

				if (option.get() == ButtonType.OK) {
					init();
				}

			}
		});

	}

	private void showErrorMessage(String errorMsg) {		
		errorMessageLabel.setText(errorMsg);
	}

	private void bindAuthorToList() {
		ControllerInterface c = new SystemController();
		this.tableAuthorView.getItems().clear();
		this.tableAuthorView.getItems().setAll(c.getAllAuthors());
	}

}