package ui;

import java.util.Collections;
import java.util.List;

import business.ControllerInterface;
import business.SystemController;
import business.constant.Constants;
import dataaccess.Auth;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class MainWindow extends Stage implements LibWindow {

	public static final MainWindow INSTANCE = new MainWindow();

	private boolean isInitialized = false;

	public void isInitialized(boolean val) {
		this.isInitialized = val;
	}

	private Text messageBar = new Text();

	public void clear() {
		messageBar.setText("");
	}

	private MainWindow() {
	}

	@Override
	public void init() {
		this.setTitle("Library Management System");
		VBox topContainer = new VBox();
		topContainer.setId("top-container");
		MenuBar mainMenu = new MenuBar();
		VBox imageHolder = new VBox();
		Image image = new Image("ui/library.jpg", 400, 300, false, false);

		// simply displays in ImageView the image as is
		ImageView iv = new ImageView();
		iv.setImage(image);
		imageHolder.getChildren().add(iv);
		imageHolder.setAlignment(Pos.CENTER);
		HBox splashBox = new HBox();
		Label splashLabel = new Label("Library Management System");
		splashLabel.setFont(Font.font(Constants.FONT_TYPE, FontWeight.BOLD, 30));
		splashLabel.setTextFill(Color.DARKRED);
		splashBox.getChildren().add(splashLabel);
		splashBox.setAlignment(Pos.CENTER);

		topContainer.getChildren().add(mainMenu);
		topContainer.getChildren().add(splashBox);
		topContainer.getChildren().add(imageHolder);
		topContainer.setStyle(Constants.GRID_COLOR);

		if (SystemController.currentAuth == Auth.LIBRARIAN) {
			addMenuLibrarian(mainMenu);
		} else if (SystemController.currentAuth == Auth.ADMIN) {
			addMenuAdmin(mainMenu);
		} else if (SystemController.currentAuth == Auth.BOTH) {
			addMenuBoth(mainMenu);
		}
		addMenuLogout(mainMenu);
		Screen screen = Screen.getPrimary();
		Rectangle2D bounds = screen.getVisualBounds();
		Scene scene = new Scene(topContainer, bounds.getWidth() * 0.5, bounds.getHeight() * 0.6);
		setScene(scene);

		// setMaximized(true);

	}

	@Override
	public boolean isInitialized() {
		return this.isInitialized;
	}

	public void addMenuBoth(MenuBar mainMenu) {
		addMenuLibrarian(mainMenu);
		addMenuAdmin(mainMenu);
	}

	public void addMenuLibrarian(MenuBar mainMenu) {
		Menu optionsMenu = new Menu("Librarian");

		MenuItem checkout = new MenuItem("Checkout Book");

		checkout.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				launchCheckoutWindow();
			}
		});

		MenuItem printCheckout = new MenuItem("Print Checkout Record");

		printCheckout.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				// Start.hideAllWindows();
				if (!PrintCheckoutRecordWindow.INSTANCE.isInitialized()) {
					PrintCheckoutRecordWindow.INSTANCE.init();
				}
				PrintCheckoutRecordWindow.INSTANCE.show();
			}
		});
		/*
		MenuItem overdueCheckout = new MenuItem("Overdue Checkout");
		overdueCheckout.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				// Start.hideAllWindows();
				if (!OverdueBookCheckingWindow.INSTANCE.isInitialized()) {
					OverdueBookCheckingWindow.INSTANCE.init();
				}
				OverdueBookCheckingWindow.INSTANCE.show();

			}
		}); */
		optionsMenu.getItems().addAll(checkout, printCheckout);//, overdueCheckout);
		mainMenu.getMenus().addAll(optionsMenu);
	}

	public void addMenuAdmin(MenuBar mainMenu) {
		Menu optionsMenu = new Menu("Admin");

		MenuItem addMember = new MenuItem("Add/Edit Member");
		addMember.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (!AddMemberWindow.INSTANCE.isInitialized()) {
					AddMemberWindow.INSTANCE.init();
				}
				AddMemberWindow.INSTANCE.show();
			}
		});

		MenuItem addBook = new MenuItem("Add Book");
		addBook.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (!AddBookWindow.INSTANCE.isInitialized()) {
					AddBookWindow.INSTANCE.init();
				}
				AddBookWindow.INSTANCE.show();
			}
		});

		MenuItem addCopy = new MenuItem("Add Copy");
		addCopy.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				launchCheckoutWindow();
			}
		});
		
		optionsMenu.getItems().addAll(addMember, addBook, addCopy);
		mainMenu.getMenus().addAll(optionsMenu);
	}

	public void addMenuLogout(MenuBar mainMenu) {
		Menu optionsMenu = new Menu("Exit");
		MenuItem logout = new MenuItem("Logout");
		
		logout.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {				
				Start.hideAllWindows();
				Start.primStage().show();
			}
		});

		optionsMenu.getItems().addAll(logout);
		mainMenu.getMenus().addAll(optionsMenu);
	}

	private void launchCheckoutWindow() {
		if (!BooksWindow.INSTANCE.isInitialized()) {
			BooksWindow.INSTANCE.init();
		}

		BooksWindow.INSTANCE.show();
	}

}
