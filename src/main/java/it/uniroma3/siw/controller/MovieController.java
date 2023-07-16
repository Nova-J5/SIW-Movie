package it.uniroma3.siw.controller;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import it.uniroma3.siw.controller.validator.MovieValidator;
import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.model.Image;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.ArtistService;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.ImageService;
import it.uniroma3.siw.service.MovieService;
import it.uniroma3.siw.service.ReviewService;
import jakarta.validation.Valid;

@Controller
public class MovieController {
	
	@Autowired 
	private MovieService movieService;
	
	@Autowired
	private ArtistService artistService;
	
	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private ImageService imageService;
	
	@Autowired
	private MovieValidator movieValidator;
	
	@Autowired
	private GlobalController globalController;
	
	@Autowired
	private CredentialsService credentialsService;
	
	
	/* OPERAZIONI PER TUTTI I TIPI DI UTENTE */
	
	@GetMapping("/movie/{id}")
	public String getMovie(@PathVariable("id") Long id, Model model) {
		
		Movie movie = this.movieService.getMovie(id);
	    model.addAttribute("movie", movie);
		Review newReview = new Review();
		model.addAttribute("newReview", newReview);
		List<Review> reviews = this.reviewService.getReviewsByMovie(movie);
		if(!reviews.isEmpty())
		{
			Review reviewActive = reviews.get(0);
			model.addAttribute("reviewActive", reviewActive);
			reviews.remove(reviewActive);
		}
	
		UserDetails userDetails = this.globalController.getUser();
		if( userDetails == null) {
			model.addAttribute("userReview", null);
		}
		else {
			User user = this.credentialsService.getCredentialsByUsername(userDetails.getUsername()).getUser();
			Review userReview = reviewService.getReviewByMovieAndUser(user, movie);
			  	
			model.addAttribute("userReview", userReview);
			reviews.remove(userReview);
		}
		
		model.addAttribute("reviews", reviews);
	    return "movie.html";
	}

	@GetMapping("/movies")
	public String showMovies(Model model) {
	    model.addAttribute("movies", this.movieService.getAllMovies());
	    return "movies.html";
	}

	@PostMapping("/searchMovies")
	public String searchMovies(Model model, @RequestParam String str) {
		
		List<Movie> movies = this.movieService.getMoviesByTitle(str);
		try {
			Integer year = Integer.parseInt(str);
			model.addAttribute("movies", this.movieService.getMoviesByYear(year));	
		} catch(Exception e) {
			model.addAttribute("movies", movies);
		}
		return "foundMovies.html";
	}
	
	
	/* OPERAZIONI PER GLI ADMIN */
	
	@GetMapping("/admin/manageMovies")
	public String manageMovies(Model model) {
		model.addAttribute("movies", this.movieService.getAllMovies());
		return "admin/manageMovies.html";
	}
	
	@GetMapping("/admin/formNewMovie")
	public String formNewMovie(Model model) {
	    model.addAttribute("movie", new Movie());
	    model.addAttribute("artists", this.artistService.getAllArtists());
	    return "admin/formNewMovie.html";
	}
	
	@PostMapping("/admin/newMovie")
	public String newMovie(@Valid @ModelAttribute("movie") Movie movie, BindingResult bindingResult, Model model,
	        @RequestParam Long directorId, @RequestParam("file") MultipartFile file) throws IOException {
	    
		this.movieValidator.validate(movie, bindingResult);
		
		if (!bindingResult.hasErrors()) {
				
			if (!file.isEmpty()) {
				Image img = new Image(file.getBytes());
				this.imageService.save(img);
				movie.setImage(img);
			}	
			Artist director = this.artistService.getArtist(directorId);
			this.movieService.inizializeMovie(movie, director);
			this.artistService.addArtist(director);
			this.movieService.addMovie(movie);
			
			
			model.addAttribute("movie", movie);
			model.addAttribute("actorsToAdd", this.artistService.findActorsNotInMovie(movie.getId()));
			return "admin/actorsToAdd.html";
		} else {
			return "admin/formNewMovie.html"; 
		}
	}
	
