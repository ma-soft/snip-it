package eu.amsoft.snipit.ressources.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
public class SnippetDto {
    @ApiModelProperty(notes = "Identifiant du snippet", example = "638a8b30f1cee24528606487")
    private String id;

    @ApiModelProperty(notes = "Titre du snippet", example = "Afficher un message dans la console")
    private String title;

    @ApiModelProperty(notes = "Contenu du snippet", example = "console.log('hello world');")
    private String content;

    @ApiModelProperty(notes = "Date de création du snippet", example = "2022-12-28T01:24:11.392433")
    private LocalDateTime createdAt;

    @ApiModelProperty(notes = "Date de modification du snippet", example = "2022-12-28T01:24:11.392433")
    private LocalDateTime updatedAt;
}
