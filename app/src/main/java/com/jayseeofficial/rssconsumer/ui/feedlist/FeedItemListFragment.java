package com.jayseeofficial.rssconsumer.ui.feedlist;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.jayseeofficial.rssconsumer.rest.Feed;
import com.jayseeofficial.rssconsumer.rest.FeedApi;
import com.jayseeofficial.rssconsumer.rest.FeedArray;
import com.jayseeofficial.rssconsumer.rest.ServerError;
import com.jayseeofficial.rssconsumer.rest.ServerResult;

import java.util.List;

public class FeedItemListFragment extends ListFragment {

    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    private Callbacks mCallbacks = sDummyCallbacks;

    private int mActivatedPosition = ListView.INVALID_POSITION;

    public interface Callbacks {
        public void onItemSelected(String id);
    }

    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(String id) {
        }
    };

    private FeedApi.FeedRequestListener listener = new FeedApi.FeedRequestListener() {
        @Override
        public void onRequestCompleted(FeedApi.Request requestCode, ServerResult result) {
            Log.d(getClass().getSimpleName(), "onRequestCompleted " + requestCode.name());
            if (requestCode == FeedApi.Request.GET_ALL_FEEDS) {
                List<Feed> feeds = ((FeedArray) result).getFeeds();
                setListAdapter(new FeedListAdapter(getActivity(), feeds));
            }
        }

        @Override
        public void onRequestError(FeedApi.Request request, ServerError error) {
            Toast.makeText(getActivity(), error.getErrorMessage(), Toast.LENGTH_LONG).show();
        }
    };

    public FeedItemListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Restore the previously serialized activated item position.
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        FeedApi.getInstance().removeListener(listener);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }
        mCallbacks = (Callbacks) activity;
        FeedApi.getInstance().registerListener(listener);
        FeedApi.getInstance().requestAllFeeds();
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Reset the active callbacks interface to the dummy implementation.
        mCallbacks = sDummyCallbacks;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.
        mCallbacks.onItemSelected(((Feed) getListAdapter().getItem(position)).getId() + "");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        getListView().setChoiceMode(activateOnItemClick
                ? ListView.CHOICE_MODE_SINGLE
                : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }
}
