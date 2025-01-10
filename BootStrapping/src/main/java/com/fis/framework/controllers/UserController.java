package com.fis.framework.controllers;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fis.framework.entities.User;
import com.fis.framework.services.UserService;

import jakarta.validation.Valid;

@Controller
public class UserController {
	private final UserService userService;
	private User registeredUser;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/register")
	public String showForm(Model model) {
		User user = new User();
		model.addAttribute("user", user);

		List<String> listProfession = Arrays.asList("Developer", "Tester", "Architect");
		model.addAttribute("listProfession", listProfession);

		return "register_form";
	}

	@PostMapping("/register")
	public String submitForm(@Valid @ModelAttribute("user") User user, BindingResult bindingResult) {
		registeredUser = user; // Store the user data temporarily
		System.out.println(user);

		if (bindingResult.hasErrors()) {
			return "register_form";
		} else {
			return "register_success";
		}
	}

	@PostMapping("/confirmation")
	public String saveRegistrationData(Model model) {
		if (registeredUser != null) {

			// encode password
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			String encodedPassword = passwordEncoder.encode(registeredUser.getPassword());
			registeredUser.setPassword(encodedPassword);

			// Save the user data before displaying the confirmation
			userService.saveUser(registeredUser);

			// You can also clear the registeredUser data if needed
			model.addAttribute("user", registeredUser);

		} else {
			return "redirect:/register"; // Redirect to registration page if no data is stored
		}
		return "index";
	}

	@GetMapping("/users")
	public String listUsers(Model model) {
		List<User> listUsers = userService.getAllUsers();
		model.addAttribute("listUsers", listUsers);

		return "users";
	}

}
