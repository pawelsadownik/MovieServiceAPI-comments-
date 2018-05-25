package rest;

import domain.Actor;
import domain.Comment;
import domain.Movie;
import domain.Rating;


import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/movies")
@Stateless
public class MovieResources {



    @PersistenceContext(unitName="demoPU")
    EntityManager em;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Movie> getAllMovies() {

        return em.createNamedQuery("movie.all", Movie.class).getResultList();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response AddMovie(Movie movie) {
        em.persist(movie);
        return Response.ok(movie.getId()).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("id") int id) {
        Movie result = em.createNamedQuery("movie.id", Movie.class)
                .setParameter("movieId", id)
                .getSingleResult();

        if(result == null) {
            return Response.status(404).build();
        }

        return Response.ok(result).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateMovie(@PathParam("id") int id, Movie movie){

        Movie result = em.createNamedQuery("movie.id", Movie.class)
                .setParameter("movieId", id)
                .getSingleResult();

        if(result==null) {
            return Response.status(404).build();
        }

        result.setName(movie.getName());
        em.persist(result);

        return Response.ok().build();
    }

//wyswietlenie komentarzy filmu
    @GET
    @Path("/{id}/comments")
    @Produces(MediaType.APPLICATION_JSON)

    public List<Comment> getComments(@PathParam("id") int id){

        Movie result = em.createNamedQuery("movie.id", Movie.class)
                .setParameter("movieId", id)
                .getSingleResult();

        if(result==null) {
            return null;
        }
    return result.getComments();

    }

 //Dodanie komentarzy do filmu

 @POST
 @Path("/{id}/comments")
 @Produces(MediaType.APPLICATION_JSON)
 public Response addComment(@PathParam("id") int id, Comment comment){

        Movie result = em.createNamedQuery("movie.id", Movie.class)
                .setParameter("movieId", id)
                .getSingleResult();

     if(result==null) {
         return Response.status(404).build();
     }

     result.getComments().add(comment);
     comment.setMovie(result);
     em.persist(comment);

     return Response.ok().build();
 }

 //Usuwanie komentarzy

    @DELETE
    @Path("/{movieId}/comments/{commentId}")
    public Response deleteComment (@PathParam("movieId") int movieId,
                                    @PathParam("commentId") int commentId){

        Movie movie = em.createNamedQuery("movie.id", Movie.class)
                .setParameter("movieId", movieId)
                .getSingleResult();

        if (movie == null){
            return Response.status(404).build();
        }


        Comment comment = em.createNamedQuery("comment.id", Comment.class)
                .setParameter("commentId", commentId)
                .getSingleResult();

        if (comment == null){
            return Response.status(404).build();
        }

        movie.getComments().remove(comment);
        em.remove(comment);

        return Response.ok().build();

    }
//Wyswietlenie aktorów filmu:

    @GET
    @Path("/{id}/actors")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Actor> getAllActorsByMovie(@PathParam("id") int id) {
        Movie result = em.createNamedQuery("movie.id", Movie.class)
                .setParameter("movieId", id)
                .getSingleResult();

        if(result == null) {
            return null;
        }

        return result.getActors();
    }


    //Dodanie oceny do filmu
    @POST
    @Path("/{id}/rate")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addRating(@PathParam("id") int id, Rating rating) {
        Movie result = em.createNamedQuery("movie.id", Movie.class)
                .setParameter("movieId", id)
                .getSingleResult();

        if(result == null) {
            return null;
        }

        result.getRates().add(rating);
        rating.setMovie(result);
        em.persist(rating);

        return Response.ok().build();
    }

    //Wyswietlenie oceny filmu

    @GET
    @Path("/{id}/rate")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Rating> getRate (@PathParam("id") int id) {

        Movie result = em.createNamedQuery("movie.id", Movie.class)
                .setParameter("movieId", id)
                .getSingleResult();

        if(result == null) {
            return null;
        }

        return result.getAverageRate();
    }


}




