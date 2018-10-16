package in.dsij.acemomentum.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import in.dsij.acemomentum.G;
import in.dsij.acemomentum.MainActivity;
import in.dsij.acemomentum.MyApplication;
import in.dsij.acemomentum.R;
import in.dsij.acemomentum.db.DbProduct;
import in.dsij.acemomentum.net.CallGenerator;
import in.dsij.acemomentum.net.res.ResGetProducts;
import in.dsij.acemomentum.net.res.ResMessage;
import in.dsij.acemomentum.view.MyTextView;
import in.dsij.acemomentum.view.ViewPagerIndicator;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeedPagerFragment extends Fragment {

    private static final String LOG_TAG = "FeedPagerFragment";

    private Realm realm;

    private ViewPager mViewPager;
    private RealmResults<DbProduct> mDbProducts;
    private MyTextView tvHeader;
    //private ViewPagerIndicator indicator;
    private ImageView ivLeft;
    private ImageView ivRight;

    public static FeedPagerFragment newInstance() {

        Bundle args = new Bundle();

        FeedPagerFragment fragment = new FeedPagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public FeedPagerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_feed_pager, container, false);

//        loadData();

        findViews(rootView);

        setViews();

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getProducts();

    }

    private void findViews(View rootView) {
        mViewPager = (ViewPager) rootView.findViewById(R.id.vpFeed);
        tvHeader = (MyTextView) rootView.findViewById(R.id.tvHeader);
        //indicator = (ViewPagerIndicator) rootView.findViewById(R.id.indicator);
        ivLeft = rootView.findViewById(R.id.ivLeft);
        ivRight = rootView.findViewById(R.id.ivRight);
    }

    private void setViews() {
        // TODO: 10/10/2017 Set Default Views

    }

    private void getProducts() {
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
                }
                else if (response.code() == 403 || response.code() == 404 || response.code() == 500 || response.code() == 503) {
                    Snackbar.make(getView(), "Sorry, the server is currently\n unavailable please try agein later", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                else {

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

                if (!MyApplication.isConnected()) {
                    Snackbar.make(getView(), "!Check your Internet connection", Snackbar.LENGTH_LONG).show();
                }
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

                setUpViewPager();

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

    private void setUpViewPager() {
        // TODO: 10/10/2017 Setup ViewPager

        mDbProducts = realm.where(DbProduct.class).findAll();
        try {
            mViewPager.setAdapter(new FeedPagerAdapter(getChildFragmentManager()));
        } catch (Exception e) {
//            getFragmentManager().popBackStack();
            Log.e(LOG_TAG, e.toString());
        }

        int index = 0;

        try {
            long openingProductId = getActivity().getIntent().getLongExtra(MainActivity.EXTRA_PRODUCT_ID, 0);
            if (openingProductId != 0) {

                index = mDbProducts.indexOf(mDbProducts.where().equalTo(DbProduct.PRODUCT_ID, openingProductId).findFirst());
            }
        } catch (Exception e) {
            index = 0;
        }

        ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                try {
                    setHeaderText(mDbProducts.get(position).getProductName());
                } catch (Exception e) {
                    //
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
        mViewPager.addOnPageChangeListener(onPageChangeListener);

        try {
          //  indicator.setupWithViewPager(mViewPager);
           // indicator.addOnPageChangeListener(onPageChangeListener);

        } catch (Exception e)
        {

        }

        Log.d(LOG_TAG, "Product Size: " + mDbProducts.size() + " Index: " + index);

        mViewPager.setCurrentItem(index, true);
        setHeaderText(mDbProducts.get(index).getProductName());

        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentPosition = mViewPager.getCurrentItem();
                try {
                    mViewPager.setCurrentItem(currentPosition - 1, true);
                } catch (Exception e) {
                    //do nothing
                }
            }
        });

        ivRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentPosition = mViewPager.getCurrentItem();
                try {
                    mViewPager.setCurrentItem(currentPosition + 1, true);
                } catch (Exception e) {
                    //do nothing
                }
            }
        });
    }

    private void setHeaderText(String productName) {
        tvHeader.setText("CALLS");
    }

    private class FeedPagerAdapter extends FragmentStatePagerAdapter {

        public FeedPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            DbProduct dbProduct = mDbProducts.get(position);

            if (dbProduct.isSubscribed() || dbProduct.isTrial()/*true*/) {
                return FeedFragment.newInstance(dbProduct.getProductId());
            } else {
                return NotSubscribedFragment.newInstance("Not subscribed to this Product", true, dbProduct.getProductId());
            }
        }

        @Override
        public int getCount() {
            return mDbProducts.size();
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
