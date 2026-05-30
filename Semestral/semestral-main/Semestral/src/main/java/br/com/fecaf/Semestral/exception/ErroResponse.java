package br.com.fecaf.Semestral.exception;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErroResponse {
    private String message;
    private Integer status;
}
