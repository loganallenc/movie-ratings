package views;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.List;

import models.MovieModel;
import models.RatingsModel;
import otto.BusSingleton;
import services.APIServiceInterface;
import services.RatingService;


/**
 * Created by logan on 2/27/16.
 */
public class MovieListAdapter extends ArrayAdapter<MovieModel> {
    /**
     * APIServiceInterface
     */
    private APIServiceInterface ratingService;
    /**
     * List of MovieModels
     */
    private List<MovieModel> movieModels;

    /**
     * Initialize UI and initialize bus.
     * @param c c
     * @param items items
     */

    public MovieListAdapter(Context c, List<MovieModel> items) {
        super(c, 0, items);
        ratingService = RatingService.getService();
        movieModels = items;

        final Bus bus = BusSingleton.get();
        // subscribe to new events!
        bus.register(this);
        for (MovieModel movie: items) {
            RatingService.getRatings(ratingService, movie.title);
        }
    }

    /**
     * Receive asynchronous event with new ratings.
     * @param ratings ratings
     */
    @Subscribe
    public final void getRatingsEvent(RatingsModel ratings) {
        // bad way of doing this, O(n^2). Fuck it
        MovieModel movie;
        for (int x = 0; x < this.getCount(); x++) {
            movie = this.getItem(x);
            if (movie.title.equals(ratings.movieTitle)) {
                movie.ratings = ratings.ratings;
                this.notifyDataSetChanged();
                x = this.getCount();
            }
        }

        // modify the list items individually based on events
    }

    /**
     * Get count override function
     * @return size of list
     */
    @Override
    public final int getCount() {
        if (movieModels == null) {
            return 0;
        }
        return movieModels.size();
    }

    /**
     * Get item override
     * @param position position
     * @return item
     */
    @Override
    public final MovieModel getItem(int position) {
        return movieModels == null? null : movieModels.get(position);
    }

    /**
     * Get view override that sets up item view.
     * @param position position
     * @param convertView convertView
     * @param parent parent
     * @return view
     */
    @Override
    public final View getView(int position, View convertView, ViewGroup parent) {
        MovieListItemView itemView = (MovieListItemView)convertView;
        if (null == itemView) { itemView = MovieListItemView.inflate(parent); }
        itemView.setMovie(getItem(position));
        itemView.setRatings(getItem(position).ratings);
        return itemView;
    }

}