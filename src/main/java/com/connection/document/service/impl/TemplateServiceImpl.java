package com.connection.document.service.impl;

import java.io.IOException;
import java.util.List;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.connection.document.exception.NonUniqueResultException;
import com.connection.document.exception.NoSuchElementException;
import com.connection.document.model.TempVariables;
import com.connection.document.model.TemplateModel;
import com.connection.document.model.WatermarkModel;
import com.connection.document.model.common.ResponseModel;
import com.connection.document.repository.TemplateRepository;
import com.connection.document.service.ITemplateService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TemplateServiceImpl implements ITemplateService {

	@Autowired
	TemplateRepository templaterepo;

	/**
	 * This method is used to save template to the database
	 * 
	 * @throws IOException
	 * @throws NonUniqueResultException 
	 */
	@Override
	public ResponseModel saveTemplate(MultipartFile file, String type, List<TempVariables> templateVariables,
			MultipartFile imageW, String textW) throws IOException, NonUniqueResultException {
		log.info("Saving template");
		
		TemplateModel templatemodel = new TemplateModel();
		
		String encodedImageW = Base64.encodeBase64String(imageW.getBytes()).trim();
		WatermarkModel watermarkModel = WatermarkModel.builder().imageWatermark(encodedImageW).textWatermark(textW)
				.build();
		templatemodel.setTemplate(Base64.encodeBase64String(file.getBytes()).trim());
		templatemodel.setWatermark(watermarkModel);
		templatemodel.setTemplateType(type);
		templatemodel.setTemplateVariables(templateVariables);
		templaterepo.save(templatemodel);
		ResponseModel responseModel = ResponseModel.builder().statusCode(HttpStatus.OK.value())
				.message("File saved successfully").build();
		return responseModel;
	}
	
	/**
	 * This method is used to delete the template from database by using id
	 */

	@Override
	public ResponseModel deleteTemplateById(Integer id) {
	templaterepo.deleteById(id);
	ResponseModel responseModel = ResponseModel.builder().statusCode(HttpStatus.OK.value())
			.message("Template deleted successfully!").build();
	return responseModel;
	}
	
	/**
	 * This method is used to replace the existing template with the new one from the database
	 */

	@Override
	public ResponseModel PutNewbyReplacingOld(Integer id,MultipartFile file,String type,List <TempVariables> templateVariables,MultipartFile imageW , String textW) throws IOException {
		TemplateModel updateTempModel = templaterepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Template not exist with id: " + id));

		String encodedImageW1 = Base64.encodeBase64String(imageW.getBytes()).trim();
		WatermarkModel watermarkModel = WatermarkModel.builder().imageWatermark(encodedImageW1).textWatermark(textW)
				.build();
		updateTempModel.setTemplate(Base64.encodeBase64String(file.getBytes()).trim());
		updateTempModel.setWatermark(watermarkModel);
		updateTempModel.setTemplateType(type);
		updateTempModel.setTemplateVariables(templateVariables);
		templaterepo.save(updateTempModel);
		ResponseModel responseModel = ResponseModel.builder().statusCode(HttpStatus.OK.value())
				.message("Template updated successfully!").build();
		return responseModel;
	}

}
