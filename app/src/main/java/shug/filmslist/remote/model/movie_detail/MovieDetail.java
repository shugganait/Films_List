package shug.filmslist.remote.model.movie_detail;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class MovieDetail {

    @SerializedName("adult")
    private boolean adult;

    @SerializedName("backdrop_path")
    private String backdropPath;

    @SerializedName("belongs_to_collection")
    private BelongsToCollection belongsToCollection;

    @SerializedName("budget")
    private long budget;

    @SerializedName("genres")
    private List<Genre> genres;

    @SerializedName("homepage")
    private String homepage;

    @SerializedName("id")
    private long id;

    @SerializedName("imdb_id")
    private String imdbid;

    @SerializedName("origin_country")
    private List<String> originCountry;

    @SerializedName("original_language")
    private String originalLanguage;

    @SerializedName("original_title")
    private String originalTitle;

    @SerializedName("overview")
    private String overview;

    @SerializedName("popularity")
    private double popularity;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("production_companies")
    private List<ProductionCompany> productionCompanies;

    @SerializedName("production_countries")
    private List<ProductionCountry> productionCountries;

    @SerializedName("release_date")
    private String releaseDate;

    @SerializedName("revenue")
    private long revenue;

    @SerializedName("runtime")
    private long runtime;

    @SerializedName("spoken_languages")
    private List<SpokenLanguage> spokenLanguages;

    @SerializedName("status")
    private String status;

    @SerializedName("tagline")
    private String tagline;

    @SerializedName("title")
    private String title;

    @SerializedName("video")
    private boolean video;

    @SerializedName("vote_average")
    private double voteAverage;

    @SerializedName("vote_count")
    private long voteCount;

    // --- Геттеры ---
    public boolean isAdult() { return adult; }
    public String getBackdropPath() { return backdropPath; }
    public BelongsToCollection getBelongsToCollection() { return belongsToCollection; }
    public long getBudget() { return budget; }
    public List<Genre> getGenres() { return genres; }
    public String getHomepage() { return homepage; }
    public long getId() { return id; }
    public String getImdbid() { return imdbid; }
    public List<String> getOriginCountry() { return originCountry; }
    public String getOriginalLanguage() { return originalLanguage; }
    public String getOriginalTitle() { return originalTitle; }
    public String getOverview() { return overview; }
    public double getPopularity() { return popularity; }
    public String getPosterPath() { return posterPath; }
    public List<ProductionCompany> getProductionCompanies() { return productionCompanies; }
    public List<ProductionCountry> getProductionCountries() { return productionCountries; }
    public String getReleaseDate() { return releaseDate; }
    public long getRevenue() { return revenue; }
    public long getRuntime() { return runtime; }
    public List<SpokenLanguage> getSpokenLanguages() { return spokenLanguages; }
    public String getStatus() { return status; }
    public String getTagline() { return tagline; }
    public String getTitle() { return title; }
    public boolean isVideo() { return video; }
    public double getVoteAverage() { return voteAverage; }
    public long getVoteCount() { return voteCount; }

    // --- Вложенные классы ---
    public static class BelongsToCollection {
        @SerializedName("backdrop_path")
        private String backdropPath;
        @SerializedName("name")
        private String name;
        @SerializedName("id")
        private long id;
        @SerializedName("poster_path")
        private String posterPath;

        public String getBackdropPath() { return backdropPath; }
        public String getName() { return name; }
        public long getId() { return id; }
        public String getPosterPath() { return posterPath; }
    }

    public static class Genre {
        @SerializedName("id")
        private long id;
        @SerializedName("name")
        private String name;

        public long getId() { return id; }
        public String getName() { return name; }
    }

    public static class ProductionCompany {
        @SerializedName("id")
        private long id;
        @SerializedName("logo_path")
        private String logoPath;
        @SerializedName("name")
        private String name;
        @SerializedName("origin_country")
        private String originCountry;

        public long getId() { return id; }
        public String getLogoPath() { return logoPath; }
        public String getName() { return name; }
        public String getOriginCountry() { return originCountry; }
    }

    public static class ProductionCountry {
        @SerializedName("iso_3166_1")
        private String iso31661;
        @SerializedName("name")
        private String name;

        public String getIso31661() { return iso31661; }
        public String getName() { return name; }
    }

    public static class SpokenLanguage {
        @SerializedName("english_name")
        private String englishName;
        @SerializedName("iso_639_1")
        private String iso6391;
        @SerializedName("name")
        private String name;

        public String getEnglishName() { return englishName; }
        public String getIso6391() { return iso6391; }
        public String getName() { return name; }
    }
}
