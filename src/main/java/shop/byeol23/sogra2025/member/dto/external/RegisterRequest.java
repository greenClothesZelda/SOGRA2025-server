package shop.byeol23.sogra2025.member.dto.external;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
	@NotBlank(message = "loginId는 비어있을 수 없습니다")
	@Size(min = 4, max = 20, message = "loginId는 4~20자여야 합니다")
	@Pattern(regexp = "^[A-Za-z0-9_]+$", message = "loginId는 영문자, 숫자, 밑줄(_)만 허용됩니다")
	@Schema(description = "로그인 ID", example = "user123")
	String loginId,
	@NotBlank(message = "password는 비어있을 수 없습니다")
	@Size(min = 8, max = 32, message = "password는 8~32자여야 합니다")
	@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)\\S+$", message = "password는 영문자와 숫자를 포함해야 하며 공백을 포함할 수 없습니다")
	@Schema(description = "비밀번호", example = "zxcvb123")
	String password,

	@NotNull(message = "name은 비어있을 수 없습니다")
	@Size(min = 2, max = 50, message = "name은 2~50자여야 합니다")
	@Pattern(regexp = "^[A-Za-z가-힣\\s]+$", message = "name은 한글, 영문자 및 공백만 허용됩니다")
	@Schema(description = "회원 이름", example = "홍길동")
	String name
) {
}
