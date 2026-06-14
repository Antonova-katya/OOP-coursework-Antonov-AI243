package ua.opnu.labwork2.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI onlineCoursesOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Система управління онлайн-курсами")
                        .version("1.0.0")
                        .description("""
                                Backend API для управління онлайн-курсами, викладачами, студентами,
                                навчальними модулями та записами студентів на курси. Система реалізована
                                на Spring Boot, Spring MVC, Spring Data JPA, Jakarta Bean Validation та PostgreSQL.
                                API використовує DTO для вхідних і вихідних даних, централізовану обробку помилок
                                та власні класи винятків.
                                """)
                        .contact(new Contact()
                                .name("Кафедра комп'ютерних систем")
                                .email("antonova.katya2006@gmail.com")));
    }
}