package domain;



import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.List;


@XmlRootElement
@Entity
@NamedQueries({
        @NamedQuery(name = "movie.all" , query = "SELECT m FROM Movie m"),
        @NamedQuery(name = "movie.id", query = "FROM Movie m WHERE m.id=:movieId"),

})
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @ManyToMany(mappedBy = "movies")
    private List <Actor> actors;

    @OneToMany(mappedBy = "movie")
    private List<Comment> comments;

    @XmlTransient
    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlTransient
    public List<Actor> getActors() {
        return actors;
    }

    public void setActors(List<Actor> actors) {
        this.actors = actors;
    }


    @OneToMany(mappedBy = "movie")
    private List <Rating> rates;

    @XmlTransient
    public List<Rating> getRates() {

        return rates;
    }

    public List<Rating> getAverageRate() {


        int avRate = (int) rates.stream().mapToInt(a -> a.getValue()).average().orElse(0);

        List <Rating> avRateList = new ArrayList<>();

       Rating av = new Rating();

       av.setId(0);

       av.setValue(avRate);

       avRateList.add(av);

        return avRateList;
    }

    public void setRates(List<Rating> rates) {
        this.rates = rates;
    }

}
