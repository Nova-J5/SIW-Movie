package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.model.Review;

public interface ReviewRepository extends CrudRepository<Review,Long>{
	
	@Query(value="SELECT r FROM Review r WHERE user.id=:id")
	List<Review> findReviewsByUser(Long id);

	@Query(value="SELECT r FROM Review r WHERE movie.id=:id")
	List<Review> findReviewsByMovie(Long id);
	
	public boolean existsByTitleAndMovie(String title, Movie movie);	
	
}