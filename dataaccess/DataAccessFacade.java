package dataaccess;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import business.Author;
import business.Book;
import business.BookCopy;
import business.CheckoutEntry;
import business.CheckoutRecord;
import business.LibraryMember;
import dataaccess.DataAccessFacade.StorageType;


public class DataAccessFacade implements DataAccess {
	
	enum StorageType {

		BOOKS,
		MEMBERS,
		CHECKOUTRECORD,
		CHECKOUTENTRY,
		USERS,
		AUTHORS
	}
	
	public static final String OUTPUT_DIR = System.getProperty("user.dir") 
											+ File.separator + "src" 
											+ File.separator + "dataaccess" 
											+ File.separator + "storage";
	
	public static final String DATE_PATTERN = "MM/dd/yyyy";
	
	public void saveNewMember(LibraryMember member) {
		Map<String, LibraryMember> mems = readMemberMap();
		if (mems == null) {
			mems = new HashMap<String, LibraryMember>();
		}
		
		mems.put(member.getMemberId(), member);
		
		saveToStorage(StorageType.MEMBERS, mems);	
	}
	
	public void saveNewCheckoutRecord(CheckoutRecord record) {
		Map<String, CheckoutRecord> records = readCheckoutRecordMap();
		if (records == null) {
			records = new HashMap<String, CheckoutRecord>();
		}
		
		records.put(record.getId(), record);
		
		saveToStorage(StorageType.CHECKOUTRECORD, records);	
	}
	
	public void saveNewCheckoutEntry(CheckoutEntry entry) {
		Map<String, CheckoutEntry> entries = readEntryRecordMap();
		if (entries == null) {
			entries = new HashMap<String, CheckoutEntry>();
		}
		
		entries.put(entry.getId(), entry);
		
		saveToStorage(StorageType.CHECKOUTENTRY, entries);	
	}	
	
	public void saveBook(Book book) {
		HashMap<String, Book> books = readBooksMap();
		if (books == null) {
			books = new HashMap<String, Book>();
		}
		
		books.put(book.getIsbn(), book);
		
		saveToStorage(StorageType.BOOKS, books);	
	}
	
	@SuppressWarnings("unchecked")
	public  HashMap<String,Book> readBooksMap() {
		//Returns a Map with name/value pairs being isbn -> Book
		return (HashMap<String,Book>) readFromStorage(StorageType.BOOKS);
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, LibraryMember> readMemberMap() {
		//Returns a Map with name/value pairs being memberId -> LibraryMember
		return (HashMap<String, LibraryMember>) readFromStorage(StorageType.MEMBERS);
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, CheckoutRecord> readCheckoutRecordMap() {
		//Returns a Map with name/value pairs being memberId -> LibraryMember
		return (HashMap<String, CheckoutRecord>) readFromStorage(StorageType.CHECKOUTRECORD);
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, CheckoutEntry> readEntryRecordMap() {
		//Returns a Map with name/value pairs being memberId -> LibraryMember
		return (HashMap<String, CheckoutEntry>) readFromStorage(StorageType.CHECKOUTENTRY);
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, User> readUserMap() {
		//Returns a Map with name/value pairs being userId -> User
		return (HashMap<String, User>)readFromStorage(StorageType.USERS);
	}
	
	
	/////load methods - these place test data into the storage area
	///// - used just once at startup  
		
	static void loadBookMap(List<Book> bookList) {
		Map<String, Book> books = new HashMap<String, Book>();
		bookList.forEach(book -> books.put(book.getIsbn(), book));
		saveToStorage(StorageType.BOOKS, books);
	}
	static void loadUserMap(List<User> userList) {
		Map<String, User> users = new HashMap<String, User>();
		userList.forEach(user -> users.put(user.getId(), user));
		saveToStorage(StorageType.USERS, users);
	}
 
	static void loadMemberMap(List<LibraryMember> memberList) {
		Map<String, LibraryMember> members = new HashMap<String, LibraryMember>();
		memberList.forEach(member -> members.put(member.getMemberId(), member));
		saveToStorage(StorageType.MEMBERS, members);
	}
	
	static void loadAuthorMap(List<Author> allAuthors) {
		Map<String, Author> authors = new HashMap<>();
		allAuthors.forEach(author-> authors.put(author.toString(), author));
		saveToStorage(StorageType.AUTHORS, authors);
	}
	@SuppressWarnings("unchecked")
	public HashMap<String, Author> readAuthorMap(){
		return (HashMap<String,Author>) readFromStorage(StorageType.AUTHORS);
	}
	
	static void saveToStorage(StorageType type, Object ob) {
		Path path = FileSystems.getDefault().getPath(OUTPUT_DIR, type.toString());
		
		try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(path))) {
			out.writeObject(ob);
		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
		}
	}
	
	static Object readFromStorage(StorageType type) {
		Path path = FileSystems.getDefault().getPath(OUTPUT_DIR, type.toString());
		
		try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(path))) {
			return in.readObject();
		} catch (IOException | ClassNotFoundException ioe) {
			System.out.println(ioe.getMessage());
			return null;
		}
	}	
	
	final static class Pair<S,T> implements Serializable{
		
		S first;
		T second;
		
		Pair(S s, T t) {
			first = s;
			second = t;
		}
		
		@Override 
		public boolean equals(Object ob) {
			if(ob == null) return false;
			if(this == ob) return true;
			
			if(ob.getClass() != getClass()) return false;
			
			@SuppressWarnings("unchecked")
			Pair<S,T> p = (Pair<S,T>)ob;
			return p.first.equals(first) && p.second.equals(second);
		}
		
		@Override 
		public int hashCode() {
			return first.hashCode() + 5 * second.hashCode();
		}
		
		@Override
		public String toString() {
			return "(" + first.toString() + ", " + second.toString() + ")";
		}
		
		private static final long serialVersionUID = 5399827794066637059L;
	}

}
