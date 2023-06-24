package it.uniroma3.siw.model;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
public class Review {
	  
	    @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;
	  
	    @NotNull
	    private String title;
	  
	    @Min(1)
	    @Max(5)
	    private Integer score;
	  
	    @NotNull
	    private String text;
	    
	    @ManyToOne
	    private Movie movie;

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
	
		public Integer getScore() {
			return score;
		}
	
		public void setScore(Integer score) {
			this.score = score;
		}
	
		public String getText() {
			return text;
		}
	
		public void setText(String text) {
			this.text = text;
		}

		public Movie getMovie() {
			return movie;
		}

		public void setMovie(Movie movie) {
			this.movie = movie;
		}

		@Override
		public int hashCode() {
			return Objects.hash(id);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Review other = (Review) obj;
			return Objects.equals(id, other.id);
		}
		
		
}
