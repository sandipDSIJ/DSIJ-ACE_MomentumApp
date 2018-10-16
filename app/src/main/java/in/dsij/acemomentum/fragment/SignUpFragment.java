package in.dsij.acemomentum.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
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
public class SignUpFragment extends Fragment {

    private static final String LOG_TAG = "SignUpFragment";
    private static final int REQ_SIGNED_UP = 1003;
    private static final String DIAG_SIGNED_UP = "SignUpFragment.Dialog.DIAG_SIGNED_UP";
    private static final String DIAG_PROGRESS = "SignUpFragment.Dialog.DIAG_PROGRESS";
    private Callbacks mCallbacks;

    private MyTextView tvHeader;
    private TextInputLayout tilFirstName;
    private MyEditText etFirstName;
    private TextInputLayout tilLastName;
    private MyEditText etLastName;
    private TextInputLayout tilEmail;
    private MyEditText etEmail;
    private TextInputLayout tilUsername;
    private MyEditText etUsername;
    private TextInputLayout tilPhone;
    private MyEditText etPhone;
    private MyTextView tvLabelTerms;
    private MyTextView tvPrivacy;
    private MyTextView tvLabelAnd;
    private MyTextView tvTerms;
    private MyTextView tvBtnSignUp;
    private MyTextView tvBtnSignIn;
    private AlertBottomSheetDialogFragment progressDialog;


    public static SignUpFragment newInstance() {

        Bundle args = new Bundle();

        SignUpFragment fragment = new SignUpFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sign_up, container, false);

        findViews(rootView);

        setViews();

