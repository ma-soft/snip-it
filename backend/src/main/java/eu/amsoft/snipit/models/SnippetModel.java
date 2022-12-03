package eu.amsoft.snipit.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Builder
@AllArgsConstructor
@Document("snippets")
public class SnippetModel {

    @Id
    private String id;
    private String title;
    private String content;

}
