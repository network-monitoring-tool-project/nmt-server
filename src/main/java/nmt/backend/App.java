package nmt.backend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class App {

    public static void main(String[] args){
        SpringApplication.run(App.class, args);
    }

    //TODO: Logger (Request, Endpunkte, ...)
    @Bean
    public CommandLineRunner cmdLineRunner(ApplicationContext context){
        return args -> {
            String[] beanNames = context.getBeanDefinitionNames();
            for (Object beanName : Arrays.stream(beanNames).filter(x -> x.startsWith("nmt")).toArray()) {
                System.out.printf("Controller '%s' geladen./n", beanName);
            }
        };
    }
}