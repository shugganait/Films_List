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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shug.filmslist.R;
import shug.filmslist.databinding.FragmentMainListBinding;
import shug.filmslist.remote.client.RetrofitClient;
import shug.filmslist.remote.model.movie_list.MovieList;
import shug.filmslist.remote.model.movie_list.MovieResult;
import shug.filmslist.remote.service.ApiService;
import shug.filmslist.ui.fragments.main.adapter.MovieAdapter;

public class MainListFragment extends Fragment {

    private FragmentMainListBinding binding;
    public static final String KEY_FOR_ID = "id_key";

    private ApiService apiService;
    private NavController navController;
    private MovieAdapter adapter;
    private ArrayList<MovieResult> movies = new ArrayList<>();

    // 🔹 Параметры пагинации
    private int currentPage = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;

    // 🔹 Поисковая строка (если пуста — показываем популярные)
    private String currentQuery = "";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMainListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        apiService = RetrofitClient.getClient().create(ApiService.class);
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        initRecyclerView();
        initListener();

        // 🔹 Восстанавливаем состояние
        movies.clear();
        if (currentQuery.isEmpty()) {
            fetchMovies(currentPage);
        } else {
            fetchMoviesByTitle(currentQuery, currentPage);
        }
    }


    // ---------------------------------------------------
    // 🔹 Инициализация RecyclerView + пагинация
    // ---------------------------------------------------
    private void initRecyclerView() {
        adapter = new MovieAdapter(movies, id -> {
            Bundle bundle = new Bundle();
            bundle.putLong(KEY_FOR_ID, id);
            navController.navigate(R.id.detailFragment, bundle);
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        binding.rvList.setLayoutManager(layoutManager);
        binding.rvList.setAdapter(adapter);

        // Пагинация
        binding.rvList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) { // только вниз
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                    if (!isLoading && !isLastPage) {
                        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount - 5) {
                            loadNextPage();
                        }
                    }
                }
            }
        });
    }

    // ---------------------------------------------------
    // 🔹 Слушатели для поиска
    // ---------------------------------------------------
    private void initListener() {
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.toString().isEmpty()) {
                    currentQuery = "";
                    resetPagination();
                    fetchMovies(currentPage);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        binding.etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String query = binding.etSearch.getText().toString().trim();
                if (!query.isEmpty()) {
                    currentQuery = query;
                    resetPagination();
                    fetchMoviesByTitle(currentQuery, currentPage);
                } else {
                    currentQuery = "";
                    resetPagination();
                    fetchMovies(currentPage);
                }
                return true;
            }
            return false;
        });
    }

    // ---------------------------------------------------
    // 🔹 Запрос популярных фильмов
    // ---------------------------------------------------
    private void fetchMovies(int page) {
        isLoading = true;
        Call<MovieList> call = apiService.getPopularMovies(ApiService.KEY, "ru-RU", page);

        call.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(@NonNull Call<MovieList> call, @NonNull Response<MovieList> response) {
                isLoading = false;
                if (response.isSuccessful() && response.body() != null) {
                    List<MovieResult> newMovies = response.body().getResults();
                    if (newMovies != null && !newMovies.isEmpty()) {
                        movies.addAll(newMovies);
                        adapter.notifyDataSetChanged();
                    } else {
                        isLastPage = true;
                    }
                } else {
                    Log.e("shug", "fetchMovies: RESPONSE FAIL");
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieList> call, @NonNull Throwable t) {
                isLoading = false;
                Log.e("shug", "fetchMovies: FAIL", t);
            }
        });
    }

    // ---------------------------------------------------
    // 🔹 Запрос фильмов по названию
    // ---------------------------------------------------
    private void fetchMoviesByTitle(String title, int page) {
        isLoading = true;
        Call<MovieList> call = apiService.searchMovies(ApiService.KEY, "ru-RU", title, page);

        call.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(@NonNull Call<MovieList> call, @NonNull Response<MovieList> response) {
                isLoading = false;
                if (response.isSuccessful() && response.body() != null) {
                    List<MovieResult> newMovies = response.body().getResults();
                    if (newMovies != null && !newMovies.isEmpty()) {
                        movies.addAll(newMovies);
                        adapter.notifyDataSetChanged();
                        binding.imgNothingFound.setVisibility(View.GONE);
                    } else {
                        binding.imgNothingFound.setVisibility(View.VISIBLE);
                        isLastPage = true;
                    }
                } else {
                    Log.e("shug", "fetchMoviesByTitle: RESPONSE FAIL");
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieList> call, @NonNull Throwable t) {
                isLoading = false;
                Log.e("shug", "fetchMoviesByTitle: FAIL", t);
            }
        });
    }

    // ---------------------------------------------------
    // 🔹 Загрузка следующей страницы
    // ---------------------------------------------------
    private void loadNextPage() {
        currentPage++;
        if (currentQuery.isEmpty()) {
            fetchMovies(currentPage);
        } else {
            fetchMoviesByTitle(currentQuery, currentPage);
        }
    }

    // ---------------------------------------------------
    // 🔹 Сброс пагинации при новом поиске
    // ---------------------------------------------------
    private void resetPagination() {
        movies.clear();
        adapter.notifyDataSetChanged();
        currentPage = 1;
        isLastPage = false;
        isLoading = false;
    }
}