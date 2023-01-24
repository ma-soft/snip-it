package eu.amsoft.snipit.ressources.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SnippetRequest {
    @ApiModelProperty(notes = "Titre du snippet", example = "Afficher un message dans la console")
    private String title;

    @ApiModelProperty(notes = "Contenu du snippet", example = "console.log('hello world');")
    private String content;
}
