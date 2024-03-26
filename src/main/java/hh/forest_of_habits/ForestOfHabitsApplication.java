package hh.forest_of_habits;

import hh.forest_of_habits.config.SecurityProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties({
        SecurityProperties.class
})
@SpringBootApplication
public class ForestOfHabitsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ForestOfHabitsApplication.class, args);
    }

}
