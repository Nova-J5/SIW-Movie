package it.uniroma3.siw.service;

import java.util.ArrayList;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.ReviewRepository;
import jakarta.transaction.Transactional;

@Service
public class ReviewService {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ReviewRepository reviewRepository;
	
	@Autowired
	private MovieService movieService;
	
	
	@Transactional
	public Review addReview(Review review) {
		return reviewRepository.save(review);
	}
	
	@Transactional
	public void setMovieAndUser(Review review, User user, Movie movie) {
		review.setMovie(movie);
		review.setUser(user);
	}
	
	@Transactional
    public List<Review> getAllReviews() {
        List<Review> result = new ArrayList<>();
        Iterable<Review> iterable = this.reviewRepository.findAll();
        for(Review review : iterable)
            result.add(review);
        return result;
    }
	
	@Transactional
    public Review getReview(Long id) {
        Optional<Review> result = this.reviewRepository.findById(id);
        return result.orElse(null);
    }

	@Transactional
	public List<Review> getReviewsByUser(User user) {
		return this.reviewRepository.findByUser(user);
	}

	@Transactional
	public List<Review> getReviewsByMovie(Movie movie) {
		return this.reviewRepository.findByMovie(movie);
	}
	
	@Transactional
	public Review getReviewByMovieAndUser(User user, Movie movie) {
		for (Review review : movie.getReviews()) {
			 if (review.getUser().equals(user)) {
				return review;
			 }
		}
		return null;	
	}
	
	@Transactional
	public void updateReview (Review review, String title, int score, String body) {
		review.setTitle(title);
		review.setScore(score);
		review.setBody(body);
	}
	
	@Transactional
	public void deleteReview(Long reviewId, Long movieId, User user) {
		Movie movie = this.movieService.getMovie(movieId);
		Review review = this.getReview(reviewId);
		movie.getReviews().remove(review);
		this.movieService.addMovie(movie);
		user.getReviews().remove(review);
		this.userService.addUser(user);
		this.reviewRepository.deleteById(reviewId);
	}
	
}
