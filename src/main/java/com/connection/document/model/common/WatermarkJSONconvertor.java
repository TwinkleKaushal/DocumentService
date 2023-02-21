package com.connection.document.model.common;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import com.connection.document.model.WatermarkModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Converter(autoApply = true)
public class WatermarkJSONconvertor implements AttributeConverter<WatermarkModel, String> {

	ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * Converts the value stored in the entity attribute into thedata representation
	 * to be stored in the database
	 */
	@Override
	public String convertToDatabaseColumn(WatermarkModel attribute) {
		try {
			return objectMapper.writeValueAsString(attribute);
		} catch (JsonProcessingException e) {
			return null;
		}
	}

	/**
	 * Converts the data stored in the database column into thevalue to be stored in
	 * the entity attribute.
	 */
	@Override
	public WatermarkModel convertToEntityAttribute(String dbData) {
		try {
			return objectMapper.readValue(dbData, new TypeReference<WatermarkModel>() {
			});
		} catch (JsonProcessingException e) {

			return null;
		}
	}
}
