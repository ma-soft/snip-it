package eu.amsoft.snipit.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SnippetModel {

    @Id
    private String id;
    private String title;
    private String content;

}
