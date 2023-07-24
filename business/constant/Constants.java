package business.constant;

import javafx.scene.paint.Color;

public class Constants {
	public static final int MAX_DAY_BOOK_BORROWING = 21;
	public static final int MIN_DAY_BOOK_BORROWING = 7;
	
	public static final String[] CHECK_OUT_LENGTH = new String[] {
			String.valueOf(MAX_DAY_BOOK_BORROWING),
			String.valueOf(MIN_DAY_BOOK_BORROWING)
		};
	
		
	//font
	public static final String FONT_TYPE = "Lucida Sans Unicode";
	public static final String GRID_COLOR = "-fx-background-color: BEIGE;";
	
	public static Color green = Color.web("#034220");
	public static Color red = Color.FIREBRICK;

}
