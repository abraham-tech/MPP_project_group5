package business;

import java.util.Collection;
import java.util.List;

public interface ControllerInterface {
	public void login(String id, String password) throws LoginException;
	public List<String> allMemberIds();
	public List<String> allBookIds();
	public void saveMember(LibraryMember member);
	public Collection<LibraryMember> alLibraryMembers();
	public void deleteMember(String memberId);
	public LibraryMember getLibraryMemberById(String memberId);
}
