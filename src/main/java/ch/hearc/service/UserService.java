package ch.hearc.service;

import java.util.Arrays;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import ch.hearc.repository.UserRepository;
import ch.hearc.model.Role;
import ch.hearc.model.User;
import ch.hearc.repository.RoleRepository;


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
	private RoleRepository roleRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	
	public User findByUsername(String username) 
	{
		return userRepository.findByUsername(username);
	}

	
	public void saveUser(User user) 
	{
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		Role userRole = roleRepository.findByRole("USER");
		user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
		userRepository.save(user);
	}
}