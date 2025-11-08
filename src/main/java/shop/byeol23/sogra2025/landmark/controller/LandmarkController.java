package shop.byeol23.sogra2025.landmark.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import shop.byeol23.sogra2025.context.MemberContext;
import shop.byeol23.sogra2025.landmark.dto.external.LandmarkDetailResponse;
import shop.byeol23.sogra2025.landmark.dto.external.LandmarkResponse;
import shop.byeol23.sogra2025.landmark.entity.Landmark;
import shop.byeol23.sogra2025.landmark.service.LandmarkService;
import shop.byeol23.sogra2025.member.dto.internal.MemberInfo;

@RequestMapping("landmarks")
@RestController
@RequiredArgsConstructor
public class LandmarkController {
	private final LandmarkService landmarkService;
	@GetMapping("/nearby")
	public List<LandmarkResponse> getNearbyLandmarks(
		@RequestParam(value = "x") double x,
		@RequestParam(value = "y") double y,
		@RequestParam(value = "page",  required = false) Integer page
	) {
		page = (page == null) ? 0 : page;
		Page<Landmark> landmarks = landmarkService.getLandmarks(x, y, page);
		List<LandmarkResponse> response = new ArrayList<>();
		for (Landmark landmark : landmarks) {
			response.add(new LandmarkResponse(landmark));
		}
		return response;
	}

	@PostMapping("/like")
	public void likeLandmark(
		@RequestParam(value = "landmarkName") String landmarkName
	) {
		String id = MemberContext.get().loginId();
		landmarkService.likeLandmark(id, landmarkName);
		return;
	}

	@PostMapping("/visit")
	public void visitLandmark(
		@RequestParam(value = "landmarkName") String landmarkName
	) {
		String id = MemberContext.get().loginId();
		landmarkService.visitLandmark(id, landmarkName);
		return;
	}

	@GetMapping("/details/{landmarkName}")
	public LandmarkDetailResponse getLandmarkDetails(
		@PathVariable("landmarkName") String landmarkName
	) {
		Landmark landmark = landmarkService.getLandmark(landmarkName);
		LandmarkDetailResponse response = new LandmarkDetailResponse(landmark);
		return response;
	}
}
