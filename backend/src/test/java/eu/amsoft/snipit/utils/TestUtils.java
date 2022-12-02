package eu.amsoft.snipit.utils;

import eu.amsoft.snipit.models.SnippetModel;
import eu.amsoft.snipit.ressources.dto.SnippetDto;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TestUtils {

    private TestUtils() {
        // empêche l'instanciation de la classe
    }

    public static SnippetDto getRandomSnippetDto() {
        return SnippetDto
                .builder()
                .id(UUID.randomUUID().toString())
                .title(RandomStringUtils.randomAlphanumeric(10))
                .content(RandomStringUtils.randomAlphanumeric(100))
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
        return SnippetModel
                .builder()
                .id(UUID.randomUUID().toString())
                .title(RandomStringUtils.randomAlphanumeric(10))
                .content(RandomStringUtils.randomAlphanumeric(100))
                .build();
    }

    public static List<SnippetModel> getRandomSnippetModelList(final int size) {
        final List<SnippetModel> models = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            models.add(getRandomSnippetModel());
        }

        return models;
    }

}
