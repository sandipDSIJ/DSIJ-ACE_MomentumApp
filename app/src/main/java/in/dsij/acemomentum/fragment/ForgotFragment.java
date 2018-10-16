package in.dsij.acemomentum.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.google.gson.GsonBuilder;

import java.io.IOException;

import in.dsij.acemomentum.G;
import in.dsij.acemomentum.MyApplication;
import in.dsij.acemomentum.R;
import in.dsij.acemomentum.net.CallGenerator;
import in.dsij.acemomentum.net.res.ResMessage;
import in.dsij.acemomentum.view.MyEditText;
import in.dsij.acemomentum.view.MyTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForgotFragment extends Fragment {

    private static final String LOG_TAG = "ForgotFragment";
    private static final int REQ_FORGOT_PASSWORD = 1004;
    private static final String DIAG_FORGOT_PASSWORD = "ForgotFragment.Dialog.DIAG_FORGOT_PASSWORD";
    private static final int REQ_TRY_AGAIN = 1005;
    private static final String DIAG_TRY_AGAIN = "ForgotFragment.Dialog.DIAG_TRY_AGAIN";
    private static final String DIAG_PROGRESS = "ForgotFragment.Dialog.DIAG_PROGRESS";
    private Callbacks mCallbacks;

    private MyTextView tvHeader;
    private TextInputLayout tilUsername;
    private MyEditText etUsername;
    private MyTextView tvLabelHint;
    private MyTextView tvBtnSendLink;
    private AlertBottomSheetDialogFragment progressDialog;

    public static ForgotFragment newInstance() {

        Bundle args = new Bundle();

        ForgotFragment fragment = new ForgotFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public ForgotFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_forgot, container, false);

        findViews(rootView);

        setViews();

        return rootView;
    }

    private void setViews() {
        etUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    tilUsername.setError(null);
                }
            }
        });

        tvBtnSendLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
                } catch (Exception e) {
                    //do nothing
                }

                boolean valid = validateUsername();

                if (valid) {

                    if(MyApplication.isConnected())
                        resetPassword(etUsername.getText().toString().trim());
                    else
                    {
                        Snackbar.make(tvBtnSendLink, "!Check your Internet connection", Snackbar.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private void showProgressDialog() {

        hideProgressDialog();

        progressDialog = AlertBottomSheetDialogFragment.newInstance("Sending Password...", null, null, true);

        progressDialog.show(getFragmentManager(),DIAG_PROGRESS);
    }

    private void hideProgressDialog() {
        try {
            progressDialog.dismiss();
        } catch (Exception e) {
            //
        }
    }

    private void resetPassword(String username) {

        showProgressDialog();

        CallGenerator.resetPassword(username)
                .enqueue(new Callback<ResMessage>() {
                    @Override
                    public void onResponse(Call<ResMessage> call, Response<ResMessage> response) {
                        if (response.isSuccessful()) {
                            try {
                                ResMessage resMessage = response.body();

                                Log.v(LOG_TAG, G.net.resetPassword.TAG +
                                        G.tag.RESPONSE +
                                        new GsonBuilder()
                                                .setPrettyPrinting()
                                                .create()
                                                .toJson(resMessage));

                                if (resMessage == null) {
                                    Log.w(LOG_TAG, G.net.resetPassword.TAG +
                                            G.tag.RESPONSE + "Received NULL Response for resetPassword");
                                } else {

                                    hideProgressDialog();

                                    AlertBottomSheetDialogFragment dialogFragment;
                                    dialogFragment = AlertBottomSheetDialogFragment.newInstance(
                                            "Password has been sent to your email id.",
                                            "Sign In",
                                            null,
                                            false
                                    );

                                    dialogFragment.setTargetFragment(ForgotFragment.this, REQ_FORGOT_PASSWORD);
                                    dialogFragment.show(getFragmentManager(), DIAG_FORGOT_PASSWORD);

                                }

                            } catch (Exception e) {
                                Log.w(LOG_TAG, G.net.resetPassword.TAG + G.tag.RESPONSE + "Parse response Error: " + response.body(), e);
                            }
                        } else {

                            hideProgressDialog();

                            ResMessage errorMessage = null;

                            try {
                                String errorRes = response.errorBody().string();

                                errorMessage = new GsonBuilder().create().fromJson(errorRes, ResMessage.class);

                            } catch (IOException e) {
                                //
                            }

                            switch (response.code()) {
                                case G.net.resetPassword.error.INTERNAL_SERVER_ERROR:

                                    Snackbar.make(tvBtnSendLink, "! Server ran into trouble.", Snackbar.LENGTH_LONG).show();

                                    break;
                                case G.net.resetPassword.error.INVALID_USERNAME:
                                    tilUsername.setError("! Invalid Username/Email Id");

                                    break;
                                case G.net.resetPassword.error.ERROR_IN_SENDING_EMAIL:

//                                    Snackbar.make(tvBtnSendLink, "! Failed to sent email, please contact DSIJ Customer Support.", Snackbar.LENGTH_LONG).show();

                                    // TODO: 9/13/2017 Show Dialog

                                    AlertBottomSheetDialogFragment dialogFragment;
                                    dialogFragment = AlertBottomSheetDialogFragment.newInstance(
                                            "Failed to sent email, please contact DSIJ Customer Support.",
                                            "Try Again",
                                            null,
                                            false
                                    );

                                    dialogFragment.setTargetFragment(ForgotFragment.this, REQ_TRY_AGAIN);
                                    dialogFragment.show(getFragmentManager(), DIAG_TRY_AGAIN);

                                    break;
                                case G.net.resetPassword.error.EMPTY_PARAMS:
                                    Snackbar.make(tvBtnSendLink, "! App ran into trouble.", Snackbar.LENGTH_LONG).show();

                                    // TODO: 9/13/2017 Reload Fragment

                                    break;
                                default:
                                    Snackbar.make(tvBtnSendLink, "! Server ran into trouble.", Snackbar.LENGTH_LONG).show();
                                    break;
                            }
                            Log.w(LOG_TAG, G.net.resetPassword.TAG + G.tag.ERROR + "Error Response code : " + response.code() + " Response Body:\n" +
                                    new GsonBuilder()
                                            .setPrettyPrinting()
                                            .create()
                                            .toJson(errorMessage));
                        }
                    }

                    @Override
                    public void onFailure(Call<ResMessage> call, Throwable t) {
                        Log.e(LOG_TAG, G.net.resetPassword.TAG + G.tag.FAILED + "Failed API Call : ", t);

                        hideProgressDialog();
                    }
                });
    }

    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_FORGOT_PASSWORD) {

            switch (resultCode) {
                case AlertBottomSheetDialogFragment.RESULT_PRIMARY:
                    mCallbacks.onClickSignIn();
                    break;
                default:
                    mCallbacks.onClickSignIn();
            }

        } else if (requestCode == REQ_TRY_AGAIN) {
            switch (resultCode) {
                case AlertBottomSheetDialogFragment.RESULT_PRIMARY:
                    tvBtnSendLink.performClick();
                    break;
                default:
                    mCallbacks.onClickSignIn();
            }
        }

    }*/

    private void findViews(View rootView) {
        tvHeader = (MyTextView) rootView.findViewById(R.id.tvHeader);
        tilUsername = (TextInputLayout) rootView.findViewById(R.id.tilUsername);
        etUsername = (MyEditText) rootView.findViewById(R.id.etUsername);
        tvLabelHint = (MyTextView) rootView.findViewById(R.id.tvLabelHint);
        tvBtnSendLink = (MyTextView) rootView.findViewById(R.id.tvBtnSendLink);
    }

    private boolean validateUsername() {
        String userName = etUsername.getText().toString().trim();

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(userName).matches() &&
                (TextUtils.isEmpty(userName) ||
                        userName.contains(" ") ||
                        userName.length() < 6 ||
                        userName.length() > 99)) {
            tilUsername.setError("! Invalid Username/Email Id");
            return false;
        } else {
            tilUsername.setError(null);
            return true;
        }
    }

    public interface Callbacks {

        void onClickSendResetLink();

        void onClickSignIn();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

}
