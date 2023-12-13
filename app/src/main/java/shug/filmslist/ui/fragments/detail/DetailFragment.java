package shug.filmslist.ui.fragments.detail;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shug.filmslist.R;
import shug.filmslist.remote.client.RetrofitClient;
import shug.filmslist.remote.model.movieDetail.MovieDetail;
import shug.filmslist.remote.model.movieDetail.MovieImage;
import shug.filmslist.remote.service.ApiService;
import shug.filmslist.ui.fragments.main.MainListFragment;

public class DetailFragment extends Fragment {

    ImageView imgPoster;
    ImageView imgFanart;
    TextView tvTitle;
    TextView tvYear;
    TextView tvRelease;
    TextView tvRating;
    TextView tvRuntitme;
    TextView tvGenres;
    TextView tvCountries;
    TextView tvLanguage;
    TextView tvDesc;
    NestedScrollView container;
    ProgressBar progress;
    ImageView btnBack;
    ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
    NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //initView
        imgPoster = view.findViewById(R.id.img_poster);
        tvTitle = view.findViewById(R.id.tv_title);
        tvYear = view.findViewById(R.id.year);
        tvRelease = view.findViewById(R.id.release);
        tvRating = view.findViewById(R.id.rating);
        tvRuntitme = view.findViewById(R.id.runtime);
        tvGenres = view.findViewById(R.id.genres);
        tvCountries = view.findViewById(R.id.countries);
        tvLanguage = view.findViewById(R.id.language);
        tvDesc = view.findViewById(R.id.tv_desc);
        imgFanart = view.findViewById(R.id.img_fanart);
        container = view.findViewById(R.id.container_main);
        progress = view.findViewById(R.id.progress);
        btnBack = view.findViewById(R.id.btn_back);
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        //getBundle
        String id = "";
        if (getArguments() != null) {
            id = getArguments().getString(MainListFragment.KEY_FOR_ID);
        }
        fetchMovieDetailAndImg(id);
        //navigation
        btnBack.setOnClickListener(view1 -> {
            navController.navigateUp();
        });
    }

    private void fetchMovieDetailAndImg(String id) {
        Call<MovieDetail> call = apiService.getMovieById(id);
        call.enqueue(new Callback<MovieDetail>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<MovieDetail> call, @NonNull Response<MovieDetail> response) {
                if (response.isSuccessful()) {
                    MovieDetail data;
                    if (response.body() != null) {
                        data = response.body();
                        tvTitle.setText(data.getTitle());
                        tvYear.setText(data.getYear());
                        tvRelease.setText(data.getRelease_date());
                        tvRating.setText(data.getImdbRating());
                        if (data.getRuntime() != null) {
                            tvRuntitme.setText(data.getRuntime().toString() + " minute");
                        }
                        if (data.getGenres() != null && !data.getGenres().isEmpty()) {
                            tvGenres.setText(data.getGenres().toString());
                        }
                        if (data.getCountries() != null && !data.getCountries().isEmpty()) {
                            tvCountries.setText(data.getCountries().toString());
                        }
                        if (data.getLanguage() != null && !data.getLanguage().isEmpty()) {
                            tvLanguage.setText(data.getLanguage().toString());
                        }
                        tvDesc.setText(data.getDescription());
                        container.setVisibility(View.VISIBLE);
                        progress.setVisibility(View.GONE);
                    }
                } else {
                    Log.e("shug", "onFailure: DATAFAIL");
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieDetail> call, @NonNull Throwable t) {
                Log.e("shug", "onFailure: FAIL");
            }
        });

        //image
        Call<MovieImage> callImg = apiService.getMovieImageById(id);
        callImg.enqueue(new Callback<MovieImage>() {
            @Override
            public void onResponse(@NonNull Call<MovieImage> call, @NonNull Response<MovieImage> response) {
                if (response.isSuccessful()) {
                    MovieImage data;
                    if (response.body() != null) {
                        data = response.body();
                        Glide.with(imgPoster).load(convertToHttps(data.getPoster())).into(imgPoster);
                        Glide.with(imgFanart).load(convertToHttps(data.getFanart())).into(imgFanart);
                    }
                } else {
                    Log.e("shug", "onFailure: DATAFAIL");
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieImage> call, @NonNull Throwable t) {
                Log.e("shug", "onFailure: FAIL");
            }
        });
    }

    private String convertToHttps(String url) {
        if (url != null && url.startsWith("http://")) {
            return "https://" + url.substring("http://".length());
        } else {
            return url;
        }
    }
}