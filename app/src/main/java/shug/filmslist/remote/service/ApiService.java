package shug.filmslist.remote.service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import shug.filmslist.remote.model.movieDetail.MovieDetail;
import shug.filmslist.remote.model.movieDetail.MovieImage;
import shug.filmslist.remote.model.movieList.MovieList;

public interface ApiService {

    String KEY = "a14e8b61a6mshbd58074203c3097p1cb74fjsn88e622472eab";

    @GET("/")
    @Headers({
            "X-RapidAPI-Key: " + KEY,
            "X-RapidAPI-Host: movies-tv-shows-database.p.rapidapi.com",
            "Type: get-upcoming-movies"
    })
    Call<MovieList> getMovies(
            @Query("page") String page
    );

    @GET("/")
    @Headers({
            "X-RapidAPI-Key: " + KEY,
            "X-RapidAPI-Host: movies-tv-shows-database.p.rapidapi.com",
            "Type: get-movies-by-title"
    })
    Call<MovieList> getMoviesByTitle(
            @Query("title") String title
    );

    @GET("/")
    @Headers({
            "X-RapidAPI-Key: " + KEY,
            "X-RapidAPI-Host: movies-tv-shows-database.p.rapidapi.com",
            "Type: get-movie-details"
    })
    Call<MovieDetail> getMovieById(
            @Query("movieid") String id
    );
    @GET("/")
    @Headers({
            "X-RapidAPI-Key: " + KEY,
            "X-RapidAPI-Host: movies-tv-shows-database.p.rapidapi.com",
            "Type: get-movies-images-by-imdb"
    })
    Call<MovieImage> getMovieImageById(
            @Query("movieid") String id
    );
}