package testing.fitbitData.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import testing.fitbitData.config.GoogleHealthProperties;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GoogleHealthApiService {
    // fo the checking

    private final GoogleHealthProperties googleHealthProperties;
    private final GoogleHealthOAuthService googleHealthOAuthService;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public Map<String, Object> getIdentity(UUID patientId) {
        return callApi(patientId, googleHealthProperties.getApiBaseUrl() + "/v4/users/me/identity");
    }

    public Map<String, Object> getProfile(UUID patientId) {
        return callApi(patientId, googleHealthProperties.getApiBaseUrl() + "/v4/users/me/profile");
    }

    public Map<String, Object> getGoogleUserInfo(UUID patientId) {
        return callApi(patientId, "https://openidconnect.googleapis.com/v1/userinfo");
    }

    public Map<String, Object> getPeopleProfile(UUID patientId) {
        return callApi(patientId, "https://people.googleapis.com/v1/people/me?personFields=names,emailAddresses,birthdays,genders,photos");
    }

    public Map<String, Object> getHeartRate(UUID patientId, String startTime, String endTime) {
        return getRawData(patientId, "heart-rate", startTime, endTime);
    }

    public Map<String, Object> getSleep(UUID patientId, String startTime, String endTime) {
        return getRawData(patientId, "sleep", startTime, endTime);
    }


    public Map<String, Object> getActiveEnergyBurned(UUID patientId) {
        return getListData(patientId, "active-energy-burned");
    }

    private Map<String, Object> getListData(UUID patientId, String dataType) {
        String url = UriComponentsBuilder.fromHttpUrl(googleHealthProperties.getApiBaseUrl() + "/v4/users/me/dataTypes/" + dataType + "/dataPoints").queryParam("pageSize", 100).build().encode().toUriString();

        return callApi(patientId, url);
    }

    public Map<String, Object> getTotalCalories(UUID patientId) {
        String url = googleHealthProperties.getApiBaseUrl() + "/v4/users/me/dataTypes/total-calories/dataPoints:dailyRollUp";

        return callDailyRollupApi(patientId, url);
    }

//    private Map<String, Object> getDailyRollupData(UUID patientId, String dataType, int startYear, int startMonth, int startDay, int endYear, int endMonth, int endDay) {
//        String url = googleHealthProperties.getApiBaseUrl() + "/v4/users/me/dataTypes/" + dataType + "/dataPoints:dailyRollUp";
//
//        return callDailyRollupApi(patientId, url, startYear, startMonth, startDay, endYear, endMonth, endDay);
//    }

    private Map<String, Object> callDailyRollupApi(UUID patientId, String url) {
        String accessToken = googleHealthOAuthService.getValidAccessToken(patientId);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(MediaType.parseMediaTypes("application/json"));

        String requestBody = """
                {
                  "range": {
                    "start": {
                      "date": {
                        "year": 2026,
                        "month": 7,
                        "day": 1
                      },
                      "time": {
                        "hours": 0,
                        "minutes": 0,
                        "seconds": 0,
                        "nanos": 0
                      }
                    },
                    "end": {
                      "date": {
                        "year": 2026,
                        "month": 7,
                        "day": 14
                      },
                      "time": {
                        "hours": 0,
                        "minutes": 0,
                        "seconds": 0,
                        "nanos": 0
                      }
                    }
                  },
                  "windowSizeDays": 1,
                  "pageSize": 14,
                  "dataSourceFamily": "users/me/dataSourceFamilies/all-sources"
                }
                """;

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        return parseResponse(response.getBody());
    }

    public Map<String, Object> getRawData(UUID patientId, String dataType, String startTime, String endTime) {
        String accessToken = googleHealthOAuthService.getValidAccessToken(patientId);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setAccept(MediaType.parseMediaTypes("application/json"));

        HttpEntity<Void> request = new HttpEntity<>(headers);

        String url = UriComponentsBuilder.fromHttpUrl(googleHealthProperties.getApiBaseUrl() + "/v4/users/me/dataTypes/" + dataType + "/dataPoints").queryParam("pageSize", 100).build().encode().toUriString();

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

        return parseResponse(response.getBody());
    }

    private Map<String, Object> callApi(UUID patientId, String url) {
        String accessToken = googleHealthOAuthService.getValidAccessToken(patientId);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setAccept(MediaType.parseMediaTypes("application/json"));

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

        return parseResponse(response.getBody());
    }

    private Map<String, Object> parseResponse(String responseBody) {
        try {
            return objectMapper.readValue(responseBody, new TypeReference<Map<String, Object>>() {
            });
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse API response", e);
        }
    }
}

