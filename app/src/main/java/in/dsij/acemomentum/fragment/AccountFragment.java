package in.dsij.acemomentum.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import in.dsij.acemomentum.R;
import in.dsij.acemomentum.db.DbUser;
import in.dsij.acemomentum.view.MyTextView;
import io.realm.Realm;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {

    private Callbacks mCallbacks;

    private Realm realm;

    private DbUser mDbUser;

    private MyTextView tvHeader;
    private MyTextView tvProfileTitles;
    private MyTextView tvName;
    private MyTextView tvEmail;
    private MyTextView tvPhone;
    private LinearLayout llNotifications;
    private LinearLayout llAboutUs;
    private LinearLayout llContactUs;
    private LinearLayout llTerms;
    private LinearLayout llPrivacy;
    private LinearLayout llLogout;

    private SwitchCompat notificationToggle;

    public static AccountFragment newInstance() {

        Bundle args = new Bundle();

        AccountFragment fragment = new AccountFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_account, container, false);

        findViews(rootView);

        setViews();

        return rootView;
    }

    private void findViews(View rootView) {
        tvHeader = (MyTextView) rootView.findViewById(R.id.tvHeader);
        tvProfileTitles = (MyTextView) rootView.findViewById(R.id.tvProfileTitles);
        tvName = (MyTextView) rootView.findViewById(R.id.tvName);
        tvEmail = (MyTextView) rootView.findViewById(R.id.tvEmail);
        tvPhone = (MyTextView) rootView.findViewById(R.id.tvPhone);
        llNotifications = (LinearLayout) rootView.findViewById(R.id.llNotifications);
        llAboutUs = (LinearLayout) rootView.findViewById(R.id.llAboutUs);
        llContactUs = (LinearLayout) rootView.findViewById(R.id.llContactUs);
        llTerms = (LinearLayout) rootView.findViewById(R.id.llTerms);
        llPrivacy = (LinearLayout) rootView.findViewById(R.id.llPrivacy);
        llLogout = (LinearLayout) rootView.findViewById(R.id.llLogout);

        notificationToggle = (SwitchCompat) rootView.findViewById(R.id.notificationToggle);
    }

    private void setViews() {
        mDbUser = realm.where(DbUser.class).findFirst();

        String displayName = mDbUser.getDisplayName();
        String[] names = displayName.split(" ");

        String profileTitles;
        try {
            profileTitles = String.valueOf(names[0].charAt(0)) + String.valueOf(names[1].charAt(0));
        } catch (Exception e) {
            profileTitles = String.valueOf(displayName.charAt(0));
        }

        tvProfileTitles.setText(profileTitles);

        tvName.setText(displayName);

        String username = mDbUser.getUsername();
        tvEmail.setText(username);

        String email = mDbUser.getEmail();
        if (!username.equals(email)) {
            tvPhone.setText(email);
        } else {
            tvPhone.setVisibility(View.INVISIBLE);
        }

        llNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                notificationToggle.setChecked(!notificationToggle.isChecked());

            }
        });

        SharedPreferences sharedPref = getContext().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        boolean enabled = sharedPref.getBoolean(getString(R.string.enable_notifications), false);

        notificationToggle.setChecked(enabled);

        notificationToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                saveNotificationPreference(b);

            }
        });

        llAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallbacks.showAbout();
            }
        });

        llContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallbacks.showContactUs();
            }
        });

        llTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallbacks.showTerms();
            }
        });

        llPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallbacks.showPrivacy();
            }
        });

        llLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallbacks.makeLogoutCall();
            }
        });

    }

    private void saveNotificationPreference(boolean enabled) {
        SharedPreferences sharedPref = getContext().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(getString(R.string.enable_notifications), enabled);
        editor.apply();
    }

    public interface Callbacks {

        void makeLogoutCall();

        void showPrivacy();

        void showTerms();

        void showAbout();

        void showContactUs();

        void onClickUpdateDetails();

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

}
