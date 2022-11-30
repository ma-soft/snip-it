package eu.amsoft.snipit.ressources.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SnippetDto {
    private String id;
    private String title;
    private String content;
}
