package in.dsij.acemomentum.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.google.gson.GsonBuilder;

import java.io.IOException;

import in.dsij.acemomentum.G;
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
public class ChangePassword extends Fragment {

    private static final String LOG_TAG = "ForgotFragment";
    private static final int REQ_FORGOT_PASSWORD = 1004;
    private static final String DIAG_FORGOT_PASSWORD = "ForgotFragment.Dialog.DIAG_FORGOT_PASSWORD";
    private static final int REQ_TRY_AGAIN = 1005;
    private static final String DIAG_TRY_AGAIN = "ForgotFragment.Dialog.DIAG_TRY_AGAIN";
    private static final String DIAG_PROGRESS = "ForgotFragment.Dialog.DIAG_PROGRESS";
    private Callbacks mCallbacks;

    private MyTextView tvHeader;
    private MyTextView tvLabelHint;
    private MyTextView tvBtnSendLink;
    private AlertBottomSheetDialogFragment progressDialog;

    private TextInputLayout tilOldPassword,tilNewPassword,tilConfirmPassword;
    private MyEditText etOldPassword,etNewPassword,etConfirmPassword;

    public static ChangePassword newInstance() {

        Bundle args = new Bundle();

        ChangePassword fragment = new ChangePassword();
        fragment.setArguments(args);
        return fragment;
    }

    public ChangePassword() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_change_password, container, false);

        findViews(rootView);

        setViews();

        return rootView;
    }

    private void setViews() {
        etOldPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    tilOldPassword.setError(null);
                }
            }
        });
        etNewPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    tilNewPassword.setError(null);
                }
            }
        });
        etConfirmPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    tilConfirmPassword.setError(null);
                }
            }
        });


        tvBtnSendLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
                } catch (Exception e) {
                    //do nothing
                }

                boolean valid = validatePassword() &&
                        validateNewPassword() &&
                        validateConfirmPassword();

                if (valid) {

                    resetPassword(etOldPassword.getText().toString().trim(),etConfirmPassword.getText().toString());
                }
            }
        });

/*
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener( new View.OnKeyListener()
        {
            @Override
            public boolean onKey( View v, int keyCode, KeyEvent event )
            {
                if( keyCode == KeyEvent.KEYCODE_BACK )
                {

                }
                return false;
            }
        } );
*/

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

   /* private void resetPassword(String username) {

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

                                    dialogFragment.setTargetFragment(ChangePassword.this, REQ_FORGOT_PASSWORD);
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

                                    dialogFragment.setTargetFragment(ChangePassword.this, REQ_TRY_AGAIN);
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
    }*/

    private void resetPassword(String oldPassword,String newPassword) {
        /*
         *
         * Here username is Email Id or Username
         *
         * */

        showProgressDialog();

        CallGenerator.changePassword(oldPassword,newPassword)
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
                                            "\"Password Changed Successfully",
                                            "Sign In",
                                            null,
                                            false
                                    );
                                    dialogFragment.setTargetFragment(ChangePassword.this, REQ_FORGOT_PASSWORD);
                                    dialogFragment.show(getFragmentManager(), DIAG_FORGOT_PASSWORD);
                                }

                            } catch (Exception e) {
                                Log.w(LOG_TAG, G.net.changePassword.TAG + G.tag.RESPONSE + "Parse response Error: " + response.body(), e);
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
                                case G.net.changePassword.error.INTERNAL_SERVER_ERROR:
                                    Snackbar.make(tvBtnSendLink, "! Server ran into trouble.", Snackbar.LENGTH_LONG).show();
                                    break;
                                case G.net.changePassword.error.INVALID_PASSWORD_FORMAT:
                                    tilOldPassword.setError("! Invalid Password");
                                    break;
                                case G.net.changePassword.error.NOT_AUTHORISED:
                                    tilOldPassword.setError("! Incorrect Password Id");
                                    break;
                                case G.net.changePassword.error.EMPTY_PARAMS:
                                    Snackbar.make(tvBtnSendLink, "! App ran into trouble.", Snackbar.LENGTH_LONG).show();
                                    break;
                                default:
                                    Snackbar.make(tvBtnSendLink, "! Server ran into trouble.", Snackbar.LENGTH_LONG).show();
                                    break;
                            }
                            Log.w(LOG_TAG, G.net.changePassword.TAG + G.tag.ERROR + "Error Response code : " + response.code() + " Response Body:\n" +
                                    new GsonBuilder()
                                            .setPrettyPrinting()
                                            .create()
                                            .toJson(errorMessage));
                        }
                    }

                    @Override
                    public void onFailure(Call<ResMessage> call, Throwable t) {
                        Log.e(LOG_TAG, G.net.changePassword.TAG + G.tag.FAILED + "Failed API Call : ", t);

                        hideProgressDialog();
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_FORGOT_PASSWORD) {

            switch (resultCode) {
                case AlertBottomSheetDialogFragment.RESULT_PRIMARY:
                    mCallbacks.onChangePassword();
                    break;
                default:
                    mCallbacks.onChangePassword();
            }

        } else if (requestCode == REQ_TRY_AGAIN) {
            switch (resultCode) {
                case AlertBottomSheetDialogFragment.RESULT_PRIMARY:
                    tvBtnSendLink.performClick();
                    break;
                default:
                    mCallbacks.onChangePassword();
            }
        }

    }

    private void findViews(View rootView) {
        tvHeader = (MyTextView) rootView.findViewById(R.id.tvHeader);
        tvLabelHint = (MyTextView) rootView.findViewById(R.id.tvLabelHint);
        tvBtnSendLink = (MyTextView) rootView.findViewById(R.id.tvBtnSendLink);

        tilOldPassword = (TextInputLayout)rootView.findViewById(R.id.tilOldPassword);
        tilNewPassword= (TextInputLayout)rootView.findViewById(R.id.tilNewPassword);
        tilConfirmPassword= (TextInputLayout)rootView.findViewById(R.id.tilConfirmPassword);
        etOldPassword = (MyEditText) rootView.findViewById(R.id.etOldPassword);
        etNewPassword= (MyEditText) rootView.findViewById(R.id.etNewPassword);
        etConfirmPassword= (MyEditText) rootView.findViewById(R.id.etConfirmPassword);
        tvLabelHint = (MyTextView) rootView.findViewById(R.id.tvLabelHint);
        tvBtnSendLink = (MyTextView)rootView.findViewById(R.id.tvBtnSendLink);

    }

    private boolean validatePassword() {
        String text = etOldPassword.getText().toString().trim();
        if (TextUtils.isEmpty(text)) {
            tilOldPassword.setError("! Empty Password");
            return false;
        } else {
            tilOldPassword.setError(null);
            return true;
        }
    }
    private boolean validateNewPassword() {
        String text = etNewPassword.getText().toString().trim();
        if (TextUtils.isEmpty(text)) {
            tilNewPassword.setError("! Empty Password");
            return false;
        } else if(text.length()<6)
        {
            tilNewPassword.setError("! Minimum six characters");
            return false;
        }
        else {
            tilNewPassword.setError(null);
            return true;
        }
    }
    private boolean validateConfirmPassword() {
        String textOld = etConfirmPassword.getText().toString().trim();
        String textNew = etNewPassword.getText().toString().trim();
        if (TextUtils.isEmpty(textOld)) {
            tilConfirmPassword.setError("! Empty Password");
            return false;
        } else if(!textNew.equals(textOld))
        {
            tilConfirmPassword.setError("! Password not matched");
            return false;
        }
        else {
            tilConfirmPassword.setError(null);
            return true;
        }
    }
    public interface Callbacks {

        void onChangePassword();

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
