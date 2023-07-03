package it.uniroma3.siw.util;

import java.io.IOException;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.web.multipart.MultipartFile;

/*
 *  Gestione delle immagini relative ai film all'interno del database
 */
public class FileManagerUtils {
	
	 /** Salva il file nel sistema.
	  * @param uploadDir Path richiesta per il salvataggio
	  * @param fileName Nome del file da salvare
	  * @param multipartFile File upload
	  * @throws IOException
	  */
	 public static void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {
		 Path uploadPath = Paths.get(uploadDir);

		 if (!Files.exists(uploadPath)) {
			 Files.createDirectories(uploadPath);
		 }

		 try (InputStream inputStream = multipartFile.getInputStream()) {
			 Path filePath = uploadPath.resolve(fileName);
			 Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
		 } 
		 catch (IOException ioe) {        
			 throw new IOException("Could not save image file: " + fileName, ioe);
		 }      
	 }
	 
	 /** Cancella il file nel sistema
	  * @param pathOfFile Percorso del file che si vuole cancellare.
	  * @param fileName Nome del file da cancellare.
	  * @throws IOException
	  */
	 public static void deleteFile(String pathOfFile, String fileName) throws IOException {
		 Path path = Paths.get(pathOfFile);

		 Files.deleteIfExists(path.resolve(fileName));   
	 }
	 
}