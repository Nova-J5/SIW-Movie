package it.uniroma3.siw.util;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.service.CredentialsService;

public class Utils {
	
	private final static String PATH_SAVE_IMAGES = "src/main/resources/static/images/user-images/";
	
	@Autowired
	private CredentialsService credentialsService;
	
	
	public static String getTypeOfUser() {
		String tipoUtente;
		try{
			UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			tipoUtente = "ADMIN";
		}
		catch(ClassCastException e){
			tipoUtente= "DEFAULT";
		}
		return tipoUtente;
	}
			
	/* Salva l'immagine nel sistema */
	public static String saveImage(MultipartFile file) throws IOException {
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		String uploadDir = PATH_SAVE_IMAGES;
		
        FileManagerUtils.saveFile(uploadDir, fileName, file);
        
		return fileName;
	}
	
	/** Cancella un file dal sistema */
	public static void deleteImage(String fileName) throws IOException {
		FileManagerUtils.deleteFile(PATH_SAVE_IMAGES, fileName);
	}
	
	
	/*
	 * Ritorna Le credenziali dell'utente attualmente loggato
	 */
	public Credentials getCredentials() {
		return credentialsService.getCredentials(this.getUserDetails().getUsername());
	}
	
	/*
	 * Ritorna i UserDetails dell'utente attualmente loggato
	 */
	private UserDetails getUserDetails() {
		return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	
}
