package testing.fitbitData.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import testing.fitbitData.config.GoogleHealthProperties;
import testing.fitbitData.entity.PatientWearableAccount;
import testing.fitbitData.repository.PatientWearableAccountRepository;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GoogleHealthOAuthService {

    private final GoogleHealthProperties googleHealthProperties;
    private final PatientWearableAccountRepository patientWearableAccountRepository;
    private final RestTemplate restTemplate;

//    public String buildAuthorizationUrl(UUID patientId) {
//        return UriComponentsBuilder.fromHttpUrl(googleHealthProperties.getAuthorizeUrl()).queryParam("client_id", googleHealthProperties.getClientId()).queryParam("redirect_uri", googleHealthProperties.getRedirectUri()).queryParam("response_type", "code").queryParam("scope", googleHealthProperties.getScopes()).queryParam("access_type", "offline").queryParam("prompt", "consent").queryParam("state", patientId.toString()).build().toUriString();
//    }


    public String buildAuthorizationUrl(UUID patientId) {
        return UriComponentsBuilder.fromHttpUrl(googleHealthProperties.getAuthorizeUrl()).queryParam("client_id", googleHealthProperties.getClientId()).queryParam("redirect_uri", googleHealthProperties.getRedirectUri()).queryParam("response_type", "code").queryParam("scope", googleHealthProperties.getScopes()).queryParam("access_type", "offline").queryParam("prompt", "consent").queryParam("state", patientId.toString()).encode().build().toUriString();
    }

    public void exchangeCodeAndStoreTokens(String code, UUID patientId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("code", code);
        formData.add("client_id", googleHealthProperties.getClientId());
        formData.add("client_secret", googleHealthProperties.getClientSecret());
        formData.add("redirect_uri", googleHealthProperties.getRedirectUri());
        formData.add("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);

        ResponseEntity<JsonNode> response = restTemplate.exchange(googleHealthProperties.getTokenUrl(), HttpMethod.POST, request, JsonNode.class);

        JsonNode body = response.getBody();
        if (body == null) {
            throw new RuntimeException("Failed to get token response from Google");
        }

        String accessToken = body.path("access_token").asText(null);
        String refreshToken = body.path("refresh_token").asText(null);
        long expiresIn = body.path("expires_in").asLong(0);

        if (accessToken == null || accessToken.isBlank()) {
            throw new RuntimeException("Access token missing in Google token response");
        }

        PatientWearableAccount account = patientWearableAccountRepository.findByPatientIdAndProvider(patientId, "fitbit").orElseGet(PatientWearableAccount::new);

        account.setPatientId(patientId);
        account.setProvider("fitbit");
        account.setAccessTokenEncrypted(accessToken);
        if (refreshToken != null && !refreshToken.isBlank()) {
            account.setRefreshTokenEncrypted(refreshToken);
        }
        account.setTokenExpiresAt(Instant.now().plusSeconds(expiresIn));
        account.setScope(googleHealthProperties.getScopes());
        account.setConnectedAt(account.getConnectedAt() == null ? Instant.now() : account.getConnectedAt());
        account.setLastRefreshedAt(Instant.now());
        account.setStatus("active");

        patientWearableAccountRepository.save(account);
    }

    public String getValidAccessToken(UUID patientId) {
        PatientWearableAccount account = patientWearableAccountRepository.findByPatientIdAndProvider(patientId, "fitbit").orElseThrow(() -> new RuntimeException("No connected Google Health/Fitbit account found"));

        if (account.getTokenExpiresAt() != null && Instant.now().isBefore(account.getTokenExpiresAt().minusSeconds(60))) {
            return account.getAccessTokenEncrypted();
        }

        return refreshAccessToken(account);
    }

    public String refreshAccessToken(PatientWearableAccount account) {
        if (account.getRefreshTokenEncrypted() == null || account.getRefreshTokenEncrypted().isBlank()) {
            throw new RuntimeException("Refresh token missing. Reconnect the account.");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", googleHealthProperties.getClientId());
        formData.add("client_secret", googleHealthProperties.getClientSecret());
        formData.add("refresh_token", account.getRefreshTokenEncrypted());
        formData.add("grant_type", "refresh_token");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);

        ResponseEntity<JsonNode> response = restTemplate.exchange(googleHealthProperties.getTokenUrl(), HttpMethod.POST, request, JsonNode.class);

        JsonNode body = response.getBody();
        if (body == null) {
            throw new RuntimeException("Failed to refresh access token");
        }

        String accessToken = body.path("access_token").asText(null);
        long expiresIn = body.path("expires_in").asLong(0);

        if (accessToken == null || accessToken.isBlank()) {
            throw new RuntimeException("Access token missing in refresh response");
        }

        account.setAccessTokenEncrypted(accessToken);
        account.setTokenExpiresAt(Instant.now().plusSeconds(expiresIn));
        account.setLastRefreshedAt(Instant.now());

        patientWearableAccountRepository.save(account);

        return accessToken;
    }
}