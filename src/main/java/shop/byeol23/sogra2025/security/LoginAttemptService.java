package shop.byeol23.sogra2025.security;

import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

@Service
public class LoginAttemptService {
	private static final int MAX_FAILS = 5;
	private static final long LOCK_MILLIS = TimeUnit.MINUTES.toMillis(15);

	private static class AttemptInfo {
		int fails;
		long lockUntil; // epoch millis
	}

	private final ConcurrentHashMap<String, AttemptInfo> store = new ConcurrentHashMap<>();

	public void recordFailure(String loginId) {
		AttemptInfo info = store.computeIfAbsent(loginId, k -> new AttemptInfo());
		if (isCurrentlyLocked(info)) {
			return; // 이미 잠김 상태에서는 실패 증가시키지 않음
		}
		info.fails++;
		if (info.fails >= MAX_FAILS) {
			info.lockUntil = Instant.now().toEpochMilli() + LOCK_MILLIS;
		}
	}

	public void recordSuccess(String loginId) {
		store.remove(loginId);
	}

	public void checkBlocked(String loginId) {
		AttemptInfo info = store.get(loginId);
		if (info == null) return;
		if (isCurrentlyLocked(info)) {
			throw new shop.byeol23.sogra2025.security.AccountLockedException(loginId);
		}
		// 잠금 기간 지났으면 초기화
		if (info.lockUntil > 0 && Instant.now().toEpochMilli() > info.lockUntil) {
			store.remove(loginId);
		}
	}

	private boolean isCurrentlyLocked(AttemptInfo info) {
		return info.lockUntil > Instant.now().toEpochMilli();
	}
}
