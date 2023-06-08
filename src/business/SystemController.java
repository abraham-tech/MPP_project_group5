package business;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import dataaccess.Auth;
import dataaccess.DataAccess;
import dataaccess.DataAccessFacade;
import dataaccess.User;

public class SystemController implements ControllerInterface {
	public static Auth currentAuth = null;

	private DataAccess da = new DataAccessFacade();

	public void login(String id, String password) throws LoginException {
		HashMap<String, User> map = da.readUserMap();
		if (!map.containsKey(id)) {
			throw new LoginException("ID " + id + " not found");
		}
		String passwordFound = map.get(id).getPassword();
		if (!passwordFound.equals(password)) {
			throw new LoginException("Password incorrect");
		}
		currentAuth = map.get(id).getAuthorization();

	}

	@Override
	public List<String> allMemberIds() {
		List<String> retval = new ArrayList<>();
		retval.addAll(da.readMemberMap().keySet());
		return retval;
	}

	@Override
	public List<String> allBookIds() {
		List<String> retval = new ArrayList<>();
		retval.addAll(da.readBooksMap().keySet());
		return retval;
	}

	@Override
	public void saveMember(LibraryMember member) {
		da.saveNewMember(member);

	}

	@Override
	public Collection<Book> allBooks() {
		return da.readBooksMap().values();
	}

	@Override
	public Collection<LibraryMember> alLibraryMembers() {
		return da.readMemberMap().values();
	}

	@Override
	public void deleteMember(String memberId) {
		da.deleteMember(memberId);

	}

	@Override
	public LibraryMember getLibraryMemberById(String memberId) {
		Collection<LibraryMember> members = da.readMemberMap().values();
		for (LibraryMember member : members) {
			if (member.getMemberId().equals(memberId)) {
				return member;
			}
		}
		return null;
	}

	@Override
	public Book getBookByISBN(String isbn) {
		Collection<Book> books = da.readBooksMap().values();
		for (Book book : books) {
			if (book.getIsbn().equals(isbn)) {
				return book;
			}
		}
		return null;
	}

	@Override
	public void saveBook(Book book) {
		da.saveBook(book);
	}

}
