package shug.filmslist.ui.fragments.detail;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shug.filmslist.R;
import shug.filmslist.databinding.FragmentDetailBinding;
import shug.filmslist.remote.client.RetrofitClient;
import shug.filmslist.remote.model.movieDetail.MovieDetail;
import shug.filmslist.remote.model.movieDetail.MovieImage;
import shug.filmslist.remote.service.ApiService;
import shug.filmslist.ui.fragments.main.MainListFragment;

public class DetailFragment extends Fragment {

    private FragmentDetailBinding binding;
    ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
    NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDetailBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //initView
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        //getBundle
        String id = "";
        if (getArguments() != null) {
            id = getArguments().getString(MainListFragment.KEY_FOR_ID);
        }
        fetchMovieDetailAndImg(id);
        //navigation
        binding.btnBack.setOnClickListener(v -> navController.navigateUp());
    }

    private void fetchMovieDetailAndImg(String id) {
        Call<MovieDetail> call = apiService.getMovieById(id);
        binding.progress.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<MovieDetail>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<MovieDetail> call, @NonNull Response<MovieDetail> response) {
                if (response.isSuccessful()) {
                    MovieDetail data;
                    if (response.body() != null) {
                        data = response.body();
                        binding.tvTitle.setText(data.getTitle());
                        binding.tvYear.setText(data.getYear());
                        binding.tvRelease.setText(data.getRelease_date());
                        binding.tvRating.setText(data.getImdbRating());
                        if (data.getRuntime() != null) {
                            binding.tvRuntime.setText(data.getRuntime().toString() + " minute");
                        }
                        if (data.getGenres() != null && !data.getGenres().isEmpty()) {
                            binding.tvGenres.setText(data.getGenres().toString());
                        }
                        if (data.getCountries() != null && !data.getCountries().isEmpty()) {
                            binding.tvCountries.setText(data.getCountries().toString());
                        }
                        if (data.getLanguage() != null && !data.getLanguage().isEmpty()) {
                            binding.tvLanguage.setText(data.getLanguage().toString());
                        }
                        binding.tvDesc.setText(data.getDescription());
                        binding.containerMain.setVisibility(View.VISIBLE);
                        binding.progress.setVisibility(View.GONE);
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
                        Glide.with(binding.imgPoster).load(convertToHttps(data.getPoster())).into(binding.imgPoster);
                        Glide.with(binding.imgFanart).load(convertToHttps(data.getFanart())).into(binding.imgFanart);
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