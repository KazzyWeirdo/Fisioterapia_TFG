package com.tfg.adapter.out.polar;

import com.tfg.model.patient.PatientFactory;
import com.tfg.patient.Patient;
import com.tfg.port.out.polar.PolarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class PolarRepositoryAdapterTest {

    private final Patient TEST_PATIENT = PatientFactory.createTestPatient("hola@gmail.com", "85729487J");

    @InjectMocks
    private PolarRepositoryAdapter polarRepositoryAdapter;

    @Mock
    private RestTemplate restTemplate = mock(RestTemplate.class);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        polarRepositoryAdapter = new PolarRepositoryAdapter();

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
}
