package nl.hoepsch.pm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The Spring Boot application.
 */
@SuppressWarnings({"checkstyle:HideUtilityClassConstructor", "PMD.UseUtilityClass"})
@SpringBootApplication
public class P1ReaderHackPublisherApp {

    /**
     * The main method to run.
     */
    public static void main(final String[] args) {
        SpringApplication.run(P1ReaderHackPublisherApp.class, args);
    }
}
