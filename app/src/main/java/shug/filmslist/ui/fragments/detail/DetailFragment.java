package shug.filmslist.ui.fragments.detail;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
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
import shug.filmslist.remote.model.movie_detail.MovieDetail;
import shug.filmslist.remote.service.ApiService;
import shug.filmslist.ui.fragments.main.MainListFragment;

public class DetailFragment extends Fragment {

    private FragmentDetailBinding binding;
    private ApiService apiService;
    private NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        apiService = RetrofitClient.getClient().create(ApiService.class);

        Long movieId = null;
        if (getArguments() != null) {
            movieId = getArguments().getLong(MainListFragment.KEY_FOR_ID);
        }

        if (movieId != null) {
            fetchMovieDetail(movieId.intValue());
        }

        binding.btnBack.setOnClickListener(v -> navController.navigateUp());

        binding.btnOpen.setOnClickListener(v -> {
            String encodedName = Uri.encode(binding.tvTitle.getText().toString());
            // Формируем ссылку на поиск по названию на Кинопоиске
            String url = "https://www.kinopoisk.ru/index.php?kp_query=" + encodedName;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });
    }

    private void fetchMovieDetail(int movieId) {
        binding.progress.setVisibility(View.VISIBLE);
        Call<MovieDetail> call = apiService.getMovieDetails(movieId, ApiService.KEY, "ru-RU");

        call.enqueue(new Callback<MovieDetail>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<MovieDetail> call, @NonNull Response<MovieDetail> response) {
                binding.progress.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    MovieDetail data = response.body();

                    binding.tvTitle.setText(data.getTitle());
                    binding.tvRelease.setText(data.getReleaseDate() != null ? data.getReleaseDate() : "N/A");
                    binding.tvRating.setText(String.valueOf(data.getVoteAverage()));
                    binding.tvRuntime.setText(data.getRuntime() + " мин");
                    binding.tvDesc.setText(data.getOverview());

                    // Genres
                    if (data.getGenres() != null && !data.getGenres().isEmpty()) {
                        StringBuilder genres = new StringBuilder();
                        for (int i = 0; i < data.getGenres().size(); i++) {
                            genres.append(data.getGenres().get(i).getName());
                            if (i < data.getGenres().size() - 1) genres.append(", ");
                        }
                        binding.tvGenres.setText(genres.toString());
                    }

                    // Languages
                    if (data.getSpokenLanguages() != null && !data.getSpokenLanguages().isEmpty()) {
                        StringBuilder languages = new StringBuilder();
                        for (int i = 0; i < data.getSpokenLanguages().size(); i++) {
                            languages.append(data.getSpokenLanguages().get(i).getEnglishName());
                            if (i < data.getSpokenLanguages().size() - 1) languages.append(", ");
                        }
                        binding.tvLanguage.setText(languages.toString());
                    }

                    // Countries
                    if (data.getProductionCountries() != null && !data.getProductionCountries().isEmpty()) {
                        StringBuilder countries = new StringBuilder();
                        for (int i = 0; i < data.getProductionCountries().size(); i++) {
                            countries.append(data.getProductionCountries().get(i).getName());
                            if (i < data.getProductionCountries().size() - 1)
                                countries.append(", ");
                        }
                        binding.tvCountries.setText(countries.toString());
                    }

                    // Images
                    if (data.getPosterPath() != null)
                        Glide.with(binding.imgPoster).load(convertToHttps(data.getPosterPath())).into(binding.imgPoster);
                    if (data.getBackdropPath() != null)
                        Glide.with(binding.imgFanart).load(convertToHttps(data.getBackdropPath())).into(binding.imgFanart);

                    binding.containerMain.setVisibility(View.VISIBLE);
                } else {
                    Log.e("DetailFragment", "onFailure: DATAFAIL");
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieDetail> call, @NonNull Throwable t) {
                binding.progress.setVisibility(View.GONE);
                Log.e("DetailFragment", "onFailure: " + t.getMessage());
            }
        });
    }

    private String convertToHttps(String url) {
        if (url != null && url.startsWith("http://")) {
            return "https://" + url.substring("http://".length());
        } else {
            return "https://image.tmdb.org/t/p/w500" + url;
        }
    }
}
