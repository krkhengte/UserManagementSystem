package jfs.backend.user.service.Service;

import java.util.List;

import jfs.backend.user.service.bindings.ActivateAccount;
import jfs.backend.user.service.bindings.Login;
import jfs.backend.user.service.bindings.User;

public interface UserMasterService {


	public boolean saveUser(User user);

	public boolean activateUserAccount(ActivateAccount account);

	public List<User> getAllUsers();

	public User getUserById(Integer userId);

	public User getUserByEmail(String emailId);

	public boolean deleteUserById(Integer userId);

	public boolean chageAccountStatus(Integer userId, String accStatus);

	public String login(Login login);
	
	public String forgotPwd(String email);

}
