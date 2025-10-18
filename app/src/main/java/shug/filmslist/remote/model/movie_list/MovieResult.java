package shug.filmslist.remote.model.movie_list;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieResult {
    private String overview;
    private String originalLanguage;
    private String originalTitle;
    private boolean video;
    private String title;
    private List<Long> genreids;
    @SerializedName("poster_path")
    private String posterPath;
    private String backdropPath;
    @SerializedName("release_date")
    private String releaseDate;
    private double popularity;
    @SerializedName("vote_average")
    private double voteAverage;
    private long id;
    private boolean adult;
    private long voteCount;

    public String getOverview() {
        return overview;
    }

    public void setOverview(String value) {
        this.overview = value;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String value) {
        this.originalLanguage = value;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String value) {
        this.originalTitle = value;
    }

    public boolean getVideo() {
        return video;
    }

    public void setVideo(boolean value) {
        this.video = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String value) {
        this.title = value;
    }

    public List<Long> getGenreids() {
        return genreids;
    }

    public void setGenreids(List<Long> value) {
        this.genreids = value;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String value) {
        this.posterPath = value;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String value) {
        this.backdropPath = value;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String value) {
        this.releaseDate = value;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double value) {
        this.popularity = value;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double value) {
        this.voteAverage = value;
    }

    public long getid() {
        return id;
    }

    public void setid(long value) {
        this.id = value;
    }

    public boolean getAdult() {
        return adult;
    }

    public void setAdult(boolean value) {
        this.adult = value;
    }

    public long getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(long value) {
        this.voteCount = value;
    }
}
