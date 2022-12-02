package eu.amsoft.snipit.ressources.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class SnippetDto {
    private String id;
    private String title;
    private String content;
}
