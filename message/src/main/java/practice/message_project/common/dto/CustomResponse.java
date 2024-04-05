package practice.message_project.common.dto;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomResponse<T>{

	private int status;

	private T data;

	private String message;

	private CustomResponse(int status, T data, String message) {
		this.status = status;
		this.data = data;
		this.message = message;
	}

	public static <T> CustomResponse<T> ok(T data) {
		return new CustomResponse<>(HttpStatus.OK.value(), data, null);
	}

	public static <T> CustomResponse<T> ok() {
		return new CustomResponse<>(HttpStatus.OK.value(), null, null);
	}

	public static <T> CustomResponse<T> of(int status,T data, String message) {
		return new CustomResponse<>(status, data, message);
	}

	// public static <T> CustomResponse<T> error(CustomException e) {
	// 	return new CustomResponse<>(e.getErrorCode().getHttpStatus().value(), null, e.getErrorCode().getMessage());
	// }

}
