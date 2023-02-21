package com.connection.document.model.common;

import java.util.List;
import java.util.Map;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestModel {

	private String templateType;
	
	@Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", message = "Password must contain:"
			+ " a digit must occur at least once\r\n" + " a lower case letter must occur at least once\r\n"
			+ " an upper case letter must occur at least once\r\n" + " a special character must occur at least once\r\n"
			+ " no whitespace allowed in the entire string\r\n" + " anything, at least eight places though\r\n")
	private String password;
	private MetaData metadata;
	private Map<String, String> dynamicVar;
	private WaterMark watermark;
	private Signature signature;
	List<List<Map<String, String>>> list;
	private EmailSenderModel emailSender;


}
