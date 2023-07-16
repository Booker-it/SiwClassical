package it.uniroma3.siwClassical.controller.utility;

import org.springframework.web.multipart.MultipartFile;

public class FileUploadUtil {
	
	private MultipartFile image;
	


	public MultipartFile getImage() {
		return image;
	}

	public void setImage(MultipartFile image) {
		this.image = image;
	}

	
}
