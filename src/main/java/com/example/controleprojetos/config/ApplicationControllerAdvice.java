package com.example.controleprojetos.config;

import com.example.controleprojetos.dtos.ApiErrorDTO;
import com.example.controleprojetos.exceptions.RegraNegocioException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ApplicationControllerAdvice {

    @ExceptionHandler(RegraNegocioException.class)
    public ApiErrorDTO handleRegraNegocioException(RegraNegocioException ex) {
        String msg = ex.getMessage();
        return new ApiErrorDTO(msg);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ApiErrorDTO handleCorpoInvalido(HttpMessageNotReadableException ex) {
        return new ApiErrorDTO(
            "Corpo da requisição inválido. Envie um JSON válido e datas no formato AAAA-MM-DD."
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ApiErrorDTO handleParametroInvalido(MethodArgumentTypeMismatchException ex) {
        return new ApiErrorDTO(
            "Parâmetro '" + ex.getName()
                + "' inválido. Use IDs inteiros e datas no formato AAAA-MM-DD."
        );
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ApiErrorDTO handleParametroAusente(MissingServletRequestParameterException ex) {
        return new ApiErrorDTO(
            "O parâmetro '" + ex.getParameterName() + "' é obrigatório."
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiErrorDTO handleIntegridade(DataIntegrityViolationException ex) {
        return new ApiErrorDTO(
            "Não foi possível concluir a operação: vínculo duplicado ou dados inconsistentes."
        );
    }
}