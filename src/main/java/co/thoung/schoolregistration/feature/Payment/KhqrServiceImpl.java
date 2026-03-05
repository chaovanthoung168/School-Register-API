package co.thoung.schoolregistration.feature.Payment;

import co.thoung.schoolregistration.domain.RegistrationTransaction;
import co.thoung.schoolregistration.feature.Payment.dto.BakongRequest;
import co.thoung.schoolregistration.feature.Payment.dto.QrSession;
import co.thoung.schoolregistration.feature.RegistrationTransaction.RegistrationTransactionRepository;
import kh.gov.nbc.bakong_khqr.BakongKHQR;
import kh.gov.nbc.bakong_khqr.model.IndividualInfo;
import kh.gov.nbc.bakong_khqr.model.KHQRCurrency;
import kh.gov.nbc.bakong_khqr.model.KHQRData;
import kh.gov.nbc.bakong_khqr.model.KHQRResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import com.fasterxml.jackson.databind.JsonNode;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.databind.ObjectMapper;


@Service
@RequiredArgsConstructor
public class KhqrServiceImpl implements KhqrService {

    @Value("${bakong.merchant.account-id}")
    private String accountId;

    @Value("${bakong.merchant.merchant-name}")
    private String merchantName;

    @Value("${bakong.merchant.merchant-city}")
    private String merchantCity;

    @Value("${bakong.merchant.currency}")
    private String currencyUSD;

    @Value("${bakong.api.check-transaction-uri}")
    private String checkTransactionUri;

    @Value("${bakong.api.token}")
    private String bakongToken;

    private final Map<String, QrSession> qrSessions = new ConcurrentHashMap<>();
    private final WebClient bakongWebClient;

    private final RegistrationTransactionRepository registrationTransactionRepository;
    private final PaymentService paymentService;

    @Override
    public KHQRResponse<KHQRData> generateQR(BakongRequest qrGenerateRequest) {

        RegistrationTransaction registrationTransaction =registrationTransactionRepository.findById(qrGenerateRequest.registrationId()).orElseThrow(
                () -> new RuntimeException("Registration transaction not found")
        );

        Double departmentPrice = registrationTransaction.getRegisterStudent().getDepartment().getPrice();

        Double paymentAmount = switch (qrGenerateRequest.paymentType()){
            case FULL -> departmentPrice;
            case SEMESTER -> departmentPrice/2;
        };


        IndividualInfo individualInfo = new IndividualInfo();
        individualInfo.setBakongAccountId(accountId);
        individualInfo.setMerchantCity(merchantCity);
        individualInfo.setCurrency(KHQRCurrency.USD);
        individualInfo.setAmount(paymentAmount);
        individualInfo.setMerchantName(merchantName);

        KHQRResponse<KHQRData> response = BakongKHQR.generateIndividual(individualInfo);

        // store md5 with 5 min expiry
        if (response.getKHQRStatus().getCode() == 0) {
            String md5 = response.getData().getMd5();
            qrSessions.put(md5, new QrSession(
                    md5,
                    Instant.now().plusSeconds(300),
                    qrGenerateRequest.registrationId(),
                    paymentAmount,
                    qrGenerateRequest.paymentType()
            ));
        }
        return response;
    }

    @Override
    public Mono<Map<String, Object>> checkTransaction(String md5) {
        QrSession session = qrSessions.get(md5);

        // ✅ QR not found
        if (session == null) {
            return Mono.just(Map.of("paid", false, "expired", true, "message", "QR not found"));
        }

        // ✅ QR expired
        if (session.isExpired()) {
            qrSessions.remove(md5); // cleanup
            return Mono.just(Map.of("paid", false, "expired", true, "message", "QR expired"));
        }

        // ✅ QR valid — check with Bakong
        return bakongWebClient.post()
                .uri(checkTransactionUri)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + bakongToken)
                .bodyValue(Map.of("md5", md5))
                .retrieve()
                .bodyToMono(String.class)  // ✅ receive as raw String
                .map(raw -> {
                    System.out.println("Bakong raw response: " + raw);
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        JsonNode json = mapper.readTree(raw);  // ✅ parse manually
                        int responseCode = json.path("responseCode").asInt(-1);
                        boolean paid = responseCode == 0;
                        if (paid) {
                            paymentService.savePayment(session);
                            qrSessions.remove(md5);
                        };
                        return Map.<String, Object>of("paid", paid, "expired", false);
                    } catch (Exception e) {
                        System.err.println("Parse error: " + e.getMessage());
                        return Map.<String, Object>of("paid", false, "expired", false, "message", "Parse error: " + e.getMessage());
                    }
                })
                .onErrorResume(e -> {
                    System.err.println("Bakong API error: " + e.getMessage());
                    return Mono.just(Map.of("paid", false, "expired", false, "message", e.getMessage()));
                });
    }

}
