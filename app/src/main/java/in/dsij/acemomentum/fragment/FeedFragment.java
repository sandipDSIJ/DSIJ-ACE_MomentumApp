package in.dsij.acemomentum.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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
import in.dsij.acemomentum.MyApplication;
import in.dsij.acemomentum.R;
import in.dsij.acemomentum.db.DbCall;
import in.dsij.acemomentum.db.DbProduct;
import in.dsij.acemomentum.net.CallGenerator;
import in.dsij.acemomentum.net.res.ResGetCalls;
import in.dsij.acemomentum.net.res.ResMessage;
import in.dsij.acemomentum.view.MyTextView;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FeedFragment extends Fragment {

    private static final String ARG_PRODUCT_ID = "FeedFragment.Arg.ARG_PRODUCT_ID";
    private static final String LOG_TAG = "FeedFragment";
    private static final String DIAG_DOWNLOADABLES = "FeedFragment.Dialog.Tag.DIAG_DOWNLOADABLES";

    private Realm realm;

    private DbProduct mDbProduct;

    private long mProductId;

    private RecyclerView mRecyclerView;
    private RealmResults<DbCall> mDbCalls;

    private FrameLayout flEmptyView;
    private MyTextView tvError;
    private LinearLayout llFeedView;

    public static FeedFragment newInstance(long productId) {

        Bundle args = new Bundle();
        args.putLong(ARG_PRODUCT_ID, productId);
        FeedFragment fragment = new FeedFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public FeedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_feed, container, false);

        findViews(rootView);

        setViews();

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mProductId = getArguments().getLong(ARG_PRODUCT_ID);

        DbProduct dbProduct = realm.where(DbProduct.class).equalTo(DbProduct.PRODUCT_ID, mProductId).findFirst();

        if (dbProduct.isSubscribed() || dbProduct.isTrial()) {
            getCalls();

        } else {
            showEmptyView(true, "You are not subscribed to this product");
        }


    }

    private void showEmptyView(boolean show, @Nullable String message) {
        if (show) {
            flEmptyView.setVisibility(View.VISIBLE);
            tvError.setVisibility(View.VISIBLE);
            llFeedView.setVisibility(View.GONE);

            if (TextUtils.isEmpty(message)) {
                tvError.setText("Please wait...");
            } else {
                tvError.setText(message);
            }
        } else {
            flEmptyView.setVisibility(View.GONE);
            llFeedView.setVisibility(View.VISIBLE);
            tvError.setVisibility(View.GONE);
        }
    }

    private void getCalls() {

        CallGenerator.getCalls(mProductId).enqueue(new Callback<List<ResGetCalls>>() {
            @Override
            public void onResponse(Call<List<ResGetCalls>> call, Response<List<ResGetCalls>> response) {
                if (response.isSuccessful()) {
                    try {
                        List<ResGetCalls> resGetCalls = response.body();

                        Log.v(LOG_TAG, G.net.getCalls.TAG +
                                G.tag.RESPONSE +
                                new GsonBuilder()
                                        .setPrettyPrinting()
                                        .create()
                                        .toJson(resGetCalls));

                        if (resGetCalls == null) {
                            Log.w(LOG_TAG, G.net.getCalls.TAG +
                                    G.tag.RESPONSE + "Received NULL Call List");

                            showEmptyView(true, "No Calls available for this Product");

                        } else {
                            insertCalls(resGetCalls);
                        }

                    } catch (Exception e) {
                        Log.w(LOG_TAG, G.net.getCalls.TAG + G.tag.RESPONSE + "Parse response Error: " + response.body(), e);
                    }
                }
                else if (response.code() == 403 || response.code() == 404 || response.code() == 500 || response.code() == 503) {
                    showEmptyView(true, "Sorry, the server is currently\n unavailable please try agein later");
                    return;
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

                    Log.w(LOG_TAG, G.net.getCalls.TAG + G.tag.ERROR + "Error Response code : " + response.code() + " Response Body:\n" +
                            new GsonBuilder()
                                    .setPrettyPrinting()
                                    .create()
                                    .toJson(errorMessage));

                }
            }

            @Override
            public void onFailure(Call<List<ResGetCalls>> call, Throwable t) {
                Log.e(LOG_TAG, G.net.getCalls.TAG + G.tag.FAILED + "Failed API Call : ", t);
                showEmptyView(true, "Not available at the moment");
                if (!MyApplication.isConnected()) {
                    //Snackbar.make(tvError, "!Check your Internet connection", Snackbar.LENGTH_LONG).show();
                    showEmptyView(true,"!Check your Internet connection");
                }
            }
        });

    }

    private void insertCalls(final List<ResGetCalls> resGetCalls) {
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

                realm.createOrUpdateAllFromJson(DbCall.class, new Gson().toJson(resGetCalls));

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

        showEmptyView(false, null);

        mDbCalls = realm.where(DbCall.class).findAllSorted(DbCall.PRIORITY_ID, Sort.ASCENDING);
        Log.d("DB_CallDatabase",mDbCalls.toString());

        mDbCalls = realm.where(DbCall.class).equalTo(DbCall.PRODUCT_ID, mProductId).findAllSorted(DbCall.PRIORITY_ID, Sort.ASCENDING);
        try {
            mRecyclerView.getAdapter().notifyDataSetChanged();
        } catch (Exception e) {

            CallAdapter adapter = new CallAdapter();
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
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rvFeed);
        flEmptyView = (FrameLayout) rootView.findViewById(R.id.flEmptyView);
        tvError = (MyTextView) rootView.findViewById(R.id.tvError);
        llFeedView = (LinearLayout) rootView.findViewById(R.id.llFeedView);
    }

    private class CallAdapter extends RecyclerView.Adapter<CallHolder> {

        @Override
        public CallHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            /*if (viewType == 1) {
                return new CallHolder(inflater.inflate(R.layout.item_feed2, parent, false));
            } else {
                return new CallHolder(inflater.inflate(R.layout.item_feed_old2, parent, false));
            }*/
            return new CallHolder(inflater.inflate(R.layout.item_row, parent, false));
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

    private class CallHolder extends RecyclerView.ViewHolder {

        private MyTextView tvCompany;
        private MyTextView tvDate;
        private MyTextView tvRecoPrice;
        private MyTextView tvTargetPrice;
        private MyTextView tvCmp;

        private DbCall call;

        public CallHolder(View itemView) {
            super(itemView);

            tvCompany = (MyTextView) itemView.findViewById(R.id.tvCompany);
            tvDate = (MyTextView) itemView.findViewById(R.id.tvDate);
            tvRecoPrice = (MyTextView) itemView.findViewById(R.id.tvRecoPrice);
            tvTargetPrice = (MyTextView) itemView.findViewById(R.id.tvTargetPrice);
            tvCmp = (MyTextView) itemView.findViewById(R.id.tvCmp);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DownloadablesBottomSheetDialogFragment dialogFragment = DownloadablesBottomSheetDialogFragment.newInstance(call.getID());
                    dialogFragment.setTargetFragment(FeedFragment.this,0);
                    dialogFragment.show(getFragmentManager(),DIAG_DOWNLOADABLES);
                }
            });
        }

        public void bindFeed(DbCall call) {
            this.call = call;

            tvCompany.setText(this.call.getCompany());

            Calendar calendar = Calendar.getInstance(getResources().getConfiguration().locale);
            calendar.setTimeInMillis(this.call.getReccoDate() * 1000);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM\nyyyy");
            tvDate.setText(dateFormat.format(calendar.getTime()));

            tvRecoPrice.setText(String.format("%.2f", this.call.getReccoPrice()));
            tvTargetPrice.setText(String.format("%.2f", this.call.getTargetPrice()));
            tvCmp.setText(String.format("%.2f", this.call.getCurrentMarketPrice()));

            Log.d(LOG_TAG, "Simplified Date: " + dateFormat.format(calendar.getTime()));

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
