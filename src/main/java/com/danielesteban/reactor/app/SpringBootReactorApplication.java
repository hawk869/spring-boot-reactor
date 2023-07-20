package com.danielesteban.reactor.app;

import com.danielesteban.reactor.app.models.Usuario;
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
        Flux<Usuario> nombres = Flux.just("Daniel", "Maria", "Pedro", "Andres", "Juan")
//                .doOnNext(System.out::println);
                .map(nombre -> new Usuario(nombre.toUpperCase(), null))
                .doOnNext(usuario -> {
                    if (usuario == null)
                        throw new RuntimeException("Nombres no puede estar vacio");
                    System.out.println(usuario.getNombre());
                })
                .map(usuario -> {
                    String nombre = usuario.getNombre().toLowerCase();
                    usuario.setNombre(nombre);
                    return usuario;
                });

//        nombres.subscribe(log::info);
        nombres.subscribe(usuario -> log.info(usuario.getNombre()),
                error -> log.error(error.getMessage()),
                () -> log.info("Ha finalizado la ejecucion del observable con exito!!"));
    }
}