        return rootView;
    }

    private void setViews() {

        etFirstName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    tilFirstName.setError(null);
                }
            }
        });

        etLastName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    tilLastName.setError(null);
                }
            }
        });

        etEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    tilEmail.setError(null);
                }
            }
        });

        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                etUsername.setText(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    tilPhone.setError(null);
                }
            }
        });

        tvBtnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallbacks.onClickSignIn();
            }
        });

        tvBtnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
                } catch (Exception e) {
                    //do nothing
                }

                boolean valid =
                        validateFirstName() &&
                                validateLastName() &&
                                validateEmail() &&
                                validatePhone();

                if (valid) {
                    signUp(etFirstName.getText().toString().trim(),
                            etLastName.getText().toString().trim(),
                            etEmail.getText().toString().trim(),
                            etPhone.getText().toString().trim());


//                    mCallbacks.signUp();
                }
            }
        });

        tvPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallbacks.showPrivacy();
            }
        });

        tvTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallbacks.showTerms();
            }
        });
    }

    private void showProgressDialog() {

        hideProgressDialog();

        progressDialog = AlertBottomSheetDialogFragment.newInstance("Signing up...", null, null, true);

        progressDialog.show(getFragmentManager(),DIAG_PROGRESS);
    }

    private void hideProgressDialog() {
        try {
            progressDialog.dismiss();
        } catch (Exception e) {
            //
        }
    }

    private void signUp(String firstName, String lastName, String email, String phone) {

        showProgressDialog();

        CallGenerator.signUp(firstName,lastName,email,phone)
                .enqueue(new Callback<ResMessage>() {
                    @Override
                    public void onResponse(Call<ResMessage> call, Response<ResMessage> response) {
                        if (response.isSuccessful()) {
                            try {
                                ResMessage resMessage = response.body();

                                Log.v(LOG_TAG, G.net.signUp.TAG +
                                        G.tag.RESPONSE +
                                        new GsonBuilder()
                                                .setPrettyPrinting()
                                                .create()
                                                .toJson(resMessage));

                                if (resMessage == null) {
                                    Log.w(LOG_TAG, G.net.signUp.TAG +
                                            G.tag.RESPONSE + "Received NULL Response for SignUp");
                                } else {

                                    hideProgressDialog();

                                    AlertBottomSheetDialogFragment dialogFragment;
                                    dialogFragment = AlertBottomSheetDialogFragment.newInstance(
                                            "Signed up!\nPassword has been sent to your email id.",
                                            "Sign In",
                                            null,
                                            false
                                    );

                                    dialogFragment.setTargetFragment(SignUpFragment.this, REQ_SIGNED_UP);
                                    dialogFragment.show(getFragmentManager(), DIAG_SIGNED_UP);

                                }

                            } catch (Exception e) {
                                Log.w(LOG_TAG, G.net.signUp.TAG + G.tag.RESPONSE + "Parse response Error: " + response.body(), e);
                            }
                        }
                        else if (response.code() == 403 || response.code() == 404 || response.code() == 500 || response.code() == 503) {
                            Snackbar.make(tvBtnSignUp, "Sorry, the server is currently\n unavailable please try agein later", Snackbar.LENGTH_SHORT).show();
                            return;
                        }
                        else {

                            hideProgressDialog();

                            ResMessage errorMessage = null;

                            try {
                                String errorRes = response.errorBody().string();

                                errorMessage = new GsonBuilder().create().fromJson(errorRes, ResMessage.class);

                            } catch (IOException e) {
                                //
                            }

                            switch (response.code()) {
                                case G.net.signUp.error.INTERNAL_SERVER_ERROR:

                                    Snackbar.make(tvBtnSignUp, "! Server ran into trouble.", Snackbar.LENGTH_LONG).show();

                                    break;
                                case G.net.signUp.error.EMAIL_ALREADY_EXISTS:
                                    tilEmail.setError("! Email Already Registered");

                                    break;
                                case G.net.signUp.error.ERROR_IN_SENDING_MAIL:

                                    Snackbar.make(tvBtnSignUp, "! Failed to sent email, please check email id & Try again.", Snackbar.LENGTH_LONG).show();

                                    // TODO: 9/13/2017 Show Dialog
                                    break;
                                case G.net.signUp.error.EMPTY_PARAMS:
                                    Snackbar.make(tvBtnSignUp, "! App ran into trouble.", Snackbar.LENGTH_LONG).show();

                                    // TODO: 9/13/2017 Reload Fragment

                                    break;
                                default:
                                    Snackbar.make(tvBtnSignUp, "! Server ran into trouble.", Snackbar.LENGTH_LONG).show();
                                    break;
                            }
                            Log.w(LOG_TAG, G.net.signUp.TAG + G.tag.ERROR + "Error Response code : " + response.code() + " Response Body:\n" +
                                    new GsonBuilder()
                                            .setPrettyPrinting()
                                            .create()
                                            .toJson(errorMessage));
                        }
                    }

                    @Override
                    public void onFailure(Call<ResMessage> call, Throwable t) {
                        Log.e(LOG_TAG, G.net.signUp.TAG + G.tag.FAILED + "Failed API Call : ", t);
                        hideProgressDialog();
                        if (!MyApplication.isConnected()) {
                            Snackbar.make(tvBtnSignUp, "!Check your Internet connection", Snackbar.LENGTH_LONG).show();
                        } else
                        {
                            Snackbar.make(tvBtnSignUp, "! Server ran into trouble.", Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_SIGNED_UP) {

            switch (resultCode) {
                case AlertBottomSheetDialogFragment.RESULT_PRIMARY:
                    mCallbacks.onClickSignIn();
                    break;
                default:
                    mCallbacks.onClickSignIn();
            }

        }

    }

    private boolean validateEmail() {
        String email = etEmail.getText().toString().trim();
        if (TextUtils.isEmpty(email) || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError("! Invalid Email");
            return false;
        } else {
            tilEmail.setError(null);
            return true;
        }
    }

    private boolean validateFirstName() {
        String name = etFirstName.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            tilFirstName.setError("! Empty Name");
            return false;
        } else {
            tilFirstName.setError(null);
            return true;
        }
    }

    private boolean validateLastName() {
        String name = etLastName.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            tilLastName.setError("! Empty Last Name");
            return false;
        } else {
            tilLastName.setError(null);
            return true;
        }
    }

    private boolean validatePhone() {
        String phone = etPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone) || !Patterns.PHONE.matcher(phone).matches() || phone.length()<10) {
            tilPhone.setError("! Invalid Phone");
            return false;
        } else {
            tilPhone.setError(null);
            return true;
        }
    }

    private void findViews(View rootView) {
        tvHeader = (MyTextView) rootView.findViewById(R.id.tvHeader);
        tilFirstName = (TextInputLayout) rootView.findViewById(R.id.tilFirstName);
        etFirstName = (MyEditText) rootView.findViewById(R.id.etFirstName);
        tilLastName = (TextInputLayout) rootView.findViewById(R.id.tilLastName);
        etLastName = (MyEditText) rootView.findViewById(R.id.etLastName);
        tilEmail = (TextInputLayout) rootView.findViewById(R.id.tilEmail);
        etEmail = (MyEditText) rootView.findViewById(R.id.etEmail);
        tilUsername = (TextInputLayout) rootView.findViewById(R.id.tilUsername);
        etUsername = (MyEditText) rootView.findViewById(R.id.etUsername);
        tilPhone = (TextInputLayout) rootView.findViewById(R.id.tilPhone);
        etPhone = (MyEditText) rootView.findViewById(R.id.etPhone);
        tvLabelTerms = (MyTextView) rootView.findViewById(R.id.tvLabelTerms);
        tvPrivacy = (MyTextView) rootView.findViewById(R.id.tvPrivacy);
        tvLabelAnd = (MyTextView) rootView.findViewById(R.id.tvLabelAnd);
        tvTerms = (MyTextView) rootView.findViewById(R.id.tvTerms);
        tvBtnSignUp = (MyTextView) rootView.findViewById(R.id.tvBtnSignUp);
        tvBtnSignIn = (MyTextView) rootView.findViewById(R.id.tvBtnSignIn);
    }

    public interface Callbacks {

        void onClickSignIn();

        void showPrivacy();

        void showTerms();
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
