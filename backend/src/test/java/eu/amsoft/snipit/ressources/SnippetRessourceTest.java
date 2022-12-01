package eu.amsoft.snipit.ressources;

import eu.amsoft.snipit.ressources.dto.SnippetDto;
import eu.amsoft.snipit.services.SnippetService;
import eu.amsoft.snipit.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Random;

import static eu.amsoft.snipit.utils.Uris.SNIPPET_RESSOURCE_BASE_URI;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SnippetRessource.class)
class SnippetRessourceTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    SnippetService snippetService;

    Random random = new Random();

    @Test
    void should_return_pagination_of_snippets() throws Exception {
        // given
        final List<SnippetDto> expectedResults = TestUtils.getRandomSnippetDtoList(random.nextInt(50));
        given(snippetService.getAll(any(Pageable.class))).willReturn(new PageImpl<>(expectedResults));

        // when
        final ResultActions response = mvc.perform(get(SNIPPET_RESSOURCE_BASE_URI));

        // then
        response
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.numberOfElements", is(expectedResults.size())))
                .andReturn().getResponse();

        for (int i = 0; i < expectedResults.size(); i++) {
            final String contentPath = String.format("$.content[%d]", i);
            // on vérifie, champs par champs, que les objets reçus sont identiques à ceux attendus
            response
                    .andExpect(jsonPath(contentPath + ".id", is(expectedResults.get(i).getId())))
                    .andExpect(jsonPath(contentPath + ".title", is(expectedResults.get(i).getTitle())))
                    .andExpect(jsonPath(contentPath + ".content", is(expectedResults.get(i).getContent())));
        }

        verify(snippetService, times(1)).getAll(any(Pageable.class));
    }
}