package it.uniroma3.siw.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Image;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.ImageService;
import it.uniroma3.siw.service.ReviewService;
import it.uniroma3.siw.service.UserService;

@Controller
public class UserController {

	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private ImageService imageService;
	
	
	@GetMapping("user/{id}")
	private String getUser(@PathVariable("id") Long id, Model model) {
		User user = this.userService.findUserById(id);
		model.addAttribute("user", user);
		List<Review> reviews = this.reviewService.getReviewsByUser(user);
		model.addAttribute("reviews", reviews);
		if (!reviews.isEmpty()) {
		    int maxElements = Math.min(4, reviews.size());
		    for (int i = 0; i < maxElements; i++) {
		        if (reviews.get(i) != null) {
		            String attributeName;
		            
		            if (i == 1) {
		                attributeName = "secondReviewed";
		            } else if (i == 2) {
		                attributeName = "thirdReviewed";
		            } else if (i == 3) {
		                attributeName = "fourthReviewed";
		            } else {
		                attributeName = "firstReviewed";
		            }
		            
		            model.addAttribute(attributeName, reviews.get(i));
		        }
		    }
		}
		
		return "user.html";
	}
	
	@GetMapping("formUpdateUser/{id}")
	private String formUpdateUser(@PathVariable("id") Long id, Model model) {
		User user = this.userService.findUserById(id);
		model.addAttribute("user", user);
		
		return "formUpdateUser.html";
	}
	
	@PostMapping("updateUser/{id}")
	public String updateUser(@PathVariable("id") Long id, Model model,
			@RequestParam("name") String name, @RequestParam("surname") String surname,
			@RequestParam("email") String email, @RequestParam("file") MultipartFile file) throws IOException {
				
		User user = this.userService.findUserById(id);
		this.userService.updateUser(user, name, surname, email);
		if (!file.isEmpty()) {
			Image img = new Image(file.getBytes());
			this.imageService.save(img);
			user.setImage(img);
		}		
		this.userService.addUser(user);
		model.addAttribute("reviews", this.reviewService.getReviewsByUser(user));
		model.addAttribute("user", user);
		return "user.html";
	}
	
}