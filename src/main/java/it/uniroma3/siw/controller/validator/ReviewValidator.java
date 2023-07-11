package it.uniroma3.siw.controller.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Review;

@Component
public class ReviewValidator implements Validator {

	@Override
	public void validate(Object o, Errors errors) {
		Review review = (Review) o;
		if (review.getMovie() != null) {
			for (Review r : review.getMovie().getReviews()) {
				if (review.getUser().equals(r.getUser()))
					errors.reject("review.duplicate");
			}
		}
		
	}
	
	@Override
	public boolean supports(Class<?> aClass) {
		return Review.class.equals(aClass);
	}
}
