package it.uniroma3.siwClassical.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siwClassical.model.Image;
import it.uniroma3.siwClassical.repository.ImageRepository;
import jakarta.transaction.Transactional;

@Service
public class ImageService {
	@Autowired
	private ImageRepository imageRepository;
	
	@Transactional
	public Image store (MultipartFile file) throws IOException{
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		Image image = new Image(fileName,file.getBytes());
		
		return imageRepository.save(image);
	}

	public Image getFile(Long id) {
		return imageRepository.findById(id).orElse(null);
	}
	
	public Image findImageById(Long id) {
		return this.imageRepository.findById(id).orElse(null);
	}

}
