package es.roomie.household.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.utils.Java;

/**
 * Utility class for JSON processing using Jackson.
 * This class provides methods to convert Java objects to JSON strings and vice versa.
 */
public class JsonUtils {
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Converts a Java object to a JSON string.
     *
     * @param object the Java object to be converted to JSON
     * @return a JSON string representation of the object
     * @throws RuntimeException if there is an error during the conversion process
     */
    public static String toJson(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Converts a JSON string to a Java object of the specified class type.
     *
     * @param json the JSON string to be converted
     * @param clazz the class type to convert the JSON string to
     * @param <T> the type of the desired object
     * @return the converted Java object
     * @throws RuntimeException if there is an error during the conversion process
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json,clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
