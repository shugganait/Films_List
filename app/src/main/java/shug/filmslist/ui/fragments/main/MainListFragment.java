package shug.filmslist.ui.fragments.main;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shug.filmslist.R;
import shug.filmslist.databinding.FragmentMainListBinding;
import shug.filmslist.remote.client.RetrofitClient;
import shug.filmslist.remote.model.movieList.MovieList;
import shug.filmslist.remote.model.movieList.MovieResult;
import shug.filmslist.remote.service.ApiService;
import shug.filmslist.ui.fragments.main.adapter.MovieAdapter;

public class MainListFragment extends Fragment {

    private FragmentMainListBinding binding;
    public static final String KEY_FOR_ID = "id_key";
    ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
    NavController navController;
    MovieAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMainListBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //initView
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        //fetch
        fetchMovies();
        //listener
        initListener();
    }

    private void initListener() {
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (binding.etSearch.getText().toString().isEmpty()) {
                    fetchMovies();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        binding.etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (!binding.etSearch.getText().toString().isEmpty()) {
                    fetchMoviesByTitle(binding.etSearch.getText().toString());
                } else {
                    fetchMovies();
                }
                return true;  // Возвращает true, чтобы сигнализировать, что событие обработано
            }
            return false;  // Возвращает false для других событий
        });
    }

    private void initAdapter(ArrayList<MovieResult> data) {
        adapter = new MovieAdapter(data, id -> {
            Bundle bundle = new Bundle();
            bundle.putString(KEY_FOR_ID, id);
            navController.navigate(R.id.detailFragment, bundle);
        });
        binding.rvList.setAdapter(adapter);
    }

    private void fetchMovies() {
        ArrayList<MovieResult> result = new ArrayList<>();
        Call<MovieList> call = apiService.getMovies(randomPage());
        call.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(@NonNull Call<MovieList> call, @NonNull Response<MovieList> response) {
                if (response.isSuccessful()) {
                    List<MovieResult> data = null;
                    if (response.body() != null) {
                        data = response.body().getMovieResults();
                    }
                    if (data != null && !data.isEmpty()) {
                        result.addAll(data);
                        initAdapter(result);
                    }
                } else {
                    Log.e("shug", "onFailure: DATAFAIL");
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieList> call, @NonNull Throwable t) {
                Log.e("shug", "onFailure: FAIL");
            }
        });
    }

    private void fetchMoviesByTitle(String title) {
        ArrayList<MovieResult> result = new ArrayList<>();
        Call<MovieList> call = apiService.getMoviesByTitle(title);
        call.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(@NonNull Call<MovieList> call, @NonNull Response<MovieList> response) {
                if (response.isSuccessful()) {
                    List<MovieResult> data = null;
                    if (response.body() != null) {
                        data = response.body().getMovieResults();
                    }
                    if (data != null && !data.isEmpty()) {
                        result.addAll(data);
                        initAdapter(result);
                    }
                } else {
                    Log.e("shug", "onFailure: DATAFAIL");
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieList> call, @NonNull Throwable t) {
                Log.e("shug", "onFailure: FAIL");
            }
        });
    }

    public String randomPage() {
        Random random = new Random();
        int randomNumber = random.nextInt(11) + 1;
        return Integer.toString(randomNumber);
    }
}