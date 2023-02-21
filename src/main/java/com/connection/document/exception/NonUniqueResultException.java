package com.connection.document.exception;

import lombok.NoArgsConstructor;
@NoArgsConstructor
public class NonUniqueResultException extends Exception {

		private static final long serialVersionUID = 1L;
		public NonUniqueResultException(String message) {
			super(message);
		}
}
