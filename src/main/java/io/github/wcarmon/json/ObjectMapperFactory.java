package io.github.wcarmon.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * Builds an idiomatic ObjectMapper.
 *
 * <p>Stricter in areas that create error hiding antipatterns.
 *
 * <p>More lenient to align better with JSON5.
 */
public final class ObjectMapperFactory {

    private ObjectMapperFactory() {}

    /**
     * Build a preconfigured ObjectMapper.
     *
     * @return ObjectMapper with our idiomatic configuration
     */
    public static ObjectMapper build() {
        return preconfigureBuilder().build();
    }

    /**
     * Allows subsequent configuration of ObjectMapper.
     *
     * @return a preconfigured builder for ObjectMapper
     */
    public static JsonMapper.Builder preconfigureBuilder() {
        return JsonMapper.builder()
                .addModules(new JavaTimeModule())
                .disable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT)
                .disable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
                .disable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
                .disable(DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES)
                .enable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES)
                .enable(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)
                .enable(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES)
                .enable(DeserializationFeature.FAIL_ON_MISSING_EXTERNAL_TYPE_ID_PROPERTY)
                .enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
                .enable(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS)
                .enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY)
                .enable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS)
                .enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .enable(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS)
                .disable(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS) // TODO: verify
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS)
                .enable(JsonReadFeature.ALLOW_JAVA_COMMENTS)
                .enable(JsonReadFeature.ALLOW_LEADING_DECIMAL_POINT_FOR_NUMBERS)
                .enable(JsonReadFeature.ALLOW_NON_NUMERIC_NUMBERS)
                .enable(JsonReadFeature.ALLOW_TRAILING_COMMA)
                .enable(SerializationFeature.FAIL_ON_SELF_REFERENCES)
                .serializationInclusion(JsonInclude.Include.NON_NULL);

        // -- Bug: converts 'a""b' to "a\"b"
        // .enable(JsonReadFeature.ALLOW_SINGLE_QUOTES)

        // -- GOTCHA: makes it harder to search *.json5 files
        // .enable(JsonReadFeature.ALLOW_UNQUOTED_FIELD_NAMES)

        // -- If you use Kotlin
        // new KotlinModule.Builder()
        //     .configure(KotlinFeature.NullToEmptyCollection, true)
        //     .configure(KotlinFeature.NullToEmptyMap, true)
        //     // use my default values when null in json
        //     .configure(KotlinFeature.NullIsSameAsDefault, true)
        //     .build())
    }
}
