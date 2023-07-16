package it.uniroma3.siw.service;

import java.util.ArrayList;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.repository.MovieRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
public class MovieService {
	
	@Autowired
	private MovieRepository movieRepository;
	
	@Autowired
	private ArtistService artistService;
	
	
	@Transactional
	public Movie addMovie(Movie movie) {
		return this.movieRepository.save(movie);
	}
	
	@Transactional
    public Movie getMovie(Long id) {
        Optional<Movie> result = this.movieRepository.findById(id);
        return result.orElse(null);
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
    public List<Movie> getMoviesByYear(Integer year) {
		return this.movieRepository.findByYear(year);
    }
	
	@Transactional
	public List<Movie> getMoviesByTitle(String title) {
		return this.movieRepository.findByTitle(title);
	}
	
	@Transactional
	public boolean alreadyExists(Movie movie) {
		return this.movieRepository.existsByTitleAndYear(movie.getTitle(), movie.getYear());
	}
	
	@Transactional
	public void updateMovie (Movie movie, String title, Integer year, Artist director) {
		movie.setTitle(title);
		movie.setYear(year);
		movie.setDirector(director);
	}

	@Transactional
	public void deleteMovie(Long id) {
		Movie movie = this.getMovie(id);
		Artist director = this.artistService.getDirectorByMovie(movie);
		if(director!=null) {
			director.getDirectedMovies().remove(movie);
			this.artistService.addArtist(director);
		}
		List<Artist> actors = this.artistService.getActorsByMovie(movie);
		for(Artist a : actors) {
			a.getStarredMovies().remove(movie);
			this.artistService.addArtist(a);
		}
		this.movieRepository.deleteById(id);
	}
	
	@Transactional
	public void inizializeMovie(@Valid Movie movie, Artist director) {
		movie.setDirector(director);
		List<Artist> actors = new ArrayList<>();
		movie.setActors(actors);
	}
	
	public List<Movie> getMoviesByDirector(Artist director) {
		return this.movieRepository.findByDirector(director);
	}
	
	@Transactional
	public Movie setDirectorToMovie(Long movieId, Long directorId) {
		Movie movie = this.getMovie(movieId);
		Artist director = this.artistService.getArtist(directorId);
		if (movie!=null && director!=null) {
			movie.setDirector(director);
			this.addMovie(movie);
		}
		return movie;
	}

	public List<Movie> getMoviesByActor(Artist artist) {
		return this.movieRepository.findByActors(artist);
	}
	
}
