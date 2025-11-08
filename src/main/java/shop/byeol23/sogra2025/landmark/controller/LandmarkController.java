package shop.byeol23.sogra2025.landmark.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import shop.byeol23.sogra2025.landmark.dto.external.LandmarkResponse;

@RequestMapping("landmarks")
@RestController
public class LandmarkController {
	@GetMapping("nearby")
	public List<LandmarkResponse> getNearbyLandmarks(
		@RequestParam(value = "x") double x,
		@RequestParam(value = "y") double y
	) {
		List<LandmarkResponse> landmarks = new ArrayList<LandmarkResponse>();
		landmarks.add(new LandmarkResponse("Landmark A", x + 0.001, y + 0.001));
		landmarks.add(new LandmarkResponse("Landmark B", x + 0.002, y + 0.002));
		landmarks.add(new LandmarkResponse("Landmark C", x + 0.003, y + 0.003));
		return landmarks;
	}
}
