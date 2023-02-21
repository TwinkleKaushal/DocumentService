package com.connection.document.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServerError {
	long timestamp;
	Integer status;
	Integer errorCode;
	String errorMessage;
	long traceId;
	String errorDeatils;
	String path;
}
