package services;

import com.squareup.otto.Bus;

import models.MovieListModel;
import models.MovieTitlesModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by logan on 2/21/16.
 */
public final class MovieService {

    /**
     * API_BASE_URL
     */
    private static final String API_BASE_URL = "http://www.omdbapi.com/";
    /**
     * RottenTomatoesInterface
     */
    private static RottenTomatoesInterface rService = null;
    /**
     * Bus
     */
    protected static Bus bus;

    /**
     * initialize bus should occur before any of the other methods are called
     * initBus occurs in App, only needs to happen once
     * @param newBus newBus
     */
    public static void initBus(Bus newBus) {
        bus = newBus;
    }

    /**
     * Getter for RottenTomatoesInterface
     * @return service
     */
    public static RottenTomatoesInterface getService() {
        if (rService == null) {
            final Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(JacksonConverterFactory.create())
                    .build();

            rService = retrofit.create(RottenTomatoesInterface.class);
            return rService;
        } else {
            return rService;
        }
    }

    /**
     * Empty constructor
     */
    private MovieService() {}

    /**
     * Send in a generated service along with a valid UserModel, perform a server call
     *
     * @param service service
     * @param searchTitle searchTitle
     */
    public static void searchMovies(RottenTomatoesInterface service, String searchTitle) {
        service.getSearch(searchTitle).enqueue(
                new Callback<MovieListModel>() {
                    @Override
                    public void onResponse(Call<MovieListModel> call, Response<MovieListModel> response) {
                        if (response.isSuccess()) {
                            bus.post(response.body());
                        }
                    }
                    @Override
                    public void onFailure(Call<MovieListModel> call, Throwable t) {
                    }
                }
        );
    }

    /**
     * Searches movies by title
     * @param service service
     * @param filterBy filterBy
     * @param other other
     */
    public static void searchMovieTitlesToQuery(APIServiceInterface service, String filterBy, String other) {
        service.searchMovieTitlesToQuery(filterBy, other).enqueue(
                new Callback<MovieTitlesModel>() {
                    @Override
                    public void onResponse(Call<MovieTitlesModel> call, Response<MovieTitlesModel> response) {
                        if (response.isSuccess()) {
                            bus.post(response.body());
                        }
                    }
                    @Override
                    public void onFailure(Call<MovieTitlesModel> call, Throwable t) {
                    }
                });
    }
}
