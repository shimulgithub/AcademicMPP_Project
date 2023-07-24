package business;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.UUID;

public class CheckoutEntry implements Serializable {

	private static final long serialVersionUID = 98714360920478488L;
	private CheckoutRecord record;
	private BookCopy copy;
	private LocalDate date;
	private LocalDate dueDate;
	String id;

	CheckoutEntry(CheckoutRecord record, BookCopy copy, LocalDate date) {

		this.id = UUID.randomUUID().toString();
		this.record = record;
		this.copy = copy;
		this.date = date;
		this.dueDate = getDueDate(copy, date);
	}

	private LocalDate getDueDate(BookCopy copy, LocalDate date) {
		Book book = copy.getBook();
		LocalDate duedate = date.plusDays(book.getMaxCheckoutLength());
		return duedate;
	}

	public String getId() {
		return this.id;
	}

	public BookCopy getCopy() {
		return this.copy;
	}

	public LocalDate getDueDate() {
		return this.dueDate;
	}

	@Override
	public String toString() {
		return "ISBN:  " + copy.getBook().getIsbn() 
				+ ",   Book: " + copy.getBook().getTitle() 
				+ ",   Checkout: " + date != null ? date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)) : "" 
				+ ",   Duedate: " + dueDate != null ? dueDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)) : "";
	}
}
