package co.thoung.schoolregistration.feature.address.dto;

public record AddressResponse(
        String houseNo,      // House number
        String street,     // Street name or number
        String village,   // Phum
        String commune,   // Khum / Sangkat
        String district,   // Srok / Khan
        String province    //
) {
}
