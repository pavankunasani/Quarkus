package org.earth.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import org.earth.model.Actor;
import org.earth.model.Film;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@ApplicationScoped
public class FilmRepository {

    private static final int PAGE_SIZE = 20;

    @Inject
    private EntityManager entityManager;

    // Using Criteria API to fetch a single film by filmId
    public Optional<Film> getFilm(short filmId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Film> query = cb.createQuery(Film.class);
        Root<Film> filmRoot = query.from(Film.class);

        // Create a predicate for the filter condition
        Predicate filmIdPredicate = cb.equal(filmRoot.get("filmId"), filmId);
        query.where(filmIdPredicate);

        // Execute the query and get the result (single film)
        try {
            Film film = entityManager.createQuery(query).getSingleResult();
            return Optional.of(film);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    // Using Criteria API for paginated films with a minimum length filter
    public List<Film> getFilmsPaged(long page, short minLength) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Film> query = cb.createQuery(Film.class);
        Root<Film> filmRoot = query.from(Film.class);

        // Create the filter condition (film length > minLength)
        Predicate lengthPredicate = cb.greaterThan(filmRoot.get("length"), minLength);
        query.where(lengthPredicate);

        // Order films by id (you can change this to any other field you want)
        query.orderBy(cb.asc(filmRoot.get("id")));

        // Create the query and apply pagination (OFFSET and LIMIT)
        TypedQuery<Film> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((int) (page * PAGE_SIZE)); // OFFSET
        typedQuery.setMaxResults(PAGE_SIZE); // LIMIT

        return typedQuery.getResultList();
    }

    public Stream<Film> actors(String startsWith, short minLength) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Film> query = cb.createQuery(Film.class);
        Root<Film> filmRoot = query.from(Film.class);

        // Join with the actors (assuming Film has a relationship with Actor)
        Join<Film, Actor> actorsJoin = filmRoot.join("actors", JoinType.LEFT); // Adjust based on your model

        // Predicate for title starting with 'startsWith' and length greater than 'minLength'
        Predicate titlePredicate = cb.like(filmRoot.get("title"), startsWith + "%");
        Predicate lengthPredicate = cb.greaterThan(filmRoot.get("length"), minLength);

        query.where(cb.and(titlePredicate, lengthPredicate));

        // Sort films by length in reversed order
        query.orderBy(cb.desc(filmRoot.get("length")));

        // Execute the query and get the results
        List<Film> films = entityManager.createQuery(query).getResultList();

        // Convert the List to Stream
        return films.stream();
    }
    @Transactional
    public void updateRentalRate(short minLength, Float rentalRate) {
        // Using JPQL for bulk update
        String jpql = "UPDATE Film f SET f.rentalRate = :rentalRate WHERE f.length > :minLength";

        Query query = entityManager.createQuery(jpql);
        query.setParameter("rentalRate", rentalRate);
        query.setParameter("minLength", minLength);

        // Execute the update
        query.executeUpdate();
    }
    public Stream<Film> getFilms(short minLength) {
        // Create CriteriaBuilder instance
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        // Create a CriteriaQuery object for Film entities
        CriteriaQuery<Film> query = cb.createQuery(Film.class);

        // Define the root of the query (Film entity)
        Root<Film> filmRoot = query.from(Film.class);

        // Create a Predicate for filtering by length
        Predicate lengthPredicate = cb.greaterThan(filmRoot.get("length"), minLength);

        // Apply the filter
        query.where(lengthPredicate);

        // Apply sorting by length (ascending by default)
        query.orderBy(cb.asc(filmRoot.get("length")));

        // Execute the query to get the list of films
        List<Film> films = entityManager.createQuery(query).getResultList();

        // Return the result as a stream
        return films.stream();
    }
}
