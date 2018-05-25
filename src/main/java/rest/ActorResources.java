package rest;

import domain.Actor;
import domain.Movie;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/actors")
@Stateless
public class ActorResources {

    @PersistenceContext(unitName="demoPU")
    EntityManager em;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Actor> getAllActors() {

        return em.createNamedQuery("actor.all", Actor.class).getResultList();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addActor(Actor actor) {
        em.persist(actor);
        return Response.ok(actor.getId()).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getActor(@PathParam("id") int id) {

        Actor result = em.createNamedQuery("actor.id", Actor.class)
                .setParameter("actorId", id)
                .getSingleResult();

        if (result == null) {

            return Response.status(404).build();
        }
        return Response.ok(result).build();
    }

    @POST
    @Path("/{actorId}/movies/{movieId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("actorId") int actorId, @PathParam("movieId") int movieId) {

        Actor actor = em.createNamedQuery("actor.id", Actor.class)
                .setParameter("actorId", actorId)
                .getSingleResult();

        Movie movie = em.createNamedQuery("movie.id", Movie.class)
                .setParameter("movieId", movieId)
                .getSingleResult();

        if(actor == null || movie == null || movie.getActors().contains(actor)) {
            return Response.status(404).build();
        }

        movie.getActors().add(actor);
        actor.getMovies().add(movie);

        return Response.ok().build();
    }


    @GET
    @Path("/{id}/movies")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Movie> getAllByActor(@PathParam("id") int id) {
        Actor result = em.createNamedQuery("actor.id", Actor.class)
                .setParameter("actorId", id)
                .getSingleResult();

        if(result == null) {
            return null;
        }

        return result.getMovies();

    }
}
