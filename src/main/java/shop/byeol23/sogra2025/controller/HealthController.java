package shop.byeol23.sogra2025.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
	@GetMapping("/health")
	public String healthCheck() {
		return "OK";
	}
}
