package it.uniroma3.siw.controller;

import java.io.IOException;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import it.uniroma3.siw.controller.validator.ArtistValidator;
import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.model.Image;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.service.ArtistService;
import it.uniroma3.siw.service.ImageService;
import jakarta.validation.Valid;

@Controller
public class ArtistController {
	
	@Autowired
	private ArtistService artistService;
	
	@Autowired
	private ArtistValidator artistValidator;
	
	@Autowired
	private ImageService imageService;
	
	
	@GetMapping("/artist/{id}")
	  public String getArtist(@PathVariable("id") Long id, Model model) {
	    Artist artist = this.artistService.getArtist(id);
	    if (artist != null) {
	    	model.addAttribute("artist", artist);
	    	model.addAttribute("directedMovies", artist.getDirectedMovies());
	    	model.addAttribute("starredMovies", artist.getStarredMovies());
	    	return "artist.html";
	    }
	    else {
			return "artistError.html";
	    }
	}
	
	@GetMapping("/artists")
	public String getArtists(Model model) {
		model.addAttribute("artists", this.artistService.getAllArtists());
		return "artists.html";
	}
	
	@GetMapping("/admin/formNewArtist")
	public String formNewArtist(Model model) {
		model.addAttribute("artist", new Artist());
		return "admin/formNewArtist.html";
	}
	
	@PostMapping("/admin/newArtist")
	public String newArtist(@Valid @ModelAttribute("artist") Artist artist, BindingResult bindingResult, Model model,
	        @RequestParam("file") MultipartFile file) throws IOException {
	    
		this.artistValidator.validate(artist, bindingResult);
		
		if (!bindingResult.hasErrors()) {
			if (!file.isEmpty()) {
				Image img = new Image(file.getBytes());
				this.imageService.save(img);
				artist.setImage(img);
			}	
			this.artistService.addArtist(artist);			
	    	model.addAttribute("directedMovies", artist.getDirectedMovies());
	    	model.addAttribute("starredMovies", artist.getStarredMovies());
			model.addAttribute("artist", artist);
			return "artist.html";
		} else {
			return "admin/formNewArtist.html"; 
		}
	}
	
	
	@GetMapping("/admin/formUpdateArtist/{id}")
	public String formUpdateMovie(@PathVariable("id") Long id, Model model) {
		model.addAttribute("artist", artistService.getArtist(id));
		return "admin/formUpdateArtist.html";
	}
	
	@PostMapping ("/admin/updateArtist")
	  public String updateArtist (@Valid @ModelAttribute("artist") Artist artist, 
			  BindingResult bindingResult, Model model, 
			  @RequestParam("name") String name, @RequestParam("surname") String surname,
			  @RequestParam("dateOfDeath") LocalDate dateOfBirth, @RequestParam("dateOfDeath") LocalDate dateOfDeath,
			  @RequestParam("imageFile") MultipartFile multipartFile) throws IOException {

		  if (artist == null)
			  return "index.html";
		  this.artistService.updateArtist(artist, name, surname, dateOfBirth, dateOfDeath);
			
		  this.artistService.addArtist(artist);
		  
		  return "redirect:/artist/" + artist.getId();

	  }
	
	  @GetMapping("/admin/deleteArtist/{id}")
	  public String deleteArtist(Model model, @PathVariable("id") Long id) {
		  this.artistService.deleteArtist(id);
		  model.addAttribute("artists", this.artistService.getAllArtists());
		  return "artists.html";
	  }
		
}
