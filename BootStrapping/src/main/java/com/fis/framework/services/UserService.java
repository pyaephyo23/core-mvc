package com.fis.framework.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fis.framework.entities.User;
import com.fis.framework.repositories.UserRepository;

@Service
public class UserService {
	private final UserRepository userRepository;

	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	// Save New User
	public User saveUser(User user) {
		return userRepository.saveAndFlush(user);
	}

	// Get All Users
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

}
