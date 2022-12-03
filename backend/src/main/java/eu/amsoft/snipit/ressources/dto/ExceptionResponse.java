package eu.amsoft.snipit.ressources.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ExceptionResponse {

    @ApiModelProperty(notes = "Le message d'erreur")
    private String errorMessage;

    @ApiModelProperty(notes = "Le code d'erreur HTTP", example = "404")
    private int errorCode;

    @ApiModelProperty(notes = "La date et l'heure de l'erreur", example = "03-12-2022 02:34:04")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;

}
