package shop.byeol23.sogra2025.member.dto.external;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record LoginRequest(
    @NotBlank(message = "loginId는 비어있을 수 없습니다")
	@Schema(example = "user123")
	String loginId,

    @NotBlank(message = "password는 비어있을 수 없습니다")
	@Schema(example = "zxcvb123")
    String password
) {
}

