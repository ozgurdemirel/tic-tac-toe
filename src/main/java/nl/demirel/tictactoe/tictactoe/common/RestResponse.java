package nl.demirel.tictactoe.tictactoe.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestResponse<T> {

    private final ResponseType type;
    private final String msgId;

    @Setter
    private T data;
    @Setter
    private String errorCode;
    @Setter
    private String text;

    public RestResponse(final ResponseType type, final String msgId) {
        this.type = type;
        this.msgId = msgId;
    }
}
