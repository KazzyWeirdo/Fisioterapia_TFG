package com.tfg.adapter.out.polar;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;

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
    void testFetchDailyData_Success() {
        String accessToken = "fake-token";
        Long userId = 12345L;

        doNothing().when(polarRepositoryAdapter).processTransactionRaw(anyString(), any(HttpEntity.class), any());

        Optional<PniReport> resultOpt = polarRepositoryAdapter.fetchDailyData(accessToken, userId);

        verify(polarRepositoryAdapter, times(2)).processTransactionRaw(urlCaptor.capture(), any(), callbackCaptor.capture());

        List<String> urls = urlCaptor.getAllValues();
        List<Consumer<JsonNode>> callbacks = callbackCaptor.getAllValues();

        int sleepIndex = urls.get(0).contains("/sleep") ? 0 : 1;

        ObjectNode sleepJson = objectMapper.createObjectNode();
        sleepJson.put("sleep_start_time", "2023-10-27T22:00:00Z");
        sleepJson.put("sleep_end_time", "2023-10-28T06:00:00Z");

        callbacks.get(sleepIndex).accept(sleepJson);

        int rechargeIndex = urls.get(0).contains("/nightly-recharge") ? 0 : 1;

        ObjectNode rechargeJson = objectMapper.createObjectNode();
        ObjectNode ansStatus = rechargeJson.putObject("ans_charge_status");
        ansStatus.put("heart_rate_variability_rmssd", 55.5);
        ansStatus.put("ans_charge", 10);

        callbacks.get(rechargeIndex).accept(rechargeJson);
    }


    @Test
    void testFetchDailyData_WithDoAnswer_Success() {
        String accessToken = "fake-token";
        Long userId = 12345L;
        doAnswer(invocation -> {
            String url = invocation.getArgument(0);
            Consumer<JsonNode> callback = invocation.getArgument(2);

            if (url.endsWith("/sleep")) {
                ObjectNode sleepJson = objectMapper.createObjectNode();
                sleepJson.put("sleep_start_time", "2023-10-27T22:00:00.000Z");
                sleepJson.put("sleep_end_time", "2023-10-28T06:30:00.000Z");
                callback.accept(sleepJson);
            } else if (url.endsWith("/nightly-recharge")) {
                ObjectNode rechargeJson = objectMapper.createObjectNode();
                ObjectNode ansStatus = rechargeJson.putObject("ans_charge_status");
                ansStatus.put("heart_rate_variability_rmssd", 60.0);
                ansStatus.put("ans_charge", 5);
                callback.accept(rechargeJson);
            }
            return null;
        }).when(polarRepositoryAdapter).processTransactionRaw(anyString(), any(HttpEntity.class), any());

        Optional<PniReport> resultOpt = polarRepositoryAdapter.fetchDailyData(accessToken, userId);

        assertTrue(resultOpt.isPresent());
        PniReport report = resultOpt.get();

        assertEquals(8.5, report.getHours_asleep(), 0.01);
        assertEquals(60.0, report.getHrv(), 0.01);
        assertEquals(5, report.getStress());
    }

    @Test
    void testFetchDailyData_ExceptionHandled() {
        doThrow(new RuntimeException("API Error"))
                .when(polarRepositoryAdapter).processTransactionRaw(anyString(), any(), any());

        Optional<PniReport> result = polarRepositoryAdapter.fetchDailyData("token", 1L);

        assertFalse(result.isPresent());
    }
}
