package hh.forest_of_habits.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Schema(description = "Секретный ключ и QR code")
@Builder
@Data
@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
public class F2AResponse {
    @Schema(description = "Секретный ключ", example = "qjhwgej3fryui32hjr32")
    private String secret;
    @Schema(description = "Возможно будет возвращаться QR code в base64, но может и нет")
    private byte[] qr;
}
