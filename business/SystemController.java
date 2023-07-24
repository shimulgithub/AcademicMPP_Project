package business;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import business.util.Utils;
import dataaccess.Auth;
import dataaccess.DataAccess;
import dataaccess.DataAccessFacade;
import dataaccess.User;

public class SystemController implements ControllerInterface {
	public static Auth currentAuth = null;
	private DataAccess dataAccess = new DataAccessFacade();
	
	public void login(String id, String password) throws LoginException {
		if (Utils.isEmpty(id) || Utils.isEmpty(password)) {
			throw new LoginException("Username and Password can't be empty");
		}
		
		HashMap<String, User> map = dataAccess.readUserMap();
		if(map == null || !map.containsKey(id)) {
			throw new LoginException("ID " + id + " not found");
		}
		
		String passwordFound = map.get(id).getPassword();
		if (!passwordFound.equals(password)) {
			throw new LoginException("Password incorrect");
		}
		
		currentAuth = map.get(id).getAuthorization();
	}

	@Override
	public List<String> getAllMemberIds() {
		return new ArrayList<String>(dataAccess.readMemberMap().keySet());
	}

	@Override
	public List<String> getAllBookIds() {
		return new ArrayList<String>(dataAccess.readBooksMap().keySet());
	}

	@Override
	public List<Book> getAllBooks(){
		return new ArrayList<Book>(dataAccess.readBooksMap().values());
	}

	@Override
	public List<LibraryMember> getAllMemebers(){
		return new ArrayList<LibraryMember>(dataAccess.readMemberMap().values());
	}

	@Override
	public void saveCheckoutRecord(CheckoutRecord record){
		dataAccess.saveNewCheckoutRecord(record);
	}

	@Override
	public List<Author> getAllAuthors() {
		return new ArrayList<Author>(dataAccess.readAuthorMap().values());
	}

	@Override
	public void addBook(Book book) {
		dataAccess.saveBook(book);
	}

	@Override
	public void addLibraryMember(LibraryMember member) {
		dataAccess.saveNewMember(member);
	}

	@Override
	public void saveCheckoutEntry(CheckoutEntry entry){
		dataAccess.saveNewCheckoutEntry(entry);
	}

	public List<LibraryMember> getLibraryMemberByID(String memberId) {
		if (!Utils.isEmpty(memberId)) {
			HashMap<String, LibraryMember> memberMap = dataAccess.readMemberMap();
			LibraryMember member = memberMap.get(memberId);
			return Arrays.asList(member);
		}
		return new ArrayList<LibraryMember>();
	}

	@Override
	public Map<LocalDate, List<CheckoutEntry>> getCheckoutEntries(LibraryMember member) {
		HashMap<String, CheckoutRecord> checkoutHashMap = dataAccess.readCheckoutRecordMap();
		Collection<CheckoutRecord> checkoutRecords = checkoutHashMap.values();
		
		Map<LocalDate, List<CheckoutEntry>> checkoutEntriesMap = new HashMap<>();
		
		checkoutRecords.forEach(e -> {
			if (e.getMember().equals(member)) {
				List<CheckoutEntry> existList = checkoutEntriesMap.get(e.getDate());
				
				if (existList == null) {
					existList = new ArrayList<>();
				}
				
				if (e.getEntries() != null) {
					existList.addAll(e.getEntries());
				}
				
				checkoutEntriesMap.put(e.getDate(), existList);
			}
		});
		
		return checkoutEntriesMap;
	}

	@Override
	public Map<LibraryMember, List<CheckoutEntry>> getCheckoutEntryList(String isbn) {
		HashMap<String, CheckoutRecord> checkoutHashMap = dataAccess.readCheckoutRecordMap();
		Collection<CheckoutRecord> checkoutRecords = checkoutHashMap.values();
		
		Map<LibraryMember, List<CheckoutEntry>> memberEntryMap = new HashMap<>();
		
		checkoutRecords.forEach(e -> {
			List<CheckoutEntry> overdueEntries = new ArrayList<>();

			List<CheckoutEntry> entries = e.getEntries();

			for (CheckoutEntry entry : entries) {
				if (LocalDate.now().isBefore(entry.getDueDate())) {
					if (Utils.isEmpty(isbn)
							|| (!Utils.isEmpty(isbn) 
									&& isbn.equalsIgnoreCase(entry.getCopy().getBook().getIsbn()))) {
						overdueEntries.add(entry);
					}
				}				
			}

			if (memberEntryMap.get(e.getMember()) != null) {
				overdueEntries.addAll(memberEntryMap.get(e.getMember()));
			}
			
			memberEntryMap.put(e.getMember(), overdueEntries);

		});

		return memberEntryMap;
	}

}
