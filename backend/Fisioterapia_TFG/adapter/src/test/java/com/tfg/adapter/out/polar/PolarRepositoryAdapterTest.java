package com.tfg.adapter.out.polar;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tfg.model.patient.PatientFactory;
import com.tfg.patient.Patient;
import com.tfg.pni.PniReport;
import com.tfg.port.out.polar.PolarRepository;
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
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
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

    @Captor
    private ArgumentCaptor<String> urlCaptor;

    @Captor
    private ArgumentCaptor<Consumer<JsonNode>> callbackCaptor;

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

        // Assert that the generated URL contains the expected parameters
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
        String expectedUrl = "https://www.polaraccesslink.com/v3/users";

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
        String dateStr = LocalDate.now().minusDays(1).toString();

        JsonNodeFactory factory = JsonNodeFactory.instance;

        ObjectNode mockSleep = factory.objectNode();
        mockSleep.put("sleep_start_time", "2023-10-27T22:00:00+00:00");
        mockSleep.put("sleep_end_time", "2023-10-28T06:00:00+00:00");

        ObjectNode mockRecharge = factory.objectNode();
        ObjectNode ansNode = mockRecharge.putObject("ans_charge_status");
        ansNode.put("heart_rate_variability_rmssd", 55.5);
        ansNode.put("ans_charge", -2.0);

        String expectedSleepUrl = "https://www.polaraccesslink.com/v3/users/sleep/" + dateStr;
        when(restTemplate.exchange(
                eq(expectedSleepUrl),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(JsonNode.class))
        ).thenReturn(new ResponseEntity<JsonNode>(mockSleep, HttpStatus.OK));

        String expectedRechargeUrl = "https://www.polaraccesslink.com/v3/users/nightly-recharge/" + dateStr;
        when(restTemplate.exchange(
                eq(expectedRechargeUrl),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(JsonNode.class))
        ).thenReturn(new ResponseEntity<JsonNode>(mockRecharge, HttpStatus.OK));

        Optional<PniReport> result = polarRepositoryAdapter.fetchDailyData(TEST_PATIENT);

        assertThat(result).isPresent();
        assertThat(result.get().getHours_asleep()).isEqualTo(8.0);
        assertThat(result.get().getHrv()).isEqualTo(55.5);
        assertThat(result.get().getStress()).isEqualTo(-2);
    }
}
