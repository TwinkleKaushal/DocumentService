package com.connection.document.service;

import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import com.connection.document.exception.NonUniqueResultException;
import com.connection.document.model.TempVariables;
import com.connection.document.model.common.ResponseModel;

public interface ITemplateService  {
	
	/**
	 * 
	 * @param file this is the variable that we are going to use for storing template
	 * @param type this variable describes the type of the template
	 * @param templateVariables it holds the dynamic variables
	 * @return save the template in our database
	 * @throws NonUniqueResultException 
	 */
	public ResponseModel saveTemplate(MultipartFile file,String type,List <TempVariables> templateVariables,MultipartFile imageW , String textW)throws IOException, NonUniqueResultException;
	
	/**
	 * 
	 * @param id Id of the template that we want to delete from template
	 */
	public ResponseModel deleteTemplateById(Integer id);
	
	/**
	 * 
	 * @param id Id of the template that we want to to replace from the database
	 * @param file this is our new updated Doc file
	 * @param type This is the type/name of the doc file.
	 * @param templateVariables  These are the variables that we are going to replace dynamically from template.
	 * @param imageW This is the image watermark
	 * @param textW This is the text watermark
	 * @throws IOException 
	 */
	public ResponseModel PutNewbyReplacingOld(Integer id,MultipartFile file,String type,List <TempVariables> templateVariables,MultipartFile imageW , String textW) throws IOException;

}
