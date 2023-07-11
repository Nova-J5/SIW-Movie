package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.MovieService;
import it.uniroma3.siw.service.ReviewService;
import it.uniroma3.siw.service.UserService;
import jakarta.validation.Valid;

@Controller
public class ReviewController {
	
	@Autowired
	private ReviewService reviewService;
	
	@Autowired 
	private MovieService movieService;
	
	@Autowired
	private GlobalController globalController;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CredentialsService credentialsService;
	
	
	@GetMapping("/reviews/{id}")
	private String getReviewedMovies(@PathVariable("id") Long id, Model model) {
		User user =  this.userService.findUserById(id);
		model.addAttribute("reviews", this.reviewService.getReviewsByUser(user));
		return "reviewedMovies.html";
	}
	
	 @PostMapping("/newReview/{movieId}/{userId}")
	 public String newReview(@Valid @ModelAttribute("review") Review review, 
			 @PathVariable("movieId") Long movieId,
			 @PathVariable("userId") Long userId,
			 BindingResult bindingResult, Model model) {
		 
		Movie movie = this.movieService.getMovie(movieId);
		User user = this.userService.findUserById(userId);
		
		this.reviewService.setMovieAndUser(review, user, movie);
		
		user.getReviews().add(review);
		movie.getReviews().add(review);
			
		this.movieService.addMovie(movie);
		this.userService.addUser(user);
		this.reviewService.addReview(review);
		return "redirect:/movie/" + movieId;
	 }
    
	 @PostMapping("/updateReview/{id}")
	 public String updateReview (@PathVariable("id") Long id, Model model,
			 @RequestParam String title, @RequestParam Integer score, @RequestParam String body) {
		
		 Review review = this.reviewService.getReview(id);
		 Movie movie = review.getMovie();
		 
		 this.reviewService.updateReview(review, title, score, body);
		 this.reviewService.addReview(review);
		 this.movieService.addMovie(movie);
		 model.addAttribute("movie", movie);
		 model.addAttribute("newReview", new Review());
		 model.addAttribute("reviews", movie.getReviews());
		 return "redirect:/movie/" + movie.getId();
	 }
	 
	 @GetMapping("/deleteReview/{reviewId}/{movieId}")
	 public String deleteReview(Model model, @PathVariable("reviewId") Long reviewId, @PathVariable("movieId") Long movieId) {
		 User user = this.credentialsService.getCredentialsByUsername(globalController.getUser().getUsername()).getUser();
		 Review review = this.reviewService.getReview(reviewId);
		 Movie movie = review.getMovie();
		 
		 this.reviewService.deleteReview(reviewId, movieId, user);
		 this.movieService.addMovie(movie);
		 model.addAttribute("movie", movie);
		 model.addAttribute("newReview", new Review());
		 model.addAttribute("reviews", movie.getReviews());
		 return "redirect:/movie/" + movieId;
	 }
    
}
