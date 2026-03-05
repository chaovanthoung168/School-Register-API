package co.thoung.schoolregistration.exception;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ErrorDetailResponse <T>{
    private String code;
    private T description;
}
