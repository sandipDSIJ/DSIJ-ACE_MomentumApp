package in.dsij.acemomentum.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import in.dsij.acemomentum.G;
import in.dsij.acemomentum.R;
import in.dsij.acemomentum.db.DbTrackerColumn;
import in.dsij.acemomentum.db.DbTrackerRow;
import in.dsij.acemomentum.net.CallGenerator;
import in.dsij.acemomentum.net.res.ResGetTrackerCol;
import in.dsij.acemomentum.net.res.ResMessage;
import in.dsij.acemomentum.view.MyTextView;
import io.realm.Realm;
import io.realm.RealmList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrackerFragment extends Fragment {

    private static final String LOG_TAG = "TrackerFragment";
    private static final String ARG_TRACKER_COLUMN_ID = "TrackerFragment.Arg.ARG_TRACKER_COLUMN_ID";

    private Realm realm;
    private RealmList<DbTrackerRow> mDbTrackerRows;
    private DbTrackerColumn mDbTrackerColumn;

    private RecyclerView mRecyclerView;
    private FrameLayout flEmptyView;
    private MyTextView tvError;
    private LinearLayout llTrackerView;

    public static TrackerFragment newInstance(long columnId) {

        Bundle args = new Bundle();
        args.putLong(ARG_TRACKER_COLUMN_ID, columnId);
        TrackerFragment fragment = new TrackerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public TrackerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        long columnId = getArguments().getLong(ARG_TRACKER_COLUMN_ID);
        mDbTrackerColumn = realm.where(DbTrackerColumn.class).equalTo(DbTrackerColumn.COLUMN_ID, columnId).findFirst();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_tracker, container, false);

        findViews(rootView);

        setViews();

        return rootView;
    }

    private void setViews() {
        //Nothing to do
    }

    private void findViews(View rootView) {
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rvTracker);
        flEmptyView = (FrameLayout) rootView.findViewById(R.id.flEmptyView);
        tvError = (MyTextView) rootView.findViewById(R.id.tvError);
        llTrackerView = (LinearLayout) rootView.findViewById(R.id.llTrackerView);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


//        mDbTrackerRows = realm.where(DbTrackerRow.class).findAllSorted(DbTrackerRow.RECOMMENDATION_DATE, Sort.DESCENDING);
        try {
            mDbTrackerRows = mDbTrackerColumn.getTrackerCalls();

            if (mDbTrackerRows.isEmpty()) {
                showEmptyView(true, "Not available.");
            } else {
                setUpRecyclerView();
            }

        } catch (Exception e) {

        }
