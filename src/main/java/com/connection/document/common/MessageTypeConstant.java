package com.connection.document.common;

import lombok.Getter;

@Getter
public enum MessageTypeConstant {
	ERROR(0), SUCCESS(1), INTERNAL_ERROR(2), OTHERS(3);

	private Integer message;

	private MessageTypeConstant(Integer messageType) {
		this.message = messageType;
	}

}
