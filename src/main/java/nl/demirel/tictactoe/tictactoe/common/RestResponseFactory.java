package nl.demirel.tictactoe.tictactoe.common;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public final class RestResponseFactory {

    private final MessageSource messageSource;

    public RestResponse<Void> error(final String msgId) {

        return createMessage(ResponseType.ERROR, msgId, null);
    }

    public <T> RestResponse<T> info(final String msgId, final T data) {

        return createMessage(ResponseType.INFO, msgId, data);
    }

    private <T> RestResponse<T> createMessage(final ResponseType type, final String msgId, final T data) {

        final RestResponse<T> restResponse = new RestResponse<>(type, msgId);
        restResponse.setText(messageSource.getMessage(msgId, null, msgId, Locale.getDefault()));
        restResponse.setData(data);

        if (type == ResponseType.ERROR) {
            restResponse.setErrorCode(UUID.randomUUID().toString());
        }

        return restResponse;
    }
}
