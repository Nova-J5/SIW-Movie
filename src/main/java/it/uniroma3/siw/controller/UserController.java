package it.uniroma3.siw.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.ReviewService;
import it.uniroma3.siw.service.UserService;
import it.uniroma3.siw.util.FileUploadUtil;

@Controller
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private ReviewService reviewService;
        
    
    @PostMapping("/users/save")
    public RedirectView saveUser(User user, RedirectAttributes ra,
            @RequestParam("image") MultipartFile image) throws IOException {
         
        String fileName = StringUtils.cleanPath(image.getOriginalFilename());
        user.setPhoto(fileName);
         
        User savedUser = userService.addUser(user);
 
        String uploadDir = "user-photos" + savedUser.getId();
 
        FileUploadUtil.saveFile(uploadDir, fileName, image);
         
        return new RedirectView("/users", true);
    }
    
    
    @GetMapping("/profile/{id}")
	public String getMovie(@PathVariable("id") Long id, Model model) {
		model.addAttribute("user", this.userService.getUser(id));
		model.addAttribute("reviews", this.reviewService.getReviewsByUser(id));
		return "profile.html";
	}
    

}