package com.connection.document.model.common;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MetaData {
	
	private LocalDateTime creationdate;
	private LocalDateTime modeificationdate;
	private String creator;
	private String author;
	private String title;
	private String subject;

}
