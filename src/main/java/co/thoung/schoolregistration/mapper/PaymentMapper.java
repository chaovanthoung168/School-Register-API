package co.thoung.schoolregistration.mapper;

import co.thoung.schoolregistration.domain.PaymentManagement;
import co.thoung.schoolregistration.feature.Payment.dto.PaymentResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    PaymentResponse toPaymentResponse(PaymentManagement paymentManagement);

}
