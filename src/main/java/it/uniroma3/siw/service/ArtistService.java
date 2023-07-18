package it.uniroma3.siw.service;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.repository.ArtistRepository;
import it.uniroma3.siw.repository.MovieRepository;
import jakarta.transaction.Transactional;

@Service
public class ArtistService {
	
	@Autowired
	private ArtistRepository artistRepository;
	
	@Autowired
	private MovieRepository movieRepository;
	

	@Transactional
	public Artist addArtist(Artist artist) {
		return this.artistRepository.save(artist);
	}
	
	
	@Transactional
    public List<Artist> getAllArtists() {
        List<Artist> result = new ArrayList<>();
        Iterable<Artist> iterable = this.artistRepository.findAll();
        for(Artist artist : iterable)
            result.add(artist);
        return result;
    }
	
	
	@Transactional
	public List<Artist>findArtistsBySurname(String surname) {
		return this.artistRepository.findBySurname(surname);
	}
	

	@Transactional
	public Artist getArtist(Long id) {
		Optional<Artist> result = this.artistRepository.findById(id);
		return result.orElse(null);
		
	}
	

	@Transactional
	public boolean alreadyExists(Artist artist) {
		return this.artistRepository.existsByNameAndSurname(artist.getName(), artist.getSurname());
	}

	
	@Transactional
	public void updateArtist(Artist artist, String name, String surname, LocalDate dateOfBirth) {
		artist.setName(name);
		artist.setSurname(surname);
		artist.setDateOfBirth(dateOfBirth);
	}
	
	
	@Transactional
	public void deleteArtist(Long id) {
		Artist artist = this.getArtist(id);
		List<Movie> starredMovies = this.movieRepository.findByActors(artist);
		for(Movie starredMovie : starredMovies) {
			starredMovie.getActors().remove(artist);
			this.movieRepository.save(starredMovie);
		}
		List<Movie> directedMovies = this.movieRepository.findByDirector(artist);
		for(Movie directedMovie: directedMovies) {
			directedMovie.setDirector(null);
			this.movieRepository.save(directedMovie);
		}
		this.artistRepository.deleteById(id);
	}
	
	
	@Transactional
	public Iterable<Artist> findActorsNotInMovie(Long id) {
		return this.artistRepository.findActorsNotInMovie(id);
	}

	public void inizializeArtist(Artist artist) {
		artist.setDirectedMovies(new ArrayList<>());
		artist.setStarredMovies(new ArrayList<>());
	}

	public Artist getDirectorByMovie(Movie directedMovie) {
		return this.artistRepository.findByDirectedMovies(directedMovie);
	}

	public List<Artist> getActorsByMovie(Movie movie) {
		return this.artistRepository.findByStarredMovies(movie);
	}
	
}