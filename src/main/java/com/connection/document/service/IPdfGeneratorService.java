package com.connection.document.service;

import javax.servlet.http.HttpServletResponse;
import com.connection.document.model.common.RequestModel;
import com.lowagie.text.DocumentException;

public interface IPdfGeneratorService {
	
	/**
	 * This method is used to generate pdf
	 * @param response object of HttpServletResponse that enables a servlet to formulate an HTTP response to a client.
	 * @param requestModel  object of RequestModel that holds the request variables
	 * @throws DocumentException throws when error occurred in processed document
	 */
	public String  exportPdf(HttpServletResponse response, RequestModel requestModel)throws DocumentException,Exception  ;

}
