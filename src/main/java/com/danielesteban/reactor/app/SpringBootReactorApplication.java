package com.danielesteban.reactor.app;

import com.danielesteban.reactor.app.models.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class SpringBootReactorApplication implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(SpringBootReactorApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(SpringBootReactorApplication.class, args);
    }

    @Override
    public void run(String... args) {

        List<String> users = new ArrayList<>();
        users.add("Daniel Cepeda");
        users.add("Maria Fulana");
        users.add("Pedro Fulano");
        users.add("Andres Guzman");
        users.add("Juan Fulano");
        users.add("Bruce Lee");
        users.add("Bruce Willis");
        Flux<String > nombres = Flux.fromIterable(users);
//                Flux.just("Daniel Cepeda", "Maria Fulana", "Pedro Fulano", "Andres Guzman", "Juan Fulano", "Bruce Lee", "Bruce Willis");

//                .doOnNext(System.out::println);
        Flux<Usuario> usuarios = nombres.map(nombre -> new Usuario(nombre.split(" ")[0].toUpperCase(), nombre.split(" ")[1].toUpperCase()))
                .filter(usuario -> usuario.getNombre().equalsIgnoreCase("bruce"))
                .doOnNext(usuario -> {
                    if (usuario == null)
                        throw new RuntimeException("Nombres no puede estar vacio");
                    System.out.println(usuario.getNombre().concat(" ").concat(usuario.getApellido()));
                })
                .map(usuario -> {
                    String nombre = usuario.getNombre().toLowerCase();
                    usuario.setNombre(nombre);
                    return usuario;
                });

//        nombres.subscribe(log::info);
        usuarios.subscribe(usuario -> log.info(usuario.toString()),
                error -> log.error(error.getMessage()),
                () -> log.info("Ha finalizado la ejecucion del observable con exito!!"));
    }
}
