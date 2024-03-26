package hh.forest_of_habits.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "security")
public class SecurityProperties {
    /**
     * Список эндпоинтов доступных без аутентификации
     */
    private String[] ignored = {};
}
