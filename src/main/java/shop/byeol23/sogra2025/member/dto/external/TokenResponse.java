package shop.byeol23.sogra2025.member.dto.external;

public record TokenResponse(
    String accessToken,
    String refreshToken,
    long accessExpiresIn
) {
}

