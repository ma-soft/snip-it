package eu.amsoft.snipit.services;

import eu.amsoft.snipit.exception.EntityNotFoundException;
import eu.amsoft.snipit.models.SnippetModel;
import eu.amsoft.snipit.repositories.SnippetRepository;
import eu.amsoft.snipit.ressources.dto.SnippetDto;
import org.junit.jupiter.api.Nested;
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

import static eu.amsoft.snipit.utils.TestUtils.*;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

@ExtendWith(MockitoExtension.class)
class SnippetServiceTest {

    @Mock
    SnippetRepository snippetRepository;

    @InjectMocks
    SnippetService snippetService;

    @Nested
    class GetAllSnippetTest {
        @Test
        void should_list_all_snippets() {
            // given
            final Pageable pageable = PageRequest.of(0, 10);
            final List<SnippetModel> models = getRandomSnippetModelList(10);
            given(snippetRepository.findAll(any(Pageable.class))).willReturn(new PageImpl<>(models));

            // when
            final Page<SnippetDto> result = snippetService.getAll(pageable);

            // then
            assertThat(result, notNullValue());
            assertThat(result.getSize(), is(models.size()));
        }
    }

    @Nested
    class GetSnippetByIdTest {

        @Test
        void should_get_snippet_when_id_exists() throws EntityNotFoundException {
            // given
            final SnippetModel model = getRandomSnippetModel();
            given(snippetRepository.findById(anyString())).willReturn(of(model));

            // when
            final SnippetDto dto = snippetService.getById(model.getId());

            // then
            assertThat(dto, notNullValue());
            assertThat(dto.getId(), is(model.getId()));
            assertThat(dto.getTitle(), is(model.getTitle()));
            assertThat(dto.getContent(), is(model.getContent()));
        }

        @Test
        void should_throw_exception_when_snippet_not_found() {
            // given
            final String id = randomAlphanumeric(10);
            final EntityNotFoundException expectedException = new EntityNotFoundException(SnippetModel.class, id);
            given(snippetRepository.findById(id)).willReturn(empty());

            // when
            final EntityNotFoundException capturedException = assertThrows(EntityNotFoundException.class, () -> snippetService.getById(id));
            assertThat(capturedException.getMessage(), is(expectedException.getMessage()));
            assertThat(capturedException, isA(EntityNotFoundException.class));
        }
    }

    @Nested
    class CreateSnippetTest {
        @Test
        void should_create_snippet() {
            // given
            final SnippetDto toSave = getRandomSnippetDto(false);
            final SnippetModel model = SnippetModel.builder()
                    .id(randomAlphanumeric(10))
                    .title(toSave.getTitle())
                    .content(toSave.getContent())
                    .build();
            given(snippetRepository.save(any(SnippetModel.class))).willReturn(model);

            // when
            final SnippetDto snippet = snippetService.createSnippet(toSave);

            // then
            assertThat(snippet, notNullValue());
            assertThat(snippet.getId(), notNullValue());
            assertThat(snippet.getTitle(), is(toSave.getTitle()));
            assertThat(snippet.getContent(), is(toSave.getContent()));
        }
    }
}