package eu.amsoft.snipit.ressources.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
public class ExceptionResponse {

    // Le message que l'on veut afficher à l'utiliseur (front)
    private String errorMessage;
    // Le code du statut HTTP (ex: 401, 404 ...)
    private int errorCode;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;

}