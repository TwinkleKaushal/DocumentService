package com.connection.document.model;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import com.connection.document.model.common.TemplateVariableJsonConvertor;
import com.connection.document.model.common.WatermarkJSONconvertor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TemplateModel {
 
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "agreementTemplate",columnDefinition = "text")
	private String template;
	
	@Column(unique = true)
	private String templateType;
	
	@Column(name = "templateVariables",columnDefinition = "json")
	@Convert(attributeName = "data",converter = TemplateVariableJsonConvertor.class)
	private List <TempVariables>  templateVariables;
	
	@Column(name = "watermark",columnDefinition = "json")
	@Convert(attributeName = "logo",converter = WatermarkJSONconvertor.class)
	private WatermarkModel watermark;
	
}
