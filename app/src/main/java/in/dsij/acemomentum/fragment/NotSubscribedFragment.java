package in.dsij.acemomentum.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.gson.GsonBuilder;

import java.io.IOException;

import in.dsij.acemomentum.G;
import in.dsij.acemomentum.MyApplication;
import in.dsij.acemomentum.R;
import in.dsij.acemomentum.net.CallGenerator;
import in.dsij.acemomentum.net.res.ResGetCalls;
import in.dsij.acemomentum.net.res.ResGetProductBrief;
import in.dsij.acemomentum.net.res.ResMessage;
import in.dsij.acemomentum.view.MyTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotSubscribedFragment extends Fragment {

    private static final String ARG_ERROR_TEXT = "NotSubscribedFragment.Arg.ARG_ERROR_TEXT";
    private static final java.lang.String ARG_SHOW_BRIEF = "NotSubscribedFragment.Arg.ARG_SHOW_BRIEF";
    private static final String ARG_PRODUCT_ID = "NotSubscribedFragment.Arg.ARG_PRODUCT_ID";
    private static final String LOG_TAG = "NotSubscribedFragment";

    private String errorText;
    private boolean showBrief;
    private long mProductId;

    private ImageView ivError;
    private MyTextView tvError;
    private MyTextView tvBrief;
    private MyTextView tvBtnKnowMore;

    public static NotSubscribedFragment newInstance(@Nullable String errorText, Boolean showBrief, long productId) {

        Bundle args = new Bundle();
        args.putString(ARG_ERROR_TEXT, errorText);
        args.putBoolean(ARG_SHOW_BRIEF, showBrief);
        args.putLong(ARG_PRODUCT_ID, productId);
        NotSubscribedFragment fragment = new NotSubscribedFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public NotSubscribedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        errorText = getArguments().getString(ARG_ERROR_TEXT);
        showBrief = getArguments().getBoolean(ARG_SHOW_BRIEF);
        mProductId = getArguments().getLong(ARG_PRODUCT_ID);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_not_subscribed, container, false);

        findViews(rootView);

        setViews();

        return rootView;
    }

    private void setViews() {

        if (!TextUtils.isEmpty(errorText)) {
            tvError.setText(errorText);
        }

        tvBrief.setVisibility(View.GONE);
        tvBtnKnowMore.setVisibility(View.GONE);

    }

    private void findViews(View rootView) {
        ivError = (ImageView) rootView.findViewById(R.id.ivError);
        tvError = (MyTextView) rootView.findViewById(R.id.tvError);
        tvBrief = (MyTextView) rootView.findViewById(R.id.tvBrief);
        tvBtnKnowMore = (MyTextView) rootView.findViewById(R.id.tvBtnKnowMore);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (showBrief) {
            getBrief();
        }
    }

    private void getBrief() {

        CallGenerator.getProductBrief(mProductId).enqueue(new Callback<ResGetProductBrief>() {
            @Override
            public void onResponse(Call<ResGetProductBrief> call, Response<ResGetProductBrief> response) {
                if (response.isSuccessful()) {
                    try {
                        ResGetProductBrief resGetProductBrief = response.body();

                        Log.v(LOG_TAG, G.net.getCalls.TAG +
                                G.tag.RESPONSE +
                                new GsonBuilder()
                                        .setPrettyPrinting()
                                        .create()
                                        .toJson(resGetProductBrief));

                        if (resGetProductBrief == null) {
                            Log.w(LOG_TAG, G.net.getCalls.TAG +
                                    G.tag.RESPONSE + "Received NULL Brief");

                            //do nothing

                        } else {
                            setBrief(resGetProductBrief);
                        }

                    } catch (Exception e) {
                        Log.w(LOG_TAG, G.net.getProductBrief.TAG + G.tag.RESPONSE + "Parse response Error: " + response.body(), e);
                        //do nothing
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

                        default:
                            // TODO: 10/10/2017
                            break;
                    }

                    Log.w(LOG_TAG, G.net.getProductBrief.TAG + G.tag.ERROR + "Error Response code : " + response.code() + " Response Body:\n" +
                            new GsonBuilder()
                                    .setPrettyPrinting()
                                    .create()
                                    .toJson(errorMessage));

                }
            }

            @Override
            public void onFailure(Call<ResGetProductBrief> call, Throwable t) {
                Log.e(LOG_TAG, G.net.getProductBrief.TAG + G.tag.FAILED + "Failed API Call : ", t);
                //do nothing
                if (!MyApplication.isConnected()) {
                    Snackbar.make(getView(), "!Check your Internet connection", Snackbar.LENGTH_LONG).show();
                }
            }
        });

    }

    private void setBrief(final ResGetProductBrief resGetProductBrief) {

        if (TextUtils.isEmpty(resGetProductBrief.getBrief()) && TextUtils.isEmpty(resGetProductBrief.getLink())){
            //do nothing
            return;
        }

        ivError.setVisibility(View.GONE);
        tvError.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(resGetProductBrief.getBrief())) {
            tvBrief.setVisibility(View.VISIBLE);
            tvBrief.setText(resGetProductBrief.getBrief());
        }

        if (!TextUtils.isEmpty(resGetProductBrief.getLink())){
            tvBtnKnowMore.setVisibility(View.VISIBLE);
            String urlLink=resGetProductBrief.getLink();
            urlLink=urlLink.replaceAll("~","");
            resGetProductBrief.setLink(urlLink);
            tvBtnKnowMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(resGetProductBrief.getLink()));
                    startActivity(intent);
                }
            });
        }
    }

}
