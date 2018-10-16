package in.dsij.acemomentum.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.dsij.acemomentum.R;
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
 *
 *
 * */
public class ResetPasswordConfirmationFragment extends Fragment {

    private static final String ARG_EMAIL = "ResetPasswordConfirmationFragment.Arg.ARG_EMAIL";

    private Callbacks mCallbacks;

    private UnderlinedTextView tvSignIn;

    public static ResetPasswordConfirmationFragment newInstance() {

        Bundle args = new Bundle();

        ResetPasswordConfirmationFragment fragment = new ResetPasswordConfirmationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ResetPasswordConfirmationFragment newInstance(String email) {

        Bundle args = new Bundle();
        args.putString(ARG_EMAIL,email);
        ResetPasswordConfirmationFragment fragment = new ResetPasswordConfirmationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public ResetPasswordConfirmationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_reset_password, container, false);

        findViews(rootView);

        setViews();

        return rootView;
    }

    private void setViews() {
        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallbacks.onClickSignIn();
            }
        });
    }

    private void findViews(View rootView) {
        tvSignIn = (UnderlinedTextView)rootView.findViewById( R.id.tvSignIn );
    }

    public interface Callbacks {

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
