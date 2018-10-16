package in.dsij.acemomentum.fragment;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import in.dsij.acemomentum.G;
import in.dsij.acemomentum.MyApplication;
import in.dsij.acemomentum.R;
import in.dsij.acemomentum.db.DbUser;
import in.dsij.acemomentum.net.CallGenerator;
import in.dsij.acemomentum.net.res.ResLogin;
import in.dsij.acemomentum.net.res.ResMessage;
import io.realm.Realm;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SplashFragment extends Fragment {

    private static final String LOG_TAG = "SplashFragment";
    private static final int REQ_LOGOUT_FROM_OTHERS = 1001;
    private static final String DIAG_LOGOUT_FROM_OTHERS = "SplashFragment.Dialog.Tag.DIAG_LOGOUT_FROM_OTHERS";
    private static final int REQ_NOT_AUTHORISED = 1002;
    private static final String DIAG_NOT_AUTHORISED = "SplashFragment.Dialog.Tag.DIAG_NOT_AUTHORISED";
    private static final int REQ_UPDATE = 1003;
    private static final String DIAG_UPDATE = "SplashFragment.Dialog.Tag.DIAG_UPDATE";
    private Callbacks mCallbacks;

    private Realm realm;
    private int loginResponseCode;
    private View rootView;

    public static SplashFragment newInstance() {

        Bundle args = new Bundle();

        SplashFragment fragment = new SplashFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public SplashFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_splash, container, false);

        rootView = inflater.inflate(R.layout.fragment_splash, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DbUser dbUser = realm.where(DbUser.class).findFirst();

       checkVersion();

    }

    private void checkVersion() {

        if (MyApplication.isConnected()) {
            CallGenerator.checkVersion(G.device.VERSION_CODE).enqueue(
                    new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                try {
                                    /*String resString = response.body().string();*/
                                    loginResponseCode = response.code();
                                    switch (response.code()) {
                                        case 200:
                                            handleLoginResponseCode();
                                            break;
                                        case 201:
                                            handleLoginResponseCode();
                                            break;
                                        case 202:
                                            handleLoginResponseCode();
                                            break;
                                    }

                                    /* Log.d(LOG_TAG, resString);*/
                                } catch (Exception e) {
                                    //showUpdateDialog("sds",true);
                                    Log.e(LOG_TAG, "Parse Response Error", e);
                                }
                            } else if (response.code() == 403 || response.code() == 404 || response.code() == 500 || response.code() == 503) {
                                // String strError = response.errorBody().toString();
                                Snackbar.make(rootView, "Sorry, the server is currently\n unavailable please try agein later", Snackbar.LENGTH_LONG).show();
                                return;
                            }
                            else {
                                switch (response.code()) {
                                    case 400:
                                        break;
                                    case 401:
                                        break;
                                    case 402:
                                        break;
                                    case 403:
                                        break;
                                    case 500:
                                        break;
                                    default:
                                        break;
                                }

                                Log.e("Retrofit", "Error Response code : " + response.code());
                                handleLoginResponseCode();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.e(LOG_TAG, G.tag.ERROR + "Check Version", t);
                            Snackbar.make(rootView, "Sorry, the server is currently\n unavailable please try agein later", Snackbar.LENGTH_LONG).show();
                            handleLoginResponseCode();
                        }
                    }
            );
        }
        else {
            Snackbar.make(rootView, "!Check your Internet connection", Snackbar.LENGTH_LONG).show();
        }
    }

    private void makeLoginWithTokenCall(@NonNull final DbUser dbUser) {

        CallGenerator.loginWithToken(dbUser.getUsername(), dbUser.getSessionToken())
                .enqueue(new Callback<ResLogin>() {
                    @Override
                    public void onResponse(Call<ResLogin> call, Response<ResLogin> response) {
                        if (response.isSuccessful()) {
                            try {
                                ResLogin resLogin = response.body();

                                Log.v(LOG_TAG, G.net.loginWithToken.TAG +
                                        G.tag.RESPONSE +
                                        new GsonBuilder()
                                                .setPrettyPrinting()
                                                .create()
                                                .toJson(resLogin));

                                loginResponseCode = response.code();

                                if (resLogin == null) {
                                    Log.w(LOG_TAG, G.net.loginWithToken.TAG +
                                            G.tag.RESPONSE + "Received NULL User");
                                } else {
                                    //If Some of user data is changed
                                    insertUser(resLogin);
                                }

                            } catch (Exception e) {
                                Log.w(LOG_TAG, G.net.loginWithToken.TAG + G.tag.RESPONSE + "Parse response Error: " + response.body(), e);
                            }
                        }
                        else if (response.code() == 403 || response.code() == 404 || response.code() == 500 || response.code() == 503) {
                            // String strError=response.errorBody().toString();
                            Snackbar.make(rootView, "Sorry, the server is currently\n unavailable please try agein later", Snackbar.LENGTH_LONG).show();
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

                            View.OnClickListener retryClickListener = new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    makeLoginWithTokenCall(dbUser);
                                }
                            };

                            AlertBottomSheetDialogFragment dialogFragment;

                            switch (response.code()) {
                                case G.net.loginWithToken.error.INTERNAL_SERVER_ERROR:
                                    /*Snackbar.make(drawer, "!Server ran into trouble.", Snackbar.LENGTH_LONG)
                                            .setAction("Retry", retryClickListener).show();*/

                                    mCallbacks.onClickSignIn();

                                    break;

                                case G.net.loginWithToken.error.ALREADY_SIGNED_INTO_OTHER_DEVICE:

                                    dialogFragment = AlertBottomSheetDialogFragment.newInstance(
                                            "You have already signed to another device.\nDo you want to sign out from other devices and Sign in here?",
                                            "Sign In",
                                            "Exit ACEMomentum",
                                            true
                                    );
                                    dialogFragment.setTargetFragment(SplashFragment.this, REQ_LOGOUT_FROM_OTHERS);
                                    dialogFragment.show(getFragmentManager(), DIAG_LOGOUT_FROM_OTHERS);

                                    break;
                                case G.net.loginWithToken.error.TOKEN_EXPIRED:
                                    mCallbacks.onClickSignIn();
                                    break;
                                case G.net.loginWithToken.error.NOT_AUTHORISED:
                                    dialogFragment = AlertBottomSheetDialogFragment.newInstance(
                                            "You are not authorised to login into this app.\nPlease contact DSIJ Customer Support.",
                                            "Exit Trader",
                                            null,
                                            true
                                    );
                                    dialogFragment.setTargetFragment(SplashFragment.this, REQ_NOT_AUTHORISED);
                                    dialogFragment.show(getFragmentManager(), DIAG_NOT_AUTHORISED);
                                    break;
                                case G.net.loginWithToken.error.SERVER_ERROR:
                                    mCallbacks.onClickSignIn();
                                    break;
                                case G.net.loginWithToken.error.USERNAME_NOT_REGISTERED:
                                    mCallbacks.onClickSignIn();
                                    break;
                                case G.net.loginWithToken.error.EMPTY_PARAMS:
                                    mCallbacks.onClickSignIn();
                                    break;
                                default:
                                    mCallbacks.onClickSignIn();
                                    break;
                            }
                            Log.w(LOG_TAG, G.net.loginWithToken.TAG + G.tag.ERROR + "Error Response code : " + response.code() + " Response Body:\n" +
                                    new GsonBuilder()
                                            .setPrettyPrinting()
                                            .create()
                                            .toJson(errorMessage));
                        }
                    }

                    @Override
                    public void onFailure(Call<ResLogin> call, Throwable t) {
                        Snackbar.make(rootView,"! Server error", Snackbar.LENGTH_INDEFINITE)
                                .setAction("Exit", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        mCallbacks.exitApp();
                                    }
                                })
                                .show();
                    }
                });

    }

    private void insertUser(final ResLogin resLogin) {

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Log.d(LOG_TAG,
                        G.tag.DB_TRANSACTION_WRITE + "User");

                try {
                    realm.where(DbUser.class).findAll().deleteAllFromRealm();
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

                mCallbacks.loggedIn();

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

    private void handleLoginResponseCode() {

        AlertBottomSheetDialogFragment dialogFragment;

        if (loginResponseCode == 201) {
            //Not using latest version
            dialogFragment = AlertBottomSheetDialogFragment.newInstance(
                    "New version of the ACEMomentum app is available.",
                    "Update",
                    "Skip",
                    true
            );

            dialogFragment.setTargetFragment(SplashFragment.this, REQ_UPDATE);
            dialogFragment.show(getFragmentManager(), DIAG_UPDATE);

        } else if (loginResponseCode == 200) {
            //Not using minimum version
            dialogFragment = AlertBottomSheetDialogFragment.newInstance(
                    "New version of the ACEMomentum app is available.\nPlease update to continue.",
                    "Update",
                    null,
                    true
            );

            dialogFragment.setTargetFragment(SplashFragment.this, REQ_UPDATE);
            dialogFragment.show(getFragmentManager(), DIAG_UPDATE);

        } else
        {
            DbUser dbUser = realm.where(DbUser.class).findFirst();
            if (dbUser == null) {
                mCallbacks.onClickSignIn();
            } else {
                makeLoginWithTokenCall(dbUser);
            }

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_LOGOUT_FROM_OTHERS) {

            switch (resultCode) {
                case AlertBottomSheetDialogFragment.RESULT_PRIMARY:
                    mCallbacks.onClickSignIn();
                    break;
                case AlertBottomSheetDialogFragment.RESULT_SECONDARY:
                    mCallbacks.exitApp();
                    break;
                default:
                    mCallbacks.onClickSignIn();
            }

        } else if (requestCode == REQ_NOT_AUTHORISED) {
            switch (resultCode) {
                case AlertBottomSheetDialogFragment.RESULT_PRIMARY:
                    mCallbacks.exitApp();
                    break;
                default:
                    mCallbacks.exitApp();
            }
        }else if (requestCode==REQ_UPDATE){
            if (resultCode == AlertBottomSheetDialogFragment.RESULT_PRIMARY) {

                String appPackageName = getContext().getPackageName();
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }

            } else if (resultCode == AlertBottomSheetDialogFragment.RESULT_SECONDARY) {
                DbUser dbUser = realm.where(DbUser.class).findFirst();
                if (dbUser == null) {
                    mCallbacks.onClickSignIn();
                } else {
                    makeLoginWithTokenCall(dbUser);
                }
            } else if (resultCode == AlertBottomSheetDialogFragment.RESULT_CANCELED) {
                DbUser dbUser = realm.where(DbUser.class).findFirst();
                if (dbUser == null) {
                    mCallbacks.onClickSignIn();
                } else {
                    makeLoginWithTokenCall(dbUser);
                }
            } else {
                //do nothing
            }
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        realm = Realm.getDefaultInstance();
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        realm.close();
        mCallbacks = null;
    }

    public interface Callbacks {
        void onClickSignIn();

        void loggedIn();

        void exitApp();
    }
}
