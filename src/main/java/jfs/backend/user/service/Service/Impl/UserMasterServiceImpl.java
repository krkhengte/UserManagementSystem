package jfs.backend.user.service.Service.Impl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import jfs.backend.user.service.Service.UserMasterService;
import jfs.backend.user.service.bindings.ActivateAccount;
import jfs.backend.user.service.bindings.Login;
import jfs.backend.user.service.bindings.User;
import jfs.backend.user.service.entity.UserMaster;
import jfs.backend.user.service.repo.UserMasterRepo;
import jfs.backend.user.service.utils.EmailUtils;

@Service
public class UserMasterServiceImpl implements UserMasterService {

	@Autowired
	private UserMasterRepo userMasterRepo;

	@Autowired
	private EmailUtils emailUtils;

	@Override
	public boolean saveUser(User user) {

		UserMaster uMaster = new UserMaster();

		BeanUtils.copyProperties(user, uMaster);
		String password = UUID.randomUUID().toString();

		uMaster.setPassword(password);
		uMaster.setAccStatus("In-Active");

		String fullName = user.getFullName();

		System.out.println(fullName);
		String subject = "Your Registration Is Success";
		//String fileName = "REG-EMAIL-BODY.txt";
		String body = readEmailBody(fullName, password);

		emailUtils.sendEmail(user.getEmail(), subject, body);

		UserMaster save = this.userMasterRepo.save(uMaster);

		return save.getUserId() != null;
	}

	// read the body which is present in .txt file

	private String readEmailBody(String fullName, String pwd) {

		String url = "";
		String mailBody = null;

		StringBuffer buffer = new StringBuffer();

		try {

			FileReader fr = new FileReader("C:\\Users\\KARTIK\\WorkSpace\\spring_ws\\User_Management-Application\\REG-EMAIL-BODY.txt");

			BufferedReader br = new BufferedReader(fr);

			String line = br.readLine();
			while (line != null) {
				buffer.append(line);
				line = br.readLine();
			}
			br.close();
			mailBody = buffer.toString();

			mailBody = mailBody.replace("{FULLNAME}", fullName)
					.replace("{TEMP-PWD}", pwd)
					.replace("{URL}", url)
					.replace("{PWD}", pwd);

		} catch (Exception e) {
			// TODO: handle exception

			e.printStackTrace();
		}
		return mailBody;
	}

	@Override
	public boolean activateUserAccount(ActivateAccount account) {

		UserMaster uMaster = new UserMaster();

		uMaster.setEmail(account.getEmail());
		uMaster.setPassword(account.getTempPwd());

		Example<UserMaster> uExample = Example.of(uMaster);

		List<UserMaster> findAll = userMasterRepo.findAll(uExample);

		if (findAll.isEmpty()) {
			return false;
		} else {
			UserMaster userMaster = findAll.get(0);
			userMaster.setPassword(account.getNewPwd());
			userMaster.setAccStatus("Active");
			userMasterRepo.save(userMaster);
			return true;

		}

	}

	@Override
	public List<User> getAllUsers() {

		List<UserMaster> findAll = this.userMasterRepo.findAll();

		List<User> listOfUsers = new ArrayList<>();

		for (UserMaster uMaster : findAll) {

			User user = new User();

			BeanUtils.copyProperties(uMaster, user);

			listOfUsers.add(user);

		}

		return listOfUsers;
	}

	@Override
	public User getUserById(Integer userId) {

		Optional<UserMaster> uMaster = this.userMasterRepo.findById(userId);

		User user = new User();
		if (uMaster.isPresent()) {
			UserMaster userMaster = uMaster.get();

			BeanUtils.copyProperties(userMaster, user);
		}
		return user;
	}

	@Override
	public boolean deleteUserById(Integer userId) {

		Optional<UserMaster> uMaster = this.userMasterRepo.findById(userId);

		User user = new User();
		if (uMaster.isPresent()) {

			userMasterRepo.deleteById(userId);

			return true;
		} else {

			return false;
		}

	}

	@Override
	public boolean chageAccountStatus(Integer userId, String accStatus) {

		Optional<UserMaster> uMaster = userMasterRepo.findById(userId);

		if (uMaster.isPresent()) {
			UserMaster userMaster = uMaster.get();
			System.out.println(userMaster.getAccStatus());
			userMaster.setAccStatus(accStatus);
			System.out.println(userMaster.getAccStatus());
			userMasterRepo.save(userMaster);
			return true;

		}

		return false;
	}

	@Override
	public String login(Login login) {

		UserMaster uMaster = new UserMaster();
		uMaster.setEmail(login.getEmail());
		uMaster.setPassword(login.getPassword());

		Example<UserMaster> of = Example.of(uMaster);

		List<UserMaster> findAll = userMasterRepo.findAll(of);

		if (findAll.isEmpty())
			return "Invalid Credentials";
		else {
			UserMaster userMaster = findAll.get(0);

			if (userMaster.getAccStatus().equals("Active"))
				return "Success";

			else
				return "Account not Activated";
		}
	}

	@Override
	public User getUserByEmail(String email) {

		UserMaster uMaster = this.userMasterRepo.findByEmail(email);

		User user = new User();

		BeanUtils.copyProperties(uMaster, user);

		return user;
	}

	@Override
	public String forgotPwd(String email) {

		UserMaster entity = this.userMasterRepo.findByEmail(email);

		if (entity == null) {

			return "Invalid Email";
		}

		String subject = "Forgot Password";
		String fileName = "REG-EMAIL-BODY2.txt";

		String body = readEmailBody(entity.getFullName(), entity.getPassword());
		boolean sendEmail = emailUtils.sendEmail(fileName, subject, body);

		if (sendEmail) {

			return "Password Sent to Registered email";
		}

		return null;
	}

}
