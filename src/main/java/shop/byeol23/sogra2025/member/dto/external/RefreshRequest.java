package shop.byeol23.sogra2025.member.dto.external;

import jakarta.validation.constraints.NotBlank;

public record RefreshRequest(
    @NotBlank(message = "refreshToken는 비어있을 수 없습니다")
    String refreshToken
) {}

