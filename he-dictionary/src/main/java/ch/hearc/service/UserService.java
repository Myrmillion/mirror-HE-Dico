package ch.hearc.service;

import java.util.Arrays;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import ch.hearc.models.User;
import ch.hearc.repository.UserRepository;


//////////////
//  UPDATE  //
//  CHAP_06 //
//////////////
@Service("userService")
public class UserService 
{

	@Autowired
	private UserRepository userRepository;
	

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	
	public User findByUsername(String username) 
	{
		return userRepository.findByUsername(username);
	}

	
	public void saveUser(User user) 
	{
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		userRepository.save(user);
	}
}