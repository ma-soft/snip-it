package eu.amsoft.snipit.services;

import eu.amsoft.snipit.models.SnippetModel;
import eu.amsoft.snipit.repositories.SnippetRepository;
import eu.amsoft.snipit.ressources.dto.SnippetDto;
import eu.amsoft.snipit.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class SnippetServiceTest {

    @Mock
    SnippetRepository snippetRepository;
    @InjectMocks
    SnippetService snippetService;

    Random random = new Random();

    @Test
    void should_list_all_snippets() {
        // given
        final Pageable pageable = PageRequest.of(0, 10);
        final List<SnippetModel> models = TestUtils.getRandomSnippetModelList(random.nextInt(50));
        given(snippetRepository.findAll(any(Pageable.class))).willReturn(new PageImpl<>(models));

        // when
        final Page<SnippetDto> result = snippetService.getAll(pageable);

        // then
        assertThat(result, notNullValue());
        assertThat(result.getSize(), is(models.size()));
    }
}