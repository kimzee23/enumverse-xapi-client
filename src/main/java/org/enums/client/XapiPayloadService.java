package org.enums.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.enums.xapi.model.XapiStatement;
import org.enums.xapi.validation.XapiValidator;

import java.util.List;

public class XapiPayloadService {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private final XapiValidator validator = new XapiValidator();

    static {
        MAPPER.registerModule(new JavaTimeModule());
        MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        MAPPER.findAndRegisterModules(); // Instant, Java time, etc.
        MAPPER.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    public String serialize(Object input) {
        if (input == null) {
            throw new IllegalArgumentException("Input is null");
        }

        validateInput(input);

        try {
            return MAPPER.writeValueAsString(input);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(
                    "Serialization failed: " + e.getOriginalMessage(), e
            );
        }
    }

    private void validateInput(Object input) {
        if (input instanceof XapiStatement statement) {
            validate(statement);
            return;
        }

        if (input instanceof List<?> list) {
            if (list.isEmpty()) {
                throw new IllegalArgumentException("Batch is empty");
            }

            for (Object obj : list) {
                if (!(obj instanceof XapiStatement statement)) {
                    throw new IllegalArgumentException("Invalid batch element");
                }
                validate(statement);
            }
            return;
        }

        throw new IllegalArgumentException("Unsupported input type");
    }

    private void validate(XapiStatement statement) {
        XapiValidator.ValidationResult result = validator.validate(statement);
        if (!result.isValid()) {
            throw new IllegalArgumentException(String.join("\n", result.getMessages()));
        }
    }
}
