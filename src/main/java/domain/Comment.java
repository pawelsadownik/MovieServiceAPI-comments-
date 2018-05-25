package domain;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
@Entity
@NamedQueries({
        @NamedQuery(name = "comment.id", query = "FROM Comment c WHERE c.id=:commentId")
})
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String content;

    @ManyToOne
    private Movie movie;

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }




}
