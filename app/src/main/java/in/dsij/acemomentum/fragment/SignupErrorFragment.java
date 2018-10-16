package in.dsij.acemomentum.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.dsij.acemomentum.R;
import in.dsij.acemomentum.view.MyTextView;
import in.dsij.acemomentum.view.UnderlinedTextView;

/**
 * A simple {@link Fragment} subclass.
 *//*
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 * NOT GOING TO BE USED
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 * */
public class SignupErrorFragment extends Fragment {

    private Callbacks mCallbacks;

    private MyTextView tvLabelSignUpError;
    private UnderlinedTextView tvSignIn;
    private UnderlinedTextView tvReset;
    private UnderlinedTextView tvSignUp;

    public static SignupErrorFragment newInstance() {

        Bundle args = new Bundle();

        SignupErrorFragment fragment = new SignupErrorFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public SignupErrorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_signup_error, container, false);

        findViews(rootView);

        setViews();

        return rootView;
    }

    private void setViews() {
        tvReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallbacks.onClickResetPassword();
            }
        });

        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallbacks.onClickSignIn();
            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallbacks.onClickSignUp();
            }
        });

    }

    private void findViews(View rootView) {
        tvLabelSignUpError = (MyTextView)rootView.findViewById( R.id.tvLabelSignUpError );
        tvSignIn = (UnderlinedTextView)rootView.findViewById( R.id.tvSignIn );
        tvReset = (UnderlinedTextView)rootView.findViewById( R.id.tvReset );
        tvSignUp = (UnderlinedTextView)rootView.findViewById( R.id.tvSignUp );
    }

    public interface Callbacks {
        void onClickSignUp();

        void onClickSignIn();

        void onClickResetPassword();
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
