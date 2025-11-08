package shop.byeol23.sogra2025.landmark.dto.external;

import shop.byeol23.sogra2025.landmark.entity.Landmark;

public record LandmarkDetailResponse(
	String name,
	double x,
	double y,
	long recommendationCount,
	long visitCount,
	String description,
	String imageUrl,
	String address
) {
	public LandmarkDetailResponse(Landmark landmark) {
		this(
			landmark.getLandmarkName(),
			landmark.getX(),
			landmark.getY(),
			landmark.getRecommendationCount(),
			landmark.getVisitCount(),
			landmark.getDescription(),
			landmark.getImageUrl(),
			landmark.getAddress()
		);
	}
}
