package shop.byeol23.sogra2025.context;

import shop.byeol23.sogra2025.member.dto.internal.MemberInfo;

public final class MemberContext {
	private static final ThreadLocal<MemberInfo> CURRENT = new ThreadLocal<>();

	public static void set(MemberInfo info) {
		CURRENT.set(info);
	}

	public static MemberInfo get() {
		return CURRENT.get();
	}

	public static void clear() {
		CURRENT.remove();
	}
}
