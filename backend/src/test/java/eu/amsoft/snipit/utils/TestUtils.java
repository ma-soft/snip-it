package eu.amsoft.snipit.utils;

import eu.amsoft.snipit.models.SnippetModel;
import eu.amsoft.snipit.ressources.dto.SnippetDto;
import eu.amsoft.snipit.ressources.dto.SnippetRequest;

import java.util.ArrayList;
import java.util.List;

import static java.time.LocalDateTime.now;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

public class TestUtils {

    private TestUtils() {
        // empêche l'instanciation de la classe
    }

    public static SnippetRequest getRandomSnippetRequest() {
        return SnippetRequest.builder()
                .title(randomAlphanumeric(10))
                .content(randomAlphanumeric(50))
                .build();
    }

    public static SnippetDto getRandomSnippetDto() {
        return SnippetDto
                .builder()
                .id(randomAlphanumeric(10))
                .title(randomAlphanumeric(20))
                .content(randomAlphanumeric(100))
                .build();
    }

    public static List<SnippetDto> getRandomSnippetDtoList(final int size) {
        final List<SnippetDto> dtos = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            dtos.add(getRandomSnippetDto());
        }

        return dtos;
    }

    public static SnippetModel getRandomSnippetModel() {
        return getRandomSnippetModel(true);
    }

    public static SnippetModel getRandomSnippetModel(final boolean withId) {
        final SnippetModel.SnippetModelBuilder builder = SnippetModel.builder()
                .title(randomAlphanumeric(20))
                .content(randomAlphanumeric(100));

        if (withId) {
            builder.id(randomAlphanumeric(10))
                    .createdAt(now());
        }

        return builder.build();
    }

    public static List<SnippetModel> getRandomSnippetModelList(final int size) {
        final List<SnippetModel> models = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            models.add(getRandomSnippetModel());
        }

        return models;
    }

}
