package com.connection.document.controller;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.connection.document.model.common.RequestModel;
import com.connection.document.service.IPdfGeneratorService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/v1/document")
public class PdfController {

	@Autowired
	IPdfGeneratorService pdfService;

	/**
	 * 
	 * @param requestModel object of RequestModel that contains the various request
	 *                     variables
	 * @param response     the ServletResponse interface to provide HTTP-specific
	 *                     functionality in sending a response
	 * @return returns a pdf with successfully message
	 * @throws Exception it indicates conditions that a reasonable application might
	 *                   want to catch.
	 */
	@PostMapping(value = "/pdf")
	public ResponseEntity<Object> createPdf(@RequestBody @Valid RequestModel requestModel, HttpServletResponse response)
			throws Exception {
		log.info("Inside PDF controller");
		response.setContentType("application/pdf");

		String headerKey = "Content-Disposition";
		System.out.println("ccccccccccccccccccccccccccccccccccccccccc");
		System.out.println(requestModel.getMetadata().getCreator());
		String headerValue = "attachment; filename=pdf_" + requestModel.getMetadata().getCreator() + "_"
				+ requestModel.getMetadata().getSubject() + "_" + requestModel.getMetadata().getCreationdate() + ".pdf";

		response.setHeader(headerKey, headerValue);
		pdfService.exportPdf(response, requestModel);
		return new ResponseEntity<>("Successfully", HttpStatus.OK);
	}
}
