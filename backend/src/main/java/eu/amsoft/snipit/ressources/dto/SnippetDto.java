package eu.amsoft.snipit.ressources.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SnippetDto {
    private String id;
    private String title;
    private String content;
}
