package shop.byeol23.sogra2025.party.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/party")
@RestController
public class PartyController {
	@GetMapping("{landmarkName}")
	public String getPartyByLandmark(
		@PathVariable String landmarkName
	) {
		return "landmarkName: " + landmarkName;
	}
}
