package com.connection.document.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WatermarkModel {
	
//	@Column(name = "imageWatermark",columnDefinition = "text")
	private String imageWatermark;
	
//	@Column(name = "textWatermark",columnDefinition = "text")
	private String textWatermark;

}
