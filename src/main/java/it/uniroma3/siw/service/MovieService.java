package it.uniroma3.siw.service;

import java.util.ArrayList;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.repository.MovieRepository;
import jakarta.transaction.Transactional;

@Service
public class MovieService {
	@Autowired
	private MovieRepository movieRepository;
	
	
	@Transactional
	public Movie addMovie(Movie movie) {
		return this.movieRepository.save(movie);
	}

	
	@Transactional
    public List<Movie> getAllMovies() {
        List<Movie> result = new ArrayList<>();
        Iterable<Movie> iterable = this.movieRepository.findAll();
        for(Movie movie : iterable)
            result.add(movie);
        return result;
    }

	
	@Transactional
    public Movie getMovie(Long id) {
        Optional<Movie> result = this.movieRepository.findById(id);
        return result.orElse(null);
    }
	
	
	@Transactional
    public List<Movie> findMoviesByYear(Integer year) {
		return this.movieRepository.findByYear(year);
    }
	
	
	@Transactional
	public List<Movie> findMoviesByTitle(String title) {
		return this.movieRepository.findByTitle(title);
	}


	@Transactional
	public boolean alreadyExists(Movie movie) {
		return this.movieRepository.existsByTitleAndYear(movie.getTitle(), movie.getYear());
	}
	

	@Transactional
	public void updateMovie(Movie updatedMovie) {
		this.movieRepository.save(updatedMovie);
	}

	
	@Transactional
	public void deleteMovie(Long id) {
		this.movieRepository.deleteById(id);		
	}
	
	
}