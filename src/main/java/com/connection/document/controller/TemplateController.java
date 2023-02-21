package com.connection.document.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.connection.document.exception.NonUniqueResultException;
import com.connection.document.model.TempVariables;
import com.connection.document.model.TemplateModel;
import com.connection.document.model.common.ResponseModel;
import com.connection.document.repository.TemplateRepository;
import com.connection.document.service.ITemplateService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/v1/document")
public class TemplateController {

	@Autowired
	Gson gson;
	@Autowired
	ITemplateService Iservice;
	
	@Autowired
	TemplateRepository temprepository;
	
	/**
	 * 
	 * @param file A representation of an uploaded file received in a multipart request. 
	 * @param type It stores the type of uploaded template
	 * @param templateVariables it stores the variables that is going to replaced dynamically
	 * @param watermark Contains the Bse64 of watermark image and saved in database 
	 * @throws IOException 
	 * @throws NonUniqueResultException 
	 */
	@PostMapping("/saveTemplate")
	public ResponseEntity<ResponseModel> SaveTemplateFile(@RequestParam("template")MultipartFile file,@RequestParam("type")String type,@RequestParam("templateVariables")String templateVariables,@RequestParam("watermark")MultipartFile watermark,String textWatermark) throws IOException, NonUniqueResultException{
		log.info("Inside SaveTemplate Controller");
		@SuppressWarnings("unchecked")
		List<TempVariables> tempVar=gson.fromJson(templateVariables, List.class);
	
		return new ResponseEntity<>(Iservice.saveTemplate(file,type,tempVar,watermark,textWatermark),HttpStatus.OK);
	}
	
	
	@DeleteMapping("/deleteTemplate/{Id}")
	public ResponseEntity<ResponseModel> DeleteTemplateFile(@PathVariable("Id") Integer id) throws Exception{
		log.info("Inside Delete Template Controller");
		@SuppressWarnings("unchecked")
		
		Optional<TemplateModel> tempData = temprepository.findById(id);

		if (!tempData.isPresent()) {

			throw new Exception("Data doesn't exist! Please Enter valid id");
		}
		return new ResponseEntity<>(Iservice.deleteTemplateById(id),HttpStatus.OK);
	}
	
	@PutMapping("/replaceTemplate/{Id}")
	public ResponseEntity<ResponseModel> UpdateTemplateFile(@PathVariable("Id") Integer id,@RequestParam("template")MultipartFile file,@RequestParam("type")String type,@RequestParam("templateVariables")String templateVariables,@RequestParam("watermark")MultipartFile watermark,String textWatermark) throws Exception{
		log.info("Inside Delete Template Controller");
		List<TempVariables> tempVar=gson.fromJson(templateVariables, List.class);
		return new ResponseEntity<>(Iservice.PutNewbyReplacingOld(id, file,type,tempVar,watermark,textWatermark),HttpStatus.OK);
		
	}
}
