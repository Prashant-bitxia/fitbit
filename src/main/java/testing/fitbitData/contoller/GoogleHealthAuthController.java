package testing.fitbitData.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import testing.fitbitData.service.GoogleHealthApiService;
import testing.fitbitData.service.GoogleHealthOAuthService;

import java.net.URI;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/google-health")
@RequiredArgsConstructor
public class GoogleHealthAuthController {

    private final GoogleHealthOAuthService googleHealthOAuthService;
    private final GoogleHealthApiService googleHealthApiService;

    @GetMapping("/connect/{patientId}")
    public ResponseEntity<Void> connect(@PathVariable UUID patientId) {
        String authUrl = googleHealthOAuthService.buildAuthorizationUrl(patientId);
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(authUrl)).build();
    }

    @GetMapping("/callback")
    public ResponseEntity<String> callback(@RequestParam("code") String code, @RequestParam("state") String state) {
        googleHealthOAuthService.exchangeCodeAndStoreTokens(code, UUID.fromString(state));
        return ResponseEntity.ok("Google Health account connected successfully.");
    }

    @GetMapping("/identity/{patientId}")
    public ResponseEntity<Map<String, Object>> getIdentity(@PathVariable UUID patientId) {
        return ResponseEntity.ok(googleHealthApiService.getIdentity(patientId));
    }

    @GetMapping("/profile/{patientId}")
    public ResponseEntity<Map<String, Object>> getProfile(@PathVariable UUID patientId) {
        return ResponseEntity.ok(googleHealthApiService.getProfile(patientId));
    }

    @GetMapping("/google-user/{patientId}")
    public ResponseEntity<Map<String, Object>> getGoogleUser(@PathVariable UUID patientId) {
        return ResponseEntity.ok(googleHealthApiService.getGoogleUserInfo(patientId));
    }

    @GetMapping("/people-profile/{patientId}")
    public ResponseEntity<Map<String, Object>> getPeopleProfile(@PathVariable UUID patientId) {
        return ResponseEntity.ok(googleHealthApiService.getPeopleProfile(patientId));
    }

    @GetMapping("/heart-rate/{patientId}")
    public ResponseEntity<Map<String, Object>> getHeartRate(@PathVariable UUID patientId, @RequestParam String startTime, @RequestParam String endTime) {
        return ResponseEntity.ok(googleHealthApiService.getHeartRate(patientId, startTime, endTime));
    }

    @GetMapping("/sleep/{patientId}")
    public ResponseEntity<Map<String, Object>> getSleep(@PathVariable UUID patientId, @RequestParam String startTime, @RequestParam String endTime) {
        return ResponseEntity.ok(googleHealthApiService.getSleep(patientId, startTime, endTime));
    }

    @GetMapping("/active-energy/{patientId}")
    public ResponseEntity<Map<String, Object>> getActiveEnergy(@PathVariable UUID patientId) {
        return ResponseEntity.ok(googleHealthApiService.getActiveEnergyBurned(patientId));
    }

    @GetMapping("/total-calories/{patientId}")
    public ResponseEntity<Map<String, Object>> getTotalCalories(@PathVariable UUID patientId) {
        return ResponseEntity.ok(googleHealthApiService.getTotalCalories(patientId));
    }

    @GetMapping("/raw/{patientId}")
    public ResponseEntity<Map<String, Object>> getRawData(@PathVariable UUID patientId, @RequestParam String dataType, @RequestParam String startTime, @RequestParam String endTime) {
        return ResponseEntity.ok(googleHealthApiService.getRawData(patientId, dataType, startTime, endTime));
    }
}


//------------------------------------------------------------------------------------------
//package testing.fitbitData.controller;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import testing.fitbitData.service.GoogleHealthApiService;
//import testing.fitbitData.service.GoogleHealthOAuthService;
//
//import java.net.URI;
//import java.util.Map;
//import java.util.UUID;
//
//@RestController
//@RequestMapping("/api/google-health")
//@RequiredArgsConstructor
//public class GoogleHealthAuthController {
//
//    private final GoogleHealthOAuthService googleHealthOAuthService;
//    private final GoogleHealthApiService googleHealthApiService;
//
//    @GetMapping("/connect/{patientId}")
//    public ResponseEntity<Void> connect(@PathVariable UUID patientId) {
//        String authUrl = googleHealthOAuthService.buildAuthorizationUrl(patientId);
//        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(authUrl)).build();
//    }
//
//    @GetMapping("/callback")
//    public ResponseEntity<String> callback(@RequestParam("code") String code, @RequestParam("state") String state) {
//        googleHealthOAuthService.exchangeCodeAndStoreTokens(code, UUID.fromString(state));
//        return ResponseEntity.ok("Google Health account connected successfully.");
//    }
//
//    @GetMapping("/identity/{patientId}")
//    public ResponseEntity<Map<String, Object>> getIdentity(@PathVariable UUID patientId) {
//        return ResponseEntity.ok(googleHealthApiService.getIdentity(patientId));
//    }
//
//    @GetMapping("/profile/{patientId}")
//    public ResponseEntity<Map<String, Object>> getProfile(@PathVariable UUID patientId) {
//        return ResponseEntity.ok(googleHealthApiService.getProfile(patientId));
//    }
//
//    @GetMapping("/heart-rate/{patientId}")
//    public ResponseEntity<Map<String, Object>> getHeartRate(@PathVariable UUID patientId, @RequestParam String startTime, @RequestParam String endTime) {
//        return ResponseEntity.ok(googleHealthApiService.getHeartRate(patientId, startTime, endTime));
//    }
//
//    @GetMapping("/sleep/{patientId}")
//    public ResponseEntity<Map<String, Object>> getSleep(@PathVariable UUID patientId, @RequestParam String startTime, @RequestParam String endTime) {
//        return ResponseEntity.ok(googleHealthApiService.getSleep(patientId, startTime, endTime));
//    }
//
//    @GetMapping("/raw/{patientId}")
//    public ResponseEntity<Map<String, Object>> getRawData(@PathVariable UUID patientId, @RequestParam String dataType, @RequestParam String startTime, @RequestParam String endTime) {
//        return ResponseEntity.ok(googleHealthApiService.getRawData(patientId, dataType, startTime, endTime));
//    }
//}