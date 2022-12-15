package nmt.backend;

import nmt.backend.helper.Utils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class App {

    public static void main(String[] args) {
        //Utils.getAvailableAddresses("10.19.0.106", "10.19.0.108").forEach(System.out::println);
        SpringApplication.run(App.class, args);
    }

    //TODO: Logger (Request, Endpunkte, ...)
    @Bean
    public CommandLineRunner cmdLineRunner(ApplicationContext context) {
        return args -> {
            String[] beanNames = context.getBeanDefinitionNames();
            Arrays.stream(beanNames)
                    .filter(x -> x.startsWith("nmt"))
                    .forEach(e -> System.out.printf("Controller '%s' geladen./n", e));
        };
    }
}