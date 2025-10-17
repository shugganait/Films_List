package shug.filmslist.remote.service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import shug.filmslist.remote.model.movie_detail.MovieDetail;
import shug.filmslist.remote.model.movie_list.MovieList;

public interface ApiService {

    String KEY = "958ffd81397873f1e8ac45610cf22299";

    @GET("movie/popular")
    Call<MovieList> getPopularMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("search/movie")
    Call<MovieList> searchMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("query") String query,
            @Query("page") int page
    );


    @GET("movie/{movie_id}")
    Call<MovieDetail> getMovieDetails(
            @Path("movie_id") int movieId,
            @Query("api_key") String apiKey,
            @Query("language") String language
    );
}