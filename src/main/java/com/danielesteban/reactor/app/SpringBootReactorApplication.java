package com.danielesteban.reactor.app;

import com.danielesteban.reactor.app.models.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

        ejemploIterable();
        ejemploFlatMap();
        ejemploToString();
    }

    public void ejemploToString() {

        List<Usuario> users = new ArrayList<>();
        users.add(new Usuario("Daniel", "Cepeda"));
        users.add(new Usuario("Maria", "Fulana"));
        users.add(new Usuario("Pedro", "Fulano"));
        users.add(new Usuario("Andres", "Guzman"));
        users.add(new Usuario("Juan", "Fulano"));
        users.add(new Usuario("Bruce", "Willis"));
        users.add(new Usuario("Bruce", "Lee"));

        Flux.fromIterable(users)
                .map(usuario -> usuario.getNombre().toUpperCase().concat(" ").concat(usuario.getApellido().toUpperCase()))
                .flatMap(nombre -> {
                    if (nombre.contains("BRUCE"))
                        return Mono.just(nombre);
                    return Mono.empty();
                })
                .map(String::toLowerCase)
                .subscribe(log::info);
    }

    public void ejemploFlatMap() {

        List<String> users = new ArrayList<>();
        users.add("Daniel Cepeda");
        users.add("Maria Fulana");
        users.add("Pedro Fulano");
        users.add("Andres Guzman");
        users.add("Juan Fulano");
        users.add("Bruce Lee");
        users.add("Bruce Willis");

        Flux.fromIterable(users)
                .map(nombre -> new Usuario(nombre.split(" ")[0].toUpperCase(), nombre.split(" ")[1].toUpperCase()))
                .flatMap( usuario -> {
                    if (usuario.getNombre().equalsIgnoreCase("bruce"))
                        return Mono.just(usuario);
                    return Mono.empty();
                })
                .map(usuario -> {
                    String nombre = usuario.getNombre().toLowerCase();
                    usuario.setNombre(nombre);
                    return usuario;
                }).subscribe(u -> log.info(u.toString()));
    }

    public void ejemploIterable() {

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
