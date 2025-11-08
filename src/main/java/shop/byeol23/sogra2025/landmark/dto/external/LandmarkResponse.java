package shop.byeol23.sogra2025.landmark.dto.external;

import shop.byeol23.sogra2025.landmark.entity.Landmark;

public record LandmarkResponse(
	String name,
	double x,
	double y,
	long recommendationCount,
	long reviewCount

) {
	public LandmarkResponse(
		Landmark landmark
	){
		this(
			landmark.getLandmarkName(),
			landmark.getX(),
			landmark.getY(),
			landmark.getRecommendationCount(),
			landmark.getVisitCount()
		);
	}
}
