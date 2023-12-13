
package shug.filmslist.remote.model.movieList;


import java.util.List;

public class MovieList {

    private List<MovieResult> movie_results;
    private Long results;
    private String status;

    public List<MovieResult> getMovieResults() {
        return movie_results;
    }

    public void setMovieResults(List<MovieResult> movieResults) {
        movieResults = movieResults;
    }

    public Long getResults() {
        return results;
    }

    public void setResults(Long results) {
        results = results;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        status = status;
    }
}
