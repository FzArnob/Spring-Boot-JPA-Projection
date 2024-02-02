package fzs.lab.jpa.projection.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class RestResponse {
    @AllArgsConstructor
    @Getter
    public static class SuccessResponse {
        private final boolean success = true;
        private final String message;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private final Object data;
    }
}
