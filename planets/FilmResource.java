package org.earth.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.earth.model.Film;
import org.earth.repository.FilmRepository;
import org.jboss.logging.annotations.Param;

import java.util.Optional;
import java.util.stream.Collectors;

@Path("/api/v1/films")
public class FilmResource {

    @Inject
    private FilmRepository filmRepository;

    @GET
    @Path("/welcome")
    @Produces(MediaType.TEXT_PLAIN)
    public String welcome(){
        return "welcome to file World";
    }

    @GET
    @Path("/film/{filmId}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getFilm(short filmId){
        Optional<Film> filmInfo = filmRepository.getFilm(filmId);
        return filmInfo.isPresent() ? filmInfo.get().getTitle() : "No Film was found";
    }

    @GET
    @Path("/film/{page}/{minLength}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getPagedData(long page,short minLength){
        return filmRepository.getFilmsPaged(page,minLength).stream().map(f -> String.format("%s (%d min)", f.getTitle(), f.getLength()))
                .collect(Collectors.joining("\n"));
    }

    @GET
    @Path("/actors/{startsWith}/{minLength}")
    @Produces(MediaType.TEXT_PLAIN)
    public String actors(String startsWith, short minLength) {
        return filmRepository.actors(startsWith, minLength)
                .map(f -> String.format("%s (%d min): %s",
                        f.getTitle(),
                        f.getLength(),
                        f.getActors().stream()
                                .map(a -> String.format("%s %s", a.getFirstName(), a.getLastName()))
                                .collect(Collectors.joining(", "))))
                .collect(Collectors.joining("\n"));
    }

    @GET
    @Path("/update/{minLength}/{rentalRate}")
    @Produces(MediaType.TEXT_PLAIN)
    public String update(short minLength, Float rentalRate) {
        filmRepository.updateRentalRate(minLength, rentalRate);
        return filmRepository.getFilms(minLength)
                .map(f -> String.format("%s (%d min) - $%f", f.getTitle(), f.getLength(), f.getRentalRate()))
                .collect(Collectors.joining("\n"));
    }

    @GET
    @Path("/film/json/{filmId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Film getFilmJsonView(short filmId){
        Optional<Film> filmInfo = filmRepository.getFilm(filmId);
        return filmInfo.orElse(null);
    }

}
