package jfs.backend.user.service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jfs.backend.user.service.Service.UserMasterService;
import jfs.backend.user.service.bindings.ActivateAccount;
import jfs.backend.user.service.bindings.Login;
import jfs.backend.user.service.bindings.User;

@RestController
public class UserMasterServiceController {

	@Autowired
	private UserMasterService userMasterService;
	
	@PostMapping("/createUser")
	public ResponseEntity<String> saveUser(@RequestBody User user){	
		boolean saveUser = this.userMasterService.saveUser(user);
		if (saveUser)	return new ResponseEntity<String>("Registration Success....!", HttpStatus.CREATED);
		else	return new ResponseEntity<String>("Registration Failed....!", HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@PostMapping("/activateAccount")
	public ResponseEntity<String> activateuserAccount(@RequestBody ActivateAccount account){	
		boolean isActivated = this.userMasterService.activateUserAccount(account);
		if (isActivated)	return new ResponseEntity<String>("Account Activated....!", HttpStatus.OK);
		else return new ResponseEntity<String>("Invalid Temporary Password....!", HttpStatus.BAD_REQUEST);
	}
	
	@GetMapping("/getAllUsers")
	public ResponseEntity<List<User>> getAllUsers(){
		List<User> allUsers = this.userMasterService.getAllUsers();
		return ResponseEntity.status(HttpStatus.CREATED).body(allUsers);
	}
	
	@GetMapping("/getSingleuserById/{userId}")
	public ResponseEntity<User> getUserById(@PathVariable Integer userId){
		User userById = this.userMasterService.getUserById(userId);
		return ResponseEntity.status(HttpStatus.CREATED).body(userById);	
	}
	
	@GetMapping("/getSingleuserByEmail/{email}")
	public ResponseEntity<User> getUserById(@PathVariable String email){
		User userByEmail = this.userMasterService.getUserByEmail(email);
		return ResponseEntity.status(HttpStatus.CREATED).body(userByEmail);
	}
	
	@DeleteMapping("/deleteUserById/{userId}")
	public ResponseEntity<String> deleteUserById(@PathVariable Integer userId){
		boolean isdeleted = this.userMasterService.deleteUserById(userId);
		if (isdeleted)	return new ResponseEntity<String>("User Deleted Successfull....!", HttpStatus.OK);
		else return new ResponseEntity<String>("Failed To Deleted....!", HttpStatus.INTERNAL_SERVER_ERROR);
	}
	

	
	@GetMapping("/chageAccountStatus/userId/{userId}/accountStatus/{accStatus}")
	public ResponseEntity<String> chageAccountStatus(@PathVariable Integer userId, @PathVariable String accStatus){
		boolean isChange = this.userMasterService.chageAccountStatus(userId, accStatus);
		if (isChange)	return new ResponseEntity<String>("Change Account Status Successfully...!", HttpStatus.OK);
		else return new ResponseEntity<String>("Failed To Change....!", HttpStatus.INTERNAL_SERVER_ERROR);
	}

	
	
	@PostMapping("/loginAccount")
	public ResponseEntity<String> login(@RequestBody Login login){   
		String login2 = this.userMasterService.login(login);
		return ResponseEntity.status(HttpStatus.CREATED).body(login2);
	}
	
	@GetMapping("/forgotPassword/{email}")
	public ResponseEntity<String> forgotPwd(@PathVariable String email){
		String forgotPwd = this.userMasterService.forgotPwd(email);
		return ResponseEntity.status(HttpStatus.CREATED).body(forgotPwd);
	}
}
