package it.uniroma3.siw.model;

import java.util.Objects;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

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
        
        @Column(nullable = true, length = 64)
        private String image;
        
        @ManyToOne
        private Artist director;
        
        @ManyToMany
        private Set<Artist> actors;
        
        @OneToMany(mappedBy="movie")
        private Set<Review> reviews;
    
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
        
        public String getImage() {
            return image;
        }
    
        public void setImage(String image) {
            this.image = image;
        }
        
        @Transient
        public String getPhotoImagePath() {
            if (image == null || id == null) return null;
             
            return "/user-photos/" + id + "/" + image;
        }
    
        public Artist getDirector() {
            return director;
        }
    
        public void setDirector(Artist director) {
            this.director = director;
        }
    
        public Set<Artist> getActors() {
            return actors;
        }
    
        public void setActors(Set<Artist> actors) {
            this.actors = actors;
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
