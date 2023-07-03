package it.uniroma3.siw.service;

import java.util.ArrayList;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.repository.ReviewRepository;
import jakarta.transaction.Transactional;

@Service

public class ReviewService {
	
	@Autowired
	private ReviewRepository reviewRepository;
	
	@Transactional
	public Review addReview(Review review) {
		return reviewRepository.save(review);
	}
	
	
	@Transactional
    public List<Review> getAllUsers() {
        List<Review> result = new ArrayList<>();
        Iterable<Review> iterable = this.reviewRepository.findAll();
        for(Review review : iterable)
            result.add(review);
        return result;
    }
	
	
	@Transactional
    public Review geReview(Long id) {
        Optional<Review> result = this.reviewRepository.findById(id);
        return result.orElse(null);
    }
	

	@Transactional
	public List<Review> getReviewsByUser(Long id) {
		return this.reviewRepository.findReviewsByUser(id);
	}
	 

	@Transactional
	public List<Review> getReviewsByMovie(Long id) {
		return this.reviewRepository.findReviewsByMovie(id);
	}
	
	
	@Transactional
	public void deleteReview(Long id) {
		reviewRepository.deleteById(id);
	}
	
	
	@Transactional
	public boolean alreadyExists(Review review) {
		return this.reviewRepository.existsByTitleAndMovie(review.getTitle(), review.getMovie());
	}
	
	
}