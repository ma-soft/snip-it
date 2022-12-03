package eu.amsoft.snipit.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonMapper {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static String toJson(final Object o) throws JsonProcessingException {
        return mapper.writeValueAsString(o);
    }
}