	@GetMapping(value="/admin/formUpdateMovie/{id}")
	public String formUpdateMovie(@PathVariable("id") Long id, Model model) {
		model.addAttribute("movie", movieService.getMovie(id));
		return "admin/formUpdateMovie.html";
	}
	
	@GetMapping(value="/admin/setDirectorToMovie/{directorId}/{movieId}")
	public String setDirectorToMovie(@PathVariable("directorId") Long directorId, @PathVariable("movieId") Long movieId, Model model) {
		
		Artist director = this.artistService.getArtist(directorId);
		Movie movie = this.movieService.getMovie(movieId);
		movie.setDirector(director);
		this.movieService.addMovie(movie);
		
		model.addAttribute("movie", movie);
		return "admin/formUpdateMovie.html";
	}
	

	
	@GetMapping("/admin/updateActors/{id}")
	public String updateActors(@PathVariable("id") Long id, Model model) {

		List<Artist> actorsToAdd = this.actorsToAdd(id);
		model.addAttribute("actorsToAdd", actorsToAdd);
		model.addAttribute("movie", this.movieService.getMovie(id));

		return "admin/actorsToAdd.html";
	}
	
	@PostMapping ("/admin/updateMovie")
	  public String updateMovie (@Valid @ModelAttribute("movie") Movie movie, 
			  BindingResult bindingResult, Model model, 
			  @RequestParam("title") String title, @RequestParam("year") Integer year,
			  @RequestParam("description") String description,
			  @RequestParam("imageFile") MultipartFile multipartFile) throws IOException {

		  if (movie == null)
			  return "index.html";
		  this.movieService.updateMovie(movie, title, year, null);
			
		  this.movieService.addMovie(movie);
		  
		  return "redirect:/movie/" + movie.getId();

	  }

	@GetMapping(value="/admin/addActorToMovie/{actorId}/{movieId}")
	public String addActorToMovie(@PathVariable("actorId") Long actorId, @PathVariable("movieId") Long movieId, Model model) {
		Movie movie = this.movieService.getMovie(movieId);
		Artist actor = this.artistService.getArtist(actorId);
		List<Artist> actors = movie.getActors();
		actors.add(actor);
		this.movieService.addMovie(movie);
		
		List<Artist> actorsToAdd = actorsToAdd(movieId);
		
		model.addAttribute("movie", movie);
		model.addAttribute("actorsToAdd", actorsToAdd);

		return "admin/actorsToAdd.html";
	}
	
	@GetMapping(value="/admin/removeActorFromMovie/{actorId}/{movieId}")
	public String removeActorFromMovie(@PathVariable("actorId") Long actorId, @PathVariable("movieId") Long movieId, Model model) {
		Movie movie = this.movieService.getMovie(movieId);
		Artist actor = this.artistService.getArtist(actorId);
		List<Artist> actors = movie.getActors();
		actors.remove(actor);
		this.movieService.addMovie(movie);

		List<Artist> actorsToAdd = actorsToAdd(movieId);
		
		model.addAttribute("movie", movie);
		model.addAttribute("actorsToAdd", actorsToAdd);

		return "admin/actorsToAdd.html";
	}
	
	@GetMapping("/admin/deleteMovie/{id}")
	public String deleteMovie(@PathVariable("id") Long id, Model model) {
		this.movieService.deleteMovie(id);
		model.addAttribute("movies", this.movieService.getAllMovies());
		return "movies.html";
	}
	


	private List<Artist> actorsToAdd(Long movieId) {
		List<Artist> actorsToAdd = new ArrayList<>();

		for (Artist a : artistService.findActorsNotInMovie(movieId)) {
			actorsToAdd.add(a);
		}
		return actorsToAdd;
	}
}