//        getTrackerRows();

    }

    private void getTrackerRows() {

        CallGenerator.getTrackerData().enqueue(new Callback<List<ResGetTrackerCol>>() {
            @Override
            public void onResponse(Call<List<ResGetTrackerCol>> call, Response<List<ResGetTrackerCol>> response) {
                if (response.isSuccessful()) {
                    try {
                        List<ResGetTrackerCol> resGetTrackerCols = response.body();

                        Log.v(LOG_TAG, G.net.getCalls.TAG +
                                G.tag.RESPONSE +
                                new GsonBuilder()
                                        .setPrettyPrinting()
                                        .create()
                                        .toJson(resGetTrackerCols));

                        if (resGetTrackerCols == null) {
                            Log.w(LOG_TAG, G.net.getCalls.TAG +
                                    G.tag.RESPONSE + "Received NULL Call List");

                            showEmptyView(true, "Not available at the moment");

                        } else {
                            insertTrackerCols(resGetTrackerCols);
                        }

                    } catch (Exception e) {
                        Log.w(LOG_TAG, G.net.getCalls.TAG + G.tag.RESPONSE + "Parse response Error: " + response.body(), e);
                    }
                } else {

                    showEmptyView(true, "Not available at the moment");

                    ResMessage errorMessage = null;

                    try {
                        String errorRes = response.errorBody().string();

                        errorMessage = new GsonBuilder().create().fromJson(errorRes, ResMessage.class);

                    } catch (IOException e) {
                        //
                    }

                    switch (response.code()) {
                        /*case G.net.loginWithPassword.error.INTERNAL_SERVER_ERROR:

                            Snackbar.make(tvBtnSignIn, "!Server ran into trouble.", Snackbar.LENGTH_LONG)
                                    .setAction("Retry", retryClickListener).show();

                            break;
                        case G.net.loginWithPassword.error.INCORRECT_PASSWORD:
                            tilPassword.setError("! Incorrect Password");
                            break;
                        case G.net.loginWithPassword.error.USER_LOCKED:


                            // TODO: 9/13/2017 Show Dialog
                            break;
                        case G.net.loginWithPassword.error.NOT_AUTHORISED:
                            // TODO: 9/13/2017 Not Sure About This
                            break;
                        case G.net.loginWithPassword.error.SERVER_ERROR:
                            Snackbar.make(tvBtnSignIn, "!Server ran into trouble.", Snackbar.LENGTH_LONG)
                                    .setAction("Retry", retryClickListener).show();
                            break;
                        case G.net.loginWithPassword.error.USERNAME_NOT_REGISTERED:
                            tilUsername.setError("! Username not Registered");
                            break;
                        case G.net.loginWithPassword.error.EMPTY_PARAMS:
                            Snackbar.make(tvBtnSignIn, "!App ran into trouble.", Snackbar.LENGTH_LONG).show();

                            // TODO: 9/13/2017 Reload Fragment

                            break;*/
                        default:
                            // TODO: 10/10/2017
                            break;
                    }
                    Log.w(LOG_TAG, G.net.getTrackerData.TAG + G.tag.ERROR + "Error Response code : " + response.code() + " Response Body:\n" +
                            new GsonBuilder()
                                    .setPrettyPrinting()
                                    .create()
                                    .toJson(errorMessage));
                }
            }

            @Override
            public void onFailure(Call<List<ResGetTrackerCol>> call, Throwable t) {
                Log.e(LOG_TAG, G.net.getTrackerData.TAG + G.tag.FAILED + "Failed API Call : ", t);
                showEmptyView(true, "Not available at the moment");
            }
        });

    }

    private void insertTrackerCols(final List<ResGetTrackerCol> resGetTrackerCols) {

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Log.d(LOG_TAG,
                        G.tag.DB_TRANSACTION_WRITE + "Tracker Data");
                realm.createOrUpdateAllFromJson(DbTrackerColumn.class, new Gson().toJson(resGetTrackerCols));
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.d(LOG_TAG,
                        G.tag.DB_TRANSACTION_WRITE + G.tag.DB_SUCCESS);

                setUpRecyclerView();

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.e(LOG_TAG,
                        G.tag.DB_TRANSACTION_WRITE + G.tag.DB_FAIL,
                        error);
            }
        });

    }

    private void setUpRecyclerView() {

        showEmptyView(false, null);

//        mDbTrackerRows = realm.where(DbTrackerRow.class).findAllSorted(DbTrackerRow.RECOMMENDATION_DATE, Sort.DESCENDING);
//        mDbTrackerRows = mDbTrackerColumn.getTrackerCalls();
        try {
            mRecyclerView.getAdapter().notifyDataSetChanged();
        } catch (Exception e) {

            TrackerAdapter adapter = new TrackerAdapter();
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setAdapter(adapter);

            //Items gap
            /*mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {
                        outRect.bottom = getResources().getDimensionPixelSize(R.dimen.gap_feed_items);
                    } else {
                        outRect.bottom = getResources().getDimensionPixelSize(R.dimen.default_gap);
                    }
                }
            });*/
        }
    }

    private class TrackerAdapter extends RecyclerView.Adapter<TrackerHolder> {

        @Override
        public TrackerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            return new TrackerHolder(inflater.inflate(R.layout.item_tracker_row, parent, false));
        }

        @Override
        public void onBindViewHolder(TrackerHolder holder, int position) {
            holder.bindTracker(mDbTrackerRows.get(position));
        }

        @Override
        public int getItemCount() {
            return mDbTrackerRows.size();
        }
    }

    private class TrackerHolder extends RecyclerView.ViewHolder {

        private MyTextView tvCompany;
        private MyTextView tvRecPrice;
        private MyTextView tvRecDate;
        private MyTextView tvOperation;
        private MyTextView tvExitPrice;
        private MyTextView tvExitDate;
        private MyTextView tvReturn;

        private DbTrackerRow trackerRow;

        public TrackerHolder(View itemView) {
            super(itemView);

            tvCompany = (MyTextView) itemView.findViewById(R.id.tvCompany);
            tvRecPrice = (MyTextView) itemView.findViewById(R.id.tvRecPrice);
            tvRecDate = (MyTextView) itemView.findViewById(R.id.tvRecDate);
            tvOperation = (MyTextView) itemView.findViewById(R.id.tvOperation);
            tvExitPrice = (MyTextView) itemView.findViewById(R.id.tvExitPrice);
            tvExitDate = (MyTextView) itemView.findViewById(R.id.tvExitDate);
            tvReturn = (MyTextView) itemView.findViewById(R.id.tvReturn);
        }

        public void bindTracker(DbTrackerRow trackerRow) {
            this.trackerRow = trackerRow;

            tvCompany.setText(this.trackerRow.getCompanyName());

            tvRecPrice.setText(String.format("%.2f", this.trackerRow.getRecommendationPrice()));

            Calendar calendar = Calendar.getInstance(getResources().getConfiguration().locale);
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd,yyyy");

            calendar.setTimeInMillis(this.trackerRow.getRecommendationDate() * 1000);
            tvRecDate.setText(dateFormat.format(calendar.getTime()));

            tvOperation.setText(this.trackerRow.getOperation());

            tvExitPrice.setText(String.format("%.2f", this.trackerRow.getExitPrice()));

            calendar.setTimeInMillis(this.trackerRow.getExitDate() * 1000);
            tvExitDate.setText(dateFormat.format(calendar.getTime()));

            tvReturn.setText(String.format("%.2f", this.trackerRow.getPercReturn()));
        }
    }

    private void showEmptyView(boolean show, @Nullable String message) {
        if (show) {
            flEmptyView.setVisibility(View.VISIBLE);
            tvError.setVisibility(View.VISIBLE);
            llTrackerView.setVisibility(View.GONE);

            if (TextUtils.isEmpty(message)) {
                tvError.setText("Please wait...");
            } else {
                tvError.setText(message);
            }
        } else {
            flEmptyView.setVisibility(View.GONE);
            llTrackerView.setVisibility(View.VISIBLE);
            tvError.setVisibility(View.GONE);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        realm = Realm.getDefaultInstance();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        realm.close();
    }
}
