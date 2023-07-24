package business;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ControllerInterface {
	public void login(String id, String password) throws LoginException;

	public List<String> getAllMemberIds();

	public List<String> getAllBookIds();

	public List<Author> getAllAuthors();

	public void addBook(Book book);

	public void addLibraryMember(LibraryMember member);

	public List<LibraryMember> getLibraryMemberByID(String memberId);

	public List<Book> getAllBooks();

	public List<LibraryMember> getAllMemebers();

	public void saveCheckoutRecord(CheckoutRecord record);

	public void saveCheckoutEntry(CheckoutEntry entry);

	public Map<LocalDate, List<CheckoutEntry>> getCheckoutEntries(LibraryMember member);

	public Map<LibraryMember, List<CheckoutEntry>> getCheckoutEntryList(String isbn);

}
