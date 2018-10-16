package in.dsij.acemomentum.fragment;


import android.content.Context;
import android.graphics.Rect;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import in.dsij.acemomentum.G;
import in.dsij.acemomentum.R;
import in.dsij.acemomentum.db.DbCall;
import in.dsij.acemomentum.db.DbProduct;
import in.dsij.acemomentum.net.CallGenerator;
import in.dsij.acemomentum.net.res.ResGetLiveCalls;
import in.dsij.acemomentum.net.res.ResGetProducts;
import in.dsij.acemomentum.net.res.ResMessage;
import in.dsij.acemomentum.view.MyTextView;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class LiveFeedFragment extends Fragment {

    private static final String LOG_TAG = "LiveFeedFragment";

    private Realm realm;

    private RecyclerView mRecyclerView;
    private FrameLayout flEmptyView;
    private MyTextView tvError;

    private RealmResults<DbCall> mDbCalls;

    public static LiveFeedFragment newInstance() {

        Bundle args = new Bundle();

        LiveFeedFragment fragment = new LiveFeedFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public LiveFeedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_live_feed, container, false);

        findViews(rootView);

        setViews();

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getProducts();

    }

    private void getProducts() {

        showEmptyView(true,"Loading...");

        CallGenerator.getProducts().enqueue(new Callback<ResGetProducts>() {
            @Override
            public void onResponse(Call<ResGetProducts> call, Response<ResGetProducts> response) {
                if (response.isSuccessful()) {
                    try {
                        ResGetProducts resGetProducts = response.body();

                        Log.v(LOG_TAG, G.net.getProducts.TAG +
                                G.tag.RESPONSE +
                                new GsonBuilder()
                                        .setPrettyPrinting()
                                        .create()
                                        .toJson(resGetProducts));

                        if (resGetProducts == null) {
                            Log.w(LOG_TAG, G.net.getProducts.TAG +
                                    G.tag.RESPONSE + "Received NULL Product List");
                        } else {
                            insertProducts(resGetProducts);
                        }

                    } catch (Exception e) {
                        Log.w(LOG_TAG, G.net.getProducts.TAG + G.tag.RESPONSE + "Parse response Error: " + response.body(), e);
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

                    Log.w(LOG_TAG, G.net.getProducts.TAG + G.tag.ERROR + "Error Response code : " + response.code() + " Response Body:\n" +
                            new GsonBuilder()
                                    .setPrettyPrinting()
                                    .create()
                                    .toJson(errorMessage));

                }
            }

            @Override
            public void onFailure(Call<ResGetProducts> call, Throwable t) {
                Log.e(LOG_TAG, G.net.getProducts.TAG + G.tag.FAILED + "Failed API Call : ", t);
            }
        });
    }

    private void insertProducts(final ResGetProducts resGetProducts) {

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Log.d(LOG_TAG,
                        G.tag.DB_TRANSACTION_WRITE + "Products");

                try {
//                    realm.where(DbProduct.class).findAll().deleteAllFromRealm();
                } catch (Exception e) {
                    Log.v(LOG_TAG, G.tag.DB_TRANSACTION_WRITE + "Already no Product present ", e);
                } finally {
                    realm.createOrUpdateAllFromJson(DbProduct.class, new Gson().toJson(resGetProducts.getProduct()));
                }

            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.d(LOG_TAG,
                        G.tag.DB_TRANSACTION_WRITE + G.tag.DB_SUCCESS);

                getLiveCalls();

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

    private void showEmptyView(boolean show, @Nullable String message) {
        if (show) {
            flEmptyView.setVisibility(View.VISIBLE);
            tvError.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);

            if (TextUtils.isEmpty(message)) {
                tvError.setText("Please wait...");
            } else {
                tvError.setText(message);
            }
        } else {
            flEmptyView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            tvError.setVisibility(View.GONE);
        }
    }

    private void setViews() {
        // TODO: 10/10/2017 Set default View

        /*tvFeedName.setText("Pop Stocks");

        CallAdapter adapter = new CallAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(adapter);

        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
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

    private void findViews(View rootView) {
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rvLiveFeed);
        flEmptyView = (FrameLayout) rootView.findViewById(R.id.flEmptyView);
        tvError = (MyTextView) rootView.findViewById(R.id.tvError);
    }

    private void getLiveCalls() {

        CallGenerator.getLiveCalls().enqueue(new Callback<ResGetLiveCalls>() {
            @Override
            public void onResponse(Call<ResGetLiveCalls> call, Response<ResGetLiveCalls> response) {

                if (response.isSuccessful()) {
                    try {
                        ResGetLiveCalls resGetLiveCalls = response.body();

                        Log.v(LOG_TAG, G.net.getLiveCalls.TAG +
                                G.tag.RESPONSE +
                                new GsonBuilder()
                                        .setPrettyPrinting()
                                        .create()
                                        .toJson(resGetLiveCalls));

                        if (resGetLiveCalls == null) {
                            Log.w(LOG_TAG, G.net.getLiveCalls.TAG +
                                    G.tag.RESPONSE + "Received NULL Call List");
                        } else {
                            insertLiveCalls(resGetLiveCalls);
                        }

                    } catch (Exception e) {
                        Log.w(LOG_TAG, G.net.getLiveCalls.TAG + G.tag.RESPONSE + "Parse response Error: " + response.body(), e);
                    }
                }

                else {

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

                    Log.w(LOG_TAG, G.net.getLiveCalls.TAG + G.tag.ERROR + "Error Response code : " + response.code() + " Response Body:\n" +
                            new GsonBuilder()
                                    .setPrettyPrinting()
                                    .create()
                                    .toJson(errorMessage));

                }
            }

            @Override
            public void onFailure(Call<ResGetLiveCalls> call, Throwable t) {
                Log.e(LOG_TAG, G.net.getLiveCalls.TAG + G.tag.FAILED + "Failed API Call : ", t);
                showEmptyView(true, "Not Available\n\nYou are not subscribed to any Product");
            }
        });
    }

    private void insertLiveCalls(final ResGetLiveCalls resGetLiveCalls) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Log.d(LOG_TAG,
                        G.tag.DB_TRANSACTION_WRITE + "Calls");

                /*try {
                    realm.where(DbCall.class).findAll().deleteAllFromRealm();
                } catch (Exception e) {
                    Log.v(LOG_TAG, G.tag.DB_TRANSACTION_WRITE + "Already no Calls present ", e);
                } finally {
                    realm.createOrUpdateAllFromJson(DbCall.class, new Gson().toJson(resGetCalls.getCalls()));
                }*/

                realm.createOrUpdateAllFromJson(DbCall.class, new Gson().toJson(resGetLiveCalls.getLiveCall()));

            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.d(LOG_TAG,
                        G.tag.DB_TRANSACTION_WRITE + G.tag.DB_SUCCESS);

                setUpRecyclerView();

//                mCallbacks.loggedIn();

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

        RealmResults<DbProduct> subscribedProducts = realm.where(DbProduct.class)
                .equalTo(DbProduct.SUBSCRIBED, true)
                .or()
                .equalTo(DbProduct.TRIAL, true)
                .findAll();

        RealmQuery<DbCall> query = realm.where(DbCall.class);

        for (int i = 0; i < subscribedProducts.size(); i++) {
            if (i != 0) {
                query.or();
            }
            query.equalTo(DbCall.PRODUCT_ID, subscribedProducts.get(i).getProductId());
        }

        mDbCalls = query.findAllSorted(DbCall.PRIORITY_ID, Sort.ASCENDING);

        if (subscribedProducts.isEmpty()) {
            showEmptyView(true, "Not Available\n\nYou are not subscribed to any Product");
        } else if (mDbCalls.isEmpty()) {
            showEmptyView(true, "Not available at the moment");
        } else {
            showEmptyView(false, null);
        }

        /*mDbCalls = realm.where(DbCall.class).findAllSorted(DbCall.TIME, Sort.DESCENDING);
        mRecyclerView.setVisibility(View.VISIBLE);
        flEmptyView.setVisibility(View.GONE);*/

        try {
            mRecyclerView.getAdapter().notifyDataSetChanged();
        } catch (Exception e) {

            CallAdapter adapter = new CallAdapter();
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setAdapter(adapter);

            mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {
                        outRect.bottom = getResources().getDimensionPixelSize(R.dimen.gap_feed_items);
                    } else {
                        outRect.bottom = getResources().getDimensionPixelSize(R.dimen.default_gap);
                    }
                }
            });
        }
    }


    private class CallHolder extends RecyclerView.ViewHolder {

        private MyTextView tvHeadline;
        private MyTextView tvSummery;
        private MyTextView tvTime;

        private DbCall call;

        public CallHolder(View itemView) {
            super(itemView);

            tvHeadline = (MyTextView) itemView.findViewById(R.id.tvHeadline);
            tvSummery = (MyTextView) itemView.findViewById(R.id.tvSummery);
            tvTime = (MyTextView) itemView.findViewById(R.id.tvTime);
        }

        public void bindFeed(DbCall call) {
            /*this.call = call;

            tvHeadline.setText(this.call.getHeadline());
//            tvSummery.setText(this.call.getDescription());

            Date date = new Date(this.call.getTime() * 1000);
            String timeString = new SimpleDateFormat("hh:mm a", getResources().getConfiguration().locale).format(date);

            Calendar callCalendar = Calendar.getInstance(getResources().getConfiguration().locale);
            callCalendar.setTimeInMillis(this.call.getTime() * 1000);
            int callDay = callCalendar.get(Calendar.DAY_OF_MONTH);

            Calendar currentCalender = Calendar.getInstance(getResources().getConfiguration().locale);
            int currentDay = currentCalender.get(Calendar.DAY_OF_MONTH);

            int headerColor;

            if (currentDay == callDay) {
                headerColor = getResources().getColor(R.color.font_feed_header_new);
                timeString = "Today " + timeString;
            } else {
                headerColor = getResources().getColor(R.color.font_feed_header_older);
                timeString = new SimpleDateFormat("E dd MMM ").format(this.call.getTime() * 1000) + timeString;
            }
            tvHeadline.setTextColor(headerColor);
            tvSummery.setText(this.call.getDescription());
            tvTime.setText(timeString);

            if (currentDay != callDay) {

            }

            Log.d(LOG_TAG, "Simplified Date: " + timeString);*/
        }
    }


    private class CallAdapter extends RecyclerView.Adapter<CallHolder> {

        @Override
        public CallHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            if (viewType == 1) {
                return new CallHolder(inflater.inflate(R.layout.item_feed2, parent, false));
            } else {
                return new CallHolder(inflater.inflate(R.layout.item_feed_old2, parent, false));
            }

        }

        @Override
        public void onBindViewHolder(CallHolder holder, int position) {
            holder.bindFeed(mDbCalls.get(position));
        }

        @Override
        public int getItemViewType(int position) {
            // TODO: 10/10/2017 Change view type by time

            /*if (mDbCalls.get(position).isLatest()) {
                return 1;
            } else {
                return 2;
            }*/

            return 1;
        }

        @Override
        public int getItemCount() {
            return mDbCalls.size();
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
