package in.dsij.acemomentum.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import in.dsij.acemomentum.G;
import in.dsij.acemomentum.MyApplication;
import in.dsij.acemomentum.R;
import in.dsij.acemomentum.db.DbCall;
import in.dsij.acemomentum.db.DbProduct;
import in.dsij.acemomentum.db.DbTrackerColumn;
import in.dsij.acemomentum.db.DbTrackerRow;
import in.dsij.acemomentum.db.DbUser;
import in.dsij.acemomentum.net.CallGenerator;
import in.dsij.acemomentum.net.res.ResLogin;
import in.dsij.acemomentum.net.res.ResMessage;
import in.dsij.acemomentum.view.MyEditText;
import in.dsij.acemomentum.view.MyTextView;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    private static final String LOG_TAG = "LoginFragment";
    private static final String DIAG_PROGRESS = "LoginFragment.Dialog.DIAG_PROGRESS";
    private static final int REQ_UPDATE = 1010;
    private static final String DIAG_UPDATE = "LoginFragment.Dialog.DIAG_UPDATE";

    private Realm realm;

    private Callbacks mCallbacks;

    private MyTextView tvHeader,tvHelp;
    private ImageView ivInstruction;
    private TextInputLayout tilUsername;
    private MyEditText etUsername;
    private TextInputLayout tilPassword;
    private MyEditText etPassword;
    private MyTextView tvForgotPassword;
    private MyTextView tvBtnSignUp;
    private MyTextView tvBtnSignIn;
    private AlertBottomSheetDialogFragment progressDialog;
    private int loginResponseCode;
    private RelativeLayout top_layout;


    public static LoginFragment newInstance() {

        Bundle args = new Bundle();

        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        findViews(rootView);

        setViews();

        clearDb();

        return rootView;
    }

    private void findViews(View rootView) {

        tvHelp=(MyTextView) rootView.findViewById(R.id.tvHelp);
        ivInstruction=(ImageView) rootView.findViewById(R.id.ivInstruction);
        tvHeader = (MyTextView) rootView.findViewById(R.id.tvHeader);
        tilUsername = (TextInputLayout) rootView.findViewById(R.id.tilUsername);
        etUsername = (MyEditText) rootView.findViewById(R.id.etUsername);
        tilPassword = (TextInputLayout) rootView.findViewById(R.id.tilPassword);
        etPassword = (MyEditText) rootView.findViewById(R.id.etPassword);
        tvForgotPassword = (MyTextView) rootView.findViewById(R.id.tvForgotPassword);
        tvBtnSignUp = (MyTextView) rootView.findViewById(R.id.tvBtnSignUp);
        tvBtnSignIn = (MyTextView) rootView.findViewById(R.id.tvBtnSignIn);
        top_layout=(RelativeLayout)rootView.findViewById(R.id.top_layout);
    }

    private void clearDb() {
        realm.beginTransaction();
        realm.where(DbUser.class).findAll().deleteAllFromRealm();
        realm.where(DbCall.class).findAll().deleteAllFromRealm();
        realm.where(DbProduct.class).findAll().deleteAllFromRealm();
        realm.where(DbTrackerRow.class).findAll().deleteAllFromRealm();
        realm.where(DbTrackerColumn.class).findAll().deleteAllFromRealm();
        realm.commitTransaction();
    }

    private void setViews() {




        if (isFirstTime()) {
           /* mUserHelpDialog=UserHelpDialog.newInstance();
            mUserHelpDialog.show(getSupportFragmentManager(),DIALOG_USER_HELP);*/
            /*mMagicLinkDialog = MagicLinkDialogFragment.newInstance(email);
                mMagicLinkDialog.show(getSupportFragmentManager(), DIALOG_MAGIC_LINK);*/
            top_layout.setVisibility(View.VISIBLE);

            // llMain.setVisibility(View.VISIBLE);


        }
        /*if (isFirstTime()) {
           *//* mUserHelpDialog=UserHelpDialog.newInstance();
            mUserHelpDialog.show(getSupportFragmentManager(),DIALOG_USER_HELP);*//*
            *//*mMagicLinkDialog = MagicLinkDialogFragment.newInstance(email);
                mMagicLinkDialog.show(getSupportFragmentManager(), DIALOG_MAGIC_LINK);*//*
            topLevelLayout.setVisibility(View.VISIBLE);
           // llMain.setVisibility(View.VISIBLE);


        }*/


        etUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    tilUsername.setError(null);
                }
            }
        });

        etPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    tilPassword.setError(null);
                }
            }
        });

        tvBtnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
                } catch (Exception e) {
                    //do nothing
                }

                boolean valid =
                        validateUsername() &&
                                validatePassword();

                if (valid) {
                    String userName = etUsername.getText().toString().trim();
                    String password = etPassword.getText().toString().trim();

                    if(MyApplication.isConnected())
                        signIn(userName, password);
                    else
                    {
                        Snackbar.make(tvBtnSignIn, "!Check your Internet connection", Snackbar.LENGTH_LONG).show();
                    }
                }
            }
        });

        tvBtnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallbacks.onClickSignUp();
            }
        });

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallbacks.onClickForgotPassword();
            }
        });


        tvHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                top_layout.startAnimation(inFromRightAnimation(200));
                top_layout.setVisibility(View.VISIBLE);
            }
        });

        ivInstruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // top_layout.startAnimation(outToRightAnimation(200));
                top_layout.setVisibility(View.GONE);

                //llMain.startAnimation(inFromLeftAnimation(200));
                return;
            }
        });

    }

    private boolean isFirstTime()
    {
        SharedPreferences preferences = getActivity().getPreferences(MODE_PRIVATE);
        boolean ranBefore = preferences.getBoolean("ACE_mom_FIRST_TIME", false);
        if (!ranBefore) {
            // first time
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("ACE_mom_FIRST_TIME", true);
            editor.commit();
        }
        return !ranBefore;
    }

    private void signIn(final String userName, final String password) {

        Callback<ResLogin> loginCallback = new Callback<ResLogin>() {
            @Override
            public void onResponse(Call<ResLogin> call, Response<ResLogin> response) {
                if (response.isSuccessful()) {
                    try {
                        ResLogin resLogin = response.body();

                        Log.v(LOG_TAG, G.net.loginWithPassword.TAG +
                                G.tag.RESPONSE +
                                new GsonBuilder()
                                        .setPrettyPrinting()
                                        .create()
                                        .toJson(resLogin));

                        loginResponseCode = response.code();

                        if (resLogin == null) {
                            Log.w(LOG_TAG, G.net.loginWithPassword.TAG +
                                    G.tag.RESPONSE + "Received NULL User");
                        } else {
                            insertUser(resLogin);
                            Log.w(LOG_TAG, G.net.loginWithPassword.TAG +
                                    resLogin.toString());
                        }

                    } catch (Exception e) {
                        Log.w(LOG_TAG, G.net.loginWithPassword.TAG + G.tag.RESPONSE + "Parse response Error: " + response.body(), e);
                    }
                }
                else if (response.code() == 403 || response.code() == 404 || response.code() == 500 || response.code() == 503) {
                    // String strError=response.errorBody().toString();
                    Snackbar.make(tvBtnSignIn, "Sorry, the server is currently\n unavailable please try agein later", Snackbar.LENGTH_LONG).show();
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

                    View.OnClickListener retryClickListener = new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            signIn(userName, password);
                        }
                    };

                    switch (response.code()) {
                        case G.net.loginWithPassword.error.INTERNAL_SERVER_ERROR:

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

                            break;


                        default:
                            // TODO: 9/13/2017 Show Dialog
                            break;
                    }
                    Log.w(LOG_TAG, G.net.loginWithPassword.TAG + G.tag.ERROR + "Error Response code : " + response.code() + " Response Body:\n" +
                            new GsonBuilder()
                                    .setPrettyPrinting()
                                    .create()
                                    .toJson(errorMessage));
                }
            }

            @Override
            public void onFailure(Call<ResLogin> call, Throwable t) {
                Log.e(LOG_TAG, G.net.loginWithPassword.TAG + G.tag.FAILED + "Failed API Call : ", t);
                hideProgressDialog();
                if (!MyApplication.isConnected()) {
                    Snackbar.make(tvBtnSignIn, "!Check your Internet connection", Snackbar.LENGTH_LONG).show();
                }

                // TODO: 11/13/2017 If offline, Show dialog

            }
        };

        showProgressDialog();
        CallGenerator.loginWithPassword(userName, password)
                .enqueue(loginCallback);
    }

    private void showProgressDialog() {

        hideProgressDialog();

        progressDialog = AlertBottomSheetDialogFragment.newInstance("Signing in...", null, null, true);

        progressDialog.show(getFragmentManager(), DIAG_PROGRESS);
    }

    private void hideProgressDialog() {
        try {
            progressDialog.dismiss();
        } catch (Exception e) {
            //
        }
    }

    private void insertUser(final ResLogin resLogin) {

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Log.d(LOG_TAG,
                        G.tag.DB_TRANSACTION_WRITE + "User");

                try {
                    realm.where(DbUser.class).findAll().deleteAllFromRealm();
                    DbUser dbUser = realm.where(DbUser.class).findFirst();
                } catch (Exception e) {
                    Log.v(LOG_TAG, G.tag.DB_TRANSACTION_WRITE + "Already no User present ", e);
                } finally {
                    realm.createOrUpdateObjectFromJson(DbUser.class, new Gson().toJson(resLogin));
                }

            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.d(LOG_TAG,
                        G.tag.DB_TRANSACTION_WRITE + G.tag.DB_SUCCESS);

                registerForCloud();

                hideProgressDialog();

                handleLoginResponseCode();

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.e(LOG_TAG,
                        G.tag.DB_TRANSACTION_WRITE + G.tag.DB_FAIL,
                        error);
                hideProgressDialog();
            }
        });

    }

    private void handleLoginResponseCode() {

        AlertBottomSheetDialogFragment dialogFragment;

        if (loginResponseCode == G.net.loginWithPassword.success.NEW_VERSION_AVAILABLE) {
            //Not using latest version
            dialogFragment = AlertBottomSheetDialogFragment.newInstance(
                    "New version of the ACEMomentum app is available.",
                    "Update",
                    "Skip",
                    true
            );

            dialogFragment.setTargetFragment(LoginFragment.this, REQ_UPDATE);
            dialogFragment.show(getFragmentManager(), DIAG_UPDATE);

        } else if (loginResponseCode == G.net.loginWithPassword.success.NOT_USING_MINIMUM_VERSION) {
            //Not using minimum version
            dialogFragment = AlertBottomSheetDialogFragment.newInstance(
                    "New version of the ACEMomentum app is available.\nPlease update to continue.",
                    "Update",
                    null,
                    true
            );

            dialogFragment.setTargetFragment(LoginFragment.this, REQ_UPDATE);
            dialogFragment.show(getFragmentManager(), DIAG_UPDATE);

        } else {
            mCallbacks.loggedIn();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_UPDATE) {
            if (resultCode == AlertBottomSheetDialogFragment.RESULT_PRIMARY) {

                String appPackageName = getContext().getPackageName();
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }

            } else if (resultCode == AlertBottomSheetDialogFragment.RESULT_SECONDARY) {
                mCallbacks.loggedIn();
            } else if (resultCode == AlertBottomSheetDialogFragment.RESULT_CANCELED) {
                //do nothing
            } else {
                //do nothing
            }
        } else {
            //do nothing
        }


    }

    private void registerForCloud() {
        SharedPreferences sharedPref = getContext().getSharedPreferences(
                getString(R.string.preference_file_key), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(getString(R.string.enable_notifications), true);
        editor.apply();

//        FirebaseMessaging.getInstance().subscribeToTopic("trader_firebase_default");
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

    private boolean validatePassword() {
        Editable password = etPassword.getText();
        if (TextUtils.isEmpty(password)) {
            tilPassword.setError("! Empty Password");
            return false;
        } else {
            tilPassword.setError(null);
            return true;
        }
    }

    public interface Callbacks {

        void loggedIn();

        void onClickSignUp();

        void onClickForgotPassword();

        void showProgressDialog(String message);

        void dismissProgressDialog();

        void showOfflineDialog();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
        realm = Realm.getDefaultInstance();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
        realm.close();
    }
    public static Animation outToRightAnimation(long durationMillis) {
        Animation outtoRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,  +1.0f,
                Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,   0.0f
        );
        outtoRight.setDuration(durationMillis);
        return outtoRight;
    }

    public static Animation inFromLeftAnimation(long durationMillis) {
        Animation inFromLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT,  -1.0f, Animation.RELATIVE_TO_PARENT,  0.0f,
                Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,   0.0f
        );
        inFromLeft.setDuration(durationMillis);
        return inFromLeft;
    }

    public static Animation inFromRightAnimation(long durationMillis) {
        Animation inFromRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT,  +1.0f, Animation.RELATIVE_TO_PARENT,  0.0f,
                Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,   0.0f
        );
        inFromRight.setDuration(durationMillis);
        return inFromRight;
    }
}
