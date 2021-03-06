package services;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.otto.Bus;

import models.ErrorModel;
import models.RatingModel;
import models.RatingsModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by logan on 2/27/16.
 */
public final class RatingService {

    // we use this to publish changes to other objects
    /**
     * Bus
     */
    protected static Bus bus;
    /**
     * APIServiceInterface
     */
    protected static APIServiceInterface rService = null;
    /**
     * baseUrl
     */
    protected static String baseUrl = "http://10.0.2.2:10010/api/"; // access the host computer. this expects the server to be running!

    /**
     * initialize bus should occur before any of the other methods are called
     * initBus occurs in App, only needs to happen once
     * @param newBus newBus
     */
    public static void initBus(Bus newBus) {
        bus = newBus;
    }

    /**
     * createService creates a Retrofit service for interacting with the team's REST API
     * @return APIServiceInterface
     */
    public static APIServiceInterface getService() {
        if (rService == null) {
            final Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(JacksonConverterFactory.create())
                    .build();

            rService = retrofit.create(APIServiceInterface.class);
        }

        return rService;
    }

    /**
     * Empty constructor
     */
    private RatingService() {}

    /**
     * errorConverter is a private helper function for converting the response body from a server call
     * to an error model when an error is received
     * @param errorBody errorBody
     * @return ErrorModel
     */
    private static ErrorModel errorConverter(ResponseBody errorBody) {
        final ObjectMapper om = new ObjectMapper();
        try {
            return om.readValue(errorBody.string(), ErrorModel.class);
        } catch (Exception e) {
            Log.d("errorConverting", e.toString());
        }

        return new ErrorModel("Incorrect error format returned.");
    }

    /**
     * Send in a generated service along with a valid RatingModel, perform a server call to create a movie rating
     * @param service service
     * @param ratingModel ratingModel
     */
    public static void createRating(APIServiceInterface service, RatingModel ratingModel) {
        service.createRating(ratingModel).enqueue(
                new Callback<RatingModel>() {
                    @Override
                    public void onResponse(Call<RatingModel> call, Response<RatingModel> response) {
                        if (response.isSuccess()) {
                            bus.post(response.body());
                        } else {
                            final ErrorModel em = errorConverter(response.errorBody());
                            bus.post(em);
                        }
                    }

                    @Override
                    public void onFailure(Call<RatingModel> call, Throwable t) {
                    }
                }
        );
    }

    /**
     * Send in a generated service along with a movie title, perform a server call to get ratings for that movie
     * @param service service
     * @param movieTitle movieTitle
     */
    public static void getRatings(APIServiceInterface service, String movieTitle) {
        service.getRatings(movieTitle).enqueue(
                new Callback<RatingsModel>() {
                    @Override
                    public void onResponse(Call<RatingsModel> call, Response<RatingsModel> response) {
                        if (response.isSuccess()) {
                            bus.post(response.body());
                        } else {
                            final ErrorModel em = errorConverter(response.errorBody());
                            bus.post(em);
                        }
                    }

                    @Override
                    public void onFailure(Call<RatingsModel> call, Throwable t) {
                    }
                }
        );
    }
}
