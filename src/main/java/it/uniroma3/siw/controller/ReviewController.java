package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.repository.MovieRepository;
import it.uniroma3.siw.repository.ReviewRepository;

@Controller
@RequestMapping("/recensioni")
public class ReviewController {
	
	@Autowired 
    private ReviewRepository reviewRepository;
	
	@Autowired 
	private MovieRepository movieRepository;

    // Metodo per visualizzare tutte le recensioni
    @GetMapping("/")
    public String getAllReviews(Model model) {
        model.addAttribute("reviews", reviewRepository.findAll());
        return "reviews";
    }

    // Metodo per aggiungere una nuova recensione a un film
    @PostMapping("/aggiungi")
    public String addReview(@RequestParam Long movieId, @RequestParam String title,
                            @RequestParam int score, @RequestParam String text) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new IllegalArgumentException("Film non trovato: " + movieId));

        Review review = new Review();
        review.setTitle(title);
        review.setScore(score);
        review.setText(text);
        review.setMovie(movie);

        reviewRepository.save(review);

        return "redirect:/film/" + movieId;
    }

    // Metodo per eliminare una recensione
    @PostMapping("/admin/deleteReview")
    public String deleteReview(@RequestParam Long Id) {
        reviewRepository.deleteById(Id);
        return "redirect:/reviews/";
    }
    
}
