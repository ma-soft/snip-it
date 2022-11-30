package eu.amsoft.snipit.mappers;

import eu.amsoft.snipit.models.SnippetModel;
import eu.amsoft.snipit.ressources.dto.SnippetDto;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

class SnippetMapperTest {

    @Test
    void should_map_to_dto() {
        final SnippetModel snippetModel = SnippetModel.builder()
                .id(UUID.randomUUID().toString())
                .title(RandomStringUtils.randomAlphabetic(10))
                .content(RandomStringUtils.randomAlphabetic(200))
                .build();

        final SnippetDto snippetDto = SnippetMapper.INSTANCE.mapToDto(snippetModel);

        assertThat(snippetDto, notNullValue());
        assertThat(snippetDto.getId(), is(snippetModel.getId()));
        assertThat(snippetDto.getTitle(), is(snippetModel.getTitle()));
        assertThat(snippetDto.getContent(), is(snippetModel.getContent()));
    }
}