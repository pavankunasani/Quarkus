package org.earth.repository;

import com.speedment.jpastreamer.application.JPAStreamer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.earth.model.Film;

import java.util.Optional;

@ApplicationScoped
public class FilmRepository {

    @Inject
    private JPAStreamer jpaStreamer;

    public Optional<Film> getFilm(short filmId){

        Optional<Film> film= jpaStreamer.stream(Film.class).filter(f -> f.getFilmId() == filmId)
                .findFirst();
        return film;
    }
}
