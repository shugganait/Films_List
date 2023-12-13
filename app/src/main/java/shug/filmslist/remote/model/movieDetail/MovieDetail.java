
package shug.filmslist.remote.model.movieDetail;

import java.util.List;

public class MovieDetail {

    private List<String> countries;
    private String release_date;
    private String description;
    private List<String> genres;
    private String imdb_rating;
    private List<String> language;
    private String popularity;
    private String rated;
    private Long runtime;
    private String status;
    private String title;
    private String vote_count;
    private String year;

    public List<String> getCountries() {
        return countries;
    }

    public void setCountries(List<String> countries) {
        countries = countries;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        description = description;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        genres = genres;
    }

    public String getImdbRating() {
        return imdb_rating;
    }

    public void setImdbRating(String imdbRating) {
        imdbRating = imdbRating;
    }

    public List<String> getLanguage() {
        return language;
    }

    public void setLanguage(List<String> language) {
        language = language;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        popularity = popularity;
    }

    public String getRated() {
        return rated;
    }

    public void setRated(String rated) {
        rated = rated;
    }

    public Long getRuntime() {
        return runtime;
    }

    public void setRuntime(Long runtime) {
        runtime = runtime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        title = title;
    }

    public String getVoteCount() {
        return vote_count;
    }

    public void setVoteCount(String voteCount) {
        voteCount = voteCount;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        year = year;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }
}
