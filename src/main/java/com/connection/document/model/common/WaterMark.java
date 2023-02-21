package com.connection.document.model.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WaterMark {
	
	private String isMandatory;
	private String waterMarkType;

}
