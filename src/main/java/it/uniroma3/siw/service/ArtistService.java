package it.uniroma3.siw.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.repository.ArtistRepository;
import jakarta.transaction.Transactional;

@Service
public class ArtistService {
	
	@Autowired
	private ArtistRepository artistRepository;
	

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
	public void updateArtist(Artist updatedArtist) {
		this.artistRepository.save(updatedArtist);
	}
	
	
	@Transactional
	public void deleteArtist(Long id) {
		this.artistRepository.deleteById(id);
	}
	
	
	@Transactional
	public Iterable<Artist> findActorsNotInMovie(Long id) {
		return this.artistRepository.findActorsNotInMovie(id);
	}

	
	
}