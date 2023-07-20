package com.danielesteban.reactor.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class SpringBootReactorApplication implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(SpringBootReactorApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(SpringBootReactorApplication.class, args);
    }

    @Override
    public void run(String... args) {
        Flux<String> nombres = Flux.just("Daniel", "Maria", "Pedro", "Andres", "Juan")
//                .doOnNext(System.out::println);
                .doOnNext(e -> {
                    if (e.isEmpty())
                        throw new RuntimeException("Nombres no puede estar vacio");
                    System.out.println(e);
                });

//        nombres.subscribe(log::info);
        nombres.subscribe(log::info,
                error -> log.error(error.getMessage()),
                () -> log.info("Ha finalizado la ejecucion del observable con exito!!"));
    }
}
