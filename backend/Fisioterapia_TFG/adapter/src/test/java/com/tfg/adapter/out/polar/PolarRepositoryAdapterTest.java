package com.tfg.adapter.out.polar;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tfg.model.patient.PatientFactory;
import com.tfg.model.patient.Patient;
import com.tfg.model.pni.PniReport;
import com.tfg.application.port.out.polar.PolarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PolarRepositoryAdapterTest {

    private final Patient TEST_PATIENT = PatientFactory.createTestPatient("hola@gmail.com", "85729487J");

    @Spy
    @InjectMocks
    private PolarRepositoryAdapter polarRepositoryAdapter;

    @Mock
    private RestTemplate restTemplate = mock(RestTemplate.class);

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        MockitoAnnotations.openMocks(this);
        polarRepositoryAdapter = Mockito.spy(new PolarRepositoryAdapter());

        ReflectionTestUtils.setField(polarRepositoryAdapter, "clientId", "mi-fake-id");
        ReflectionTestUtils.setField(polarRepositoryAdapter, "clientSecret", "mi-fake-secret");
        ReflectionTestUtils.setField(polarRepositoryAdapter, "tokenUri", "https://token.polar.com");
        ReflectionTestUtils.setField(polarRepositoryAdapter, "authUri", "https://token.polar.com");
        ReflectionTestUtils.setField(polarRepositoryAdapter, "redirectUri", "https://token.polar.com");
        ReflectionTestUtils.setField(polarRepositoryAdapter, "restTemplate", restTemplate);
    }

    @Test
    public void testGenerateAuthUrl() {
        String state = "testState";
        String authUrl = polarRepositoryAdapter.generateAuthUrl(state);

        assert authUrl.contains("response_type=code");
        assert authUrl.contains("client_id=");
        assert authUrl.contains("redirect_uri=");
        assert authUrl.contains("scope=accesslink.read_all");
        assert authUrl.contains("state=" + state);
    }

    @Test
    void testExchangeCode() {
        String code = "testCode";

        PolarTokenResponse mockResponse = new PolarTokenResponse();
        mockResponse.setAccessToken("testAccessToken");
        mockResponse.setPolarUserId(1L);

        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(PolarTokenResponse.class)))
                .thenReturn(ResponseEntity.ok(mockResponse));

        PolarRepository.PolarAuthResult result = polarRepositoryAdapter.exchangeCode(code);

        assertThat(result.accessToken()).isEqualTo("testAccessToken");
        assertThat(result.polarUserId()).isEqualTo(1L);

        verify(restTemplate, times(1)).postForEntity(eq("https://token.polar.com"), any(HttpEntity.class), eq(PolarTokenResponse.class));
    }

    @Test
    void testRegistreUserInPolar_Success() {
        String polarAccessToken = "testToken";
        Long polarUserId = 123L;

        when(restTemplate.postForEntity(
                eq("https://www.polaraccesslink.com/v3/users"),
                any(HttpEntity.class),
                eq(String.class))
        ).thenReturn(new ResponseEntity<>("{ \"member-id\": \"123\" }", HttpStatus.OK));

        polarRepositoryAdapter.registerUserInPolar(polarAccessToken, polarUserId);

        verify(restTemplate, times(1)).postForEntity(
                eq("https://www.polaraccesslink.com/v3/users"),
                any(HttpEntity.class),
                eq(String.class)
        );
    }

    @Test
    void testFetchDailyData_Success() {
        LocalDate date = LocalDate.now().minusDays(1);
        String dateStr = date.toString();

        JsonNodeFactory factory = JsonNodeFactory.instance;

        ObjectNode mockSleep = factory.objectNode();
        mockSleep.put("sleep_start_time", "2023-10-27T22:00:00+00:00");
        mockSleep.put("sleep_end_time", "2023-10-28T06:00:00+00:00");
        mockSleep.put("sleep_continuity", 4);
        mockSleep.put("deep_sleep", 5400); // 90 minutes in seconds

        ObjectNode hrSamples = mockSleep.putObject("heart_rate_samples");
        hrSamples.put("02:00", 55);
        hrSamples.put("02:05", 60);
        hrSamples.put("02:10", 50);
        hrSamples.put("02:15", 55);

        String expectedSleepUrl = "https://www.polaraccesslink.com/v3/users/sleep/" + dateStr;
        when(restTemplate.exchange(
                eq(expectedSleepUrl),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(JsonNode.class))
        ).thenReturn(new ResponseEntity<>(mockSleep, HttpStatus.OK));

        Optional<PniReport> result = polarRepositoryAdapter.fetchDailyData(TEST_PATIENT, date);

        assertThat(result).isPresent();
        assertThat(result.get().getHours_asleep()).isEqualTo(8.0);
        assertThat(result.get().getAvg_hr()).isEqualTo(55.0);
        assertThat(result.get().getMin_hr()).isEqualTo(50);
        assertThat(result.get().getDeep_sleep()).isEqualTo(90);
        assertThat(result.get().getContinuity()).isEqualTo(4);
    }
}
