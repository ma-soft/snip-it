package eu.amsoft.snipit.ressources;

import eu.amsoft.snipit.ressources.dto.SnippetDto;
import eu.amsoft.snipit.services.SnippetService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static eu.amsoft.snipit.utils.Uris.SNIPPET_RESSOURCE_BASE_URI;

@RestController
@AllArgsConstructor
@RequestMapping(SNIPPET_RESSOURCE_BASE_URI)
public class SnippetRessource {

    private final SnippetService snippetService;

    @GetMapping
    public ResponseEntity<Page<SnippetDto>> getAllSnippets() {
        return ResponseEntity.ok(
                snippetService.getAll()
        );
    }
}
