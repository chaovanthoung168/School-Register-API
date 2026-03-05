package co.thoung.schoolregistration.feature.Payment;

import co.thoung.schoolregistration.feature.Payment.dto.BakongRequest;
import kh.gov.nbc.bakong_khqr.model.KHQRData;
import kh.gov.nbc.bakong_khqr.model.KHQRResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface KhqrService {
    KHQRResponse<KHQRData> generateQR(BakongRequest qrGenerateRequest);
    Mono<Map<String, Object>> checkTransaction(String md5);
}
