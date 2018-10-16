package in.dsij.acemomentum.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import in.dsij.acemomentum.G;
import in.dsij.acemomentum.MyApplication;
import in.dsij.acemomentum.R;
import in.dsij.acemomentum.db.DbTrackerColumn;
import in.dsij.acemomentum.net.CallGenerator;
import in.dsij.acemomentum.net.res.ReqCallTracker;
import in.dsij.acemomentum.net.res.ResGetTrackerCol;
import in.dsij.acemomentum.net.res.ResMessage;
import in.dsij.acemomentum.view.MyTextView;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrackerPagerFragment extends Fragment {

    private static final String LOG_TAG = "TrackerPagerFragment";

    private Realm realm;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private FrameLayout flEmptyView;
    private MyTextView tvError;

    //private AQuery mAquery;
    private RealmResults<DbTrackerColumn> mDbTrackerColumns;

    public static TrackerPagerFragment newInstance() {

        Bundle args = new Bundle();

        TrackerPagerFragment fragment = new TrackerPagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public TrackerPagerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_tracker_pager, container, false);
        //mAquery=new AQuery(getActivity());
        findViews(rootView);

        setViews();

        return rootView;
    }

    private void setViews() {
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        showEmptyView(true, "Loading...");
        getTrackerData();

    }

    private void findViews(View rootView) {
        mTabLayout = (TabLayout) rootView.findViewById(R.id.tab_layout_tracker);
        mViewPager = (ViewPager) rootView.findViewById(R.id.vpTracker);
        flEmptyView = (FrameLayout) rootView.findViewById(R.id.flEmptyView);
        tvError = (MyTextView) rootView.findViewById(R.id.tvError);
    }

    /*public void jsonCallback(String url, JSONObject json, AjaxStatus status) {
        //When JSON is not null
        if (json != null) {
            String[] cities = null;
            //Create GSON object
            Gson gson = new GsonBuilder().create();
            try {
                //Get JSON response by converting JSONArray into String
                String jsonResponse = json.getJSONArray("Cities").toString();
                //Using fromJson method deserialize JSON response [Convert JSON array into Java array]
                cities = gson.fromJson(jsonResponse, String[].class);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                Toast.makeText(mAquery.getContext(), "Error in parsing JSON", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(mAquery.getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }
            //Set City adapter with created Java array 'cities'

        }
        //When JSON is null
        else {
            //When response code is 500 (Internal Server Error)
            if(status.getCode() == 500){
                Toast.makeText(mAquery.getContext(),"Server is busy or down. Try again!",Toast.LENGTH_SHORT).show();
            }
            //When response code is 404 (Not found)
            else if(status.getCode() == 404){
                Toast.makeText(mAquery.getContext(),"Resource not found!",Toast.LENGTH_SHORT).show();
            }
            //When response code is other 500 or 404
            else{
                Toast.makeText(mAquery.getContext(),"Unexpected Error occured",Toast.LENGTH_SHORT).show();
            }
        }
    }*/
    private void getTrackerData() {
       /* String url="https://www.dsij.in/desktopmodules/services/api/MobileApp/GetInvestorCallTracker_ACEMomentum";
        HashMap<String,String> map=new HashMap<String, String>();
        ReqCallTracker mReqCallTracker=new ReqCallTracker();
       *//* mReqCallTracker.setAppid(21);
        mReqCallTracker.setDeviceid("1234");
        mReqCallTracker.setToken("a3d8ecc19373113c863bf5ac3bd26d8cef9bb72283c352298ac70684d50f1e54");
        mReqCallTracker.setUsername("sandipp");*//*
        map.put("username","sandipp@dsij.in");
        map.put("token","a3d8ecc19373113c863bf5ac3bd26d8cef9bb72283c352298ac70684d50f1e54");
        map.put("deviceid","21");
        map.put("versionno","21");
        map.put("appid","61");*/

        // mAquery.ajax(url,map, JSONObject.class, this,"jsonCallback");





        /*mAquery.ajax(url, map, JSONArray.class, new AjaxCallback<JSONArray>() {
            @Override
            public void callback(String url, JSONArray json, AjaxStatus status) {
                if(json != null){

                    Toast.makeText(getContext(), "Error:" + status.getCode(), Toast.LENGTH_LONG).show();
                    //Toast.makeText(aq.getContext(), jsonArray.toString(), Toast.LENGTH_LONG).show();
                }else{
                    //ajax error, show error code
                    Toast.makeText(getContext(), "Error:" + status.getCode(), Toast.LENGTH_LONG).show();
                }
                // do something with the result
            }
        });*/

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
                }
                if (response.code() == 403 || response.code() == 404 || response.code() == 500 || response.code() == 503) {
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
                    } catch (Exception e) {
                        //
                    }

                    switch (response.code()) {
                        case G.net.getTrackerData.error.INVALID_APP_ID:
                            //showEmptyView(true, "Invaid App ID");
                            break;
                        case G.net.getTrackerData.error.INVALID_TOKEN:
                            break;
                        case G.net.getTrackerData.error.SERVER_ERROR:
                            showEmptyView(true, G.net.getTrackerData.error.SERVER_ERROR + "");
                            break;
                        default:
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
                if (!MyApplication.isConnected()) {
                    //Snackbar.make(tvError, "!Check your Internet connection", Snackbar.LENGTH_LONG).show();
                    showEmptyView(true, "!Check your Internet connection");
                }
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

                setUpViewPager();

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
        showEmptyView(false, null);
        mDbTrackerColumns = realm.where(DbTrackerColumn.class).findAll();

        try {
            mViewPager.setAdapter(new TrackerColumnAdapter(getChildFragmentManager()));
        } catch (Exception e) {
            Log.e(LOG_TAG, e.toString());
            return;
        }
    }

    private class TrackerColumnAdapter extends FragmentStatePagerAdapter {

        public TrackerColumnAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            DbTrackerColumn dbTrackerColumn = mDbTrackerColumns.get(position);
            return TrackerFragment.newInstance(dbTrackerColumn.getColumnId());
        }

        @Override
        public int getCount() {
            return mDbTrackerColumns.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "TRACKER";
        }
    }

    private void showEmptyView(boolean show, @Nullable String message) {
        if (show) {
            flEmptyView.setVisibility(View.VISIBLE);
            tvError.setVisibility(View.VISIBLE);
            mViewPager.setVisibility(View.GONE);

            if (TextUtils.isEmpty(message)) {
                tvError.setText("Please wait...");
            } else {
                tvError.setText(message);
            }
        } else {
            flEmptyView.setVisibility(View.GONE);
            mViewPager.setVisibility(View.VISIBLE);
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
