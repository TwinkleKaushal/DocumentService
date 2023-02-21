package com.connection.document.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TempVariables {
	
	private String variable;
    private String type;
    private String isMandatory;
    private String defaultValue;
}

