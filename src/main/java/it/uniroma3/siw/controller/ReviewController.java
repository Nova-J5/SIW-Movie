package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.service.MovieService;
import it.uniroma3.siw.service.ReviewService;

@Controller
@RequestMapping("/recensioni")
public class ReviewController {
	
	@Autowired
	private ReviewService reviewService;
	
	@Autowired 
	private MovieService movieService;
	
	
    // Metodo per visualizzare tutte le recensioni di un film
    @GetMapping("/review")
    public String getAllReviews(Long id, Model model) {
        model.addAttribute("reviews", reviewService.getReviewsByMovie(id));
        return "reviews.html";
    }
    
    
    @GetMapping(value="/admin/formNewMovie")
	public String formNewMovie(Model model) {
		model.addAttribute("movie", new Movie());
		return "admin/formNewMovie.html";
	}

    
    @PostMapping("/review")
    public String newReview(@ModelAttribute("review") Review review, @RequestParam Long movieId, 
    		 Model model, BindingResult bindingResult) {
    	
    	//I dati sono validi?
    	if(!bindingResult.hasErrors()) {
    		review.setMovie(movieService.getMovie(movieId));
    		this.reviewService.addReview(review);
    		model.addAttribute("review", review);
    		return "review.html";
    	}
    	
    	return "formNewReview.html";
    }
    
    
    @GetMapping("/admin/formDeleteReview")
	public String formDeleteMovie(@RequestParam Long id, Model model) {
		model.addAttribute("reviews", this.reviewService.getReviewsByMovie(id));
		return "admin/deleteReview.html";
	}

    
    @PostMapping("/admin/deleteReview")
    public String deleteReview(@RequestParam Long id) {
        this.reviewService.deleteReview(id);
        return "review";
    }
    
    
}
