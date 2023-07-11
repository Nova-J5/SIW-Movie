package it.uniroma3.siw.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Movie {
    
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;
    
        @NotBlank
        private String title;
        
        @NotNull
        @Min(1900)
        @Max(2023)
        private Integer year;
        
        @Size(max = 2000)
        private String description;
        
        @ManyToOne
        private Artist director;
        
        @ManyToMany
        private List<Artist> actors;
        
        @OneToMany(mappedBy = "movie")
        private List<Review> reviews;
        
    	@OneToOne
    	private Image image;
    	
    	
    	public Movie() {
    		this.actors = new ArrayList<>();
    		this.reviews = new ArrayList<>();
    	}
    	
    	
    	public String printStars(int score) {
    		String s = "";
    		for(int i=0; i<score; i++) {
    			if (i==0)
    				s="&#9733";
    			else
    				s = s + "&#9733";
    		}
    		for(int i=score; i<5; i++)
    			s = s + "&#9734" ;
    		return s;
    	}

		public Long getId() {
            return id;
        }
    
        public void setId(Long id) {
            this.id = id;
        }
    
        public String getTitle() {
            return title;
        }
        public void setTitle(String title) {
            this.title = title;
        }
    
        public Integer getYear() {
            return year;
        }
    
        public void setYear(Integer year) {
            this.year = year;
        }
        
		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}
    
        public Image getImage() {
			return image;
		}

		public void setImage(Image image) {
			this.image = image;
		}

		public Artist getDirector() {
            return director;
        }
    
        public void setDirector(Artist director) {
            this.director = director;
        }
    
        public List<Artist> getActors() {
            return actors;
        }
    
        public void setActors(List<Artist> actors) {
            this.actors = actors;
        }
        
        public List<Review> getReviews() {
			return reviews;
		}

		public void setReviews(List<Review> reviews) {
			this.reviews = reviews;
		}
    
        @Override
        public int hashCode() {
            return Objects.hash(title, year);
        }
    
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Movie other = (Movie) obj;
            return Objects.equals(title, other.title) && year.equals(other.year);
        }
        
}
