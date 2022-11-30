package eu.amsoft.snipit.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;

@Getter
@Builder
@AllArgsConstructor
public class SnippetModel {

    @Id
    private String id;
    private String title;
    private String content;

}
