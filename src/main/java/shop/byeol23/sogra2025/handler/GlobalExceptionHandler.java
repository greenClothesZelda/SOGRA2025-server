// java
package shop.byeol23.sogra2025.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.BindException;

import lombok.extern.slf4j.Slf4j;
import shop.byeol23.sogra2025.handler.dto.ErrorResponse;
import shop.byeol23.sogra2025.security.AccountLockedException;

import jakarta.validation.ConstraintViolationException;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	// 유효성 검사 실패 (RequestBody)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
		String message = ex.getBindingResult()
			.getAllErrors()
			.stream()
			.map(error -> error.getDefaultMessage())
			.collect(Collectors.joining(", "));
		log.warn("Validation failed: {}", message);
		return new ErrorResponse("잘못된 요청", message);
	}

	// 유효성 검사 실패 (form, @ModelAttribute 등)
	@ExceptionHandler(BindException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse handleBindException(BindException ex) {
		String message = ex.getBindingResult()
			.getAllErrors()
			.stream()
			.map(error -> error.getDefaultMessage())
			.collect(Collectors.joining(", "));
		log.warn("Validation failed: {}", message);
		return new ErrorResponse("잘못된 요청", message);
	}

	// 경로/쿼리 파라미터 등에서 발생하는 제약 위반
	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse handleConstraintViolation(ConstraintViolationException ex) {
		String message = ex.getConstraintViolations()
			.stream()
			.map(cv -> cv.getMessage())
			.collect(Collectors.joining(", "));
		log.warn("Constraint violations: {}", message);
		return new ErrorResponse("잘못된 요청", message);
	}

	// 계정 잠김 처리
	@ExceptionHandler(AccountLockedException.class)
	@ResponseStatus(HttpStatus.LOCKED)
	public ErrorResponse handleAccountLocked(AccountLockedException ex){
		log.warn("Account locked: {}", ex.getMessage());
		return new ErrorResponse("계정잠김", ex.getMessage());
	}

	//서버 책임
	@ExceptionHandler({
		Exception.class,
	})
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorResponse handleAllExceptions(Exception ex) {
		log.error("Unhandled exception occurred: ", ex);
		return new ErrorResponse("UNKNOWN_ERROR", "An unexpected error occurred.");
	}
}
