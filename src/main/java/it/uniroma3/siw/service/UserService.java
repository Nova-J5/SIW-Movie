package it.uniroma3.siw.service;

import java.util.ArrayList;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	
    @Transactional
    public User addUser(User user) {
        return this.userRepository.save(user);
    }
    
	@Transactional
	public User findUserById(Long id) {
		Optional<User> result = userRepository.findById(id);
		return result.orElse(null);
	}
    
    @Transactional
    public List<User> getAllUsers() {
        List<User> result = new ArrayList<>();
        Iterable<User> iterable = this.userRepository.findAll();
        for(User user : iterable)
            result.add(user);
        return result;
    }

	@Transactional
	public void updateUser (User user, String name, String surname, String email) {
		if(name != null) {
			user.setName(name);
		}
		if(surname != null) {
			user.setSurname(surname);
		}
		if(email != null) {
			user.setEmail(email);
		}
	}

}
