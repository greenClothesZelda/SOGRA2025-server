package shop.byeol23.sogra2025.security;

public class AccountLockedException extends RuntimeException {
	public AccountLockedException(String loginId) {
		super("해당 계정이 일시적으로 잠겼습니다: " + loginId);
	}
}

