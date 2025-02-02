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
}
