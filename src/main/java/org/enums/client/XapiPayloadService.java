package org.enums.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.enums.xapi.model.XapiStatement;
import org.enums.xapi.validation.XapiValidator;

import java.util.List;

class XapiPayloadService {

    private final XapiValidator validator = new XapiValidator();

    String serialize(Object input) throws JsonProcessingException {
        if (input == null) {
            throw new IllegalArgumentException("Input is null");
        }

        if (input instanceof XapiStatement s) {
            validate(s);
            return StatementSerializer.toJson(s);
        }

        if (input instanceof List<?> list) {
            if (list.isEmpty()) {
                throw new IllegalArgumentException("Batch is empty");
            }
            for (Object o : list) {
                if (!(o instanceof XapiStatement s)) {
                    throw new IllegalArgumentException("Batch contains non-XapiStatement");
                }
                validate(s);
            }
            return JsonMapper.INSTANCE.writeValueAsString(list);
        }

        throw new IllegalArgumentException("Unsupported input type");
    }

    private void validate(XapiStatement s) {
        XapiValidator.ValidationResult vr = validator.validate(s);
        if (!vr.isValid()) {
            throw new IllegalArgumentException(String.join("\n", vr.getMessages()));
        }
    }
}
