package ar.com.ada.sp.validations.handler.advice;

import ar.com.ada.sp.validations.handler.exception.validations.ApiErrorsResponseBody;
import ar.com.ada.sp.validations.handler.exception.validations.ApiFieldError;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ApiValidationExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        //crear lista de errores - se obtiene la lista de todos los errores ocurridos
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

        //en base a la lista anterior se genera una nueva lista con objetos ApiFieldError
        //que describen la info de los errores
        List<ApiFieldError> apiFieldErrors = fieldErrors
                .stream()
                //por cada objeto FieldError se crea uno de tipo ApiFieldError
                //con la info necesaria
                .map(fieldError -> new ApiFieldError(
                        fieldError.getField(),
                        fieldError.getCode(),
                        fieldError.getDefaultMessage())
                )
                //se convierte el mapa a una lista List<ApiFieldError>
                .collect(Collectors.toList());

        //se crea un objeto ApiErrorResponseBody que contiene la lista apiFieldErrors
        //y la informacion de tiempo, status code y status response
        ApiErrorsResponseBody apiErrorsResponseBody = new ApiErrorsResponseBody(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                apiFieldErrors);

        //se retorna el response final de los errores
        return ResponseEntity.badRequest().body(apiErrorsResponseBody);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        //dividimos el mensaje por los saltos de line (\n) y lo almacenamos en una lista
        List<String> exMessagelines = Arrays.asList(ex.getMessage().split("\n"));

        //Del 1er elemento de la lista exMessagelines (String), lo dividimos por 2 puntos (:)
        //y lo almacenamos en otra lista String
        List<String> messageLines = Arrays.asList(exMessagelines.get(0).split(":"));

        //de la lista messageLines, obtenemos el ultimo elemento que nos indica el error que ocurrio
        String message = messageLines.get(messageLines.size() - 1).trim();

        //del 2do elemento de la lista exMessagelines (String), lo dividimos por doble comillas (\")
        //y lo almacenamos en otra lista (String)
        List<String> filedLines = Arrays.asList(exMessagelines.get(1).split("\""));

        //de la filedLines, obtenemos el 2do elemento que nos indica el campo que tiene la validacion
        String field = filedLines.get(1);

        //se crea una lista con el error de tipo ApiFieldError y los datos de field y message
        List<ApiFieldError> apiFieldErrors = Arrays.asList(new ApiFieldError(field, "", message));

        //se crea un objeto ApiErrorsResponseBody que contiene la lista apiFieldErrors
        //y la informacion de tiempo, status code y status response
        ApiErrorsResponseBody apiErrorsResponseBody = new ApiErrorsResponseBody(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                apiFieldErrors);

        //retorna el response final de los errores
        return ResponseEntity.badRequest().body(apiErrorsResponseBody);



    }
}
