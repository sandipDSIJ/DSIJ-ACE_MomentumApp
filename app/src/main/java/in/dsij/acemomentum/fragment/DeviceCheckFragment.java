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
 * NOT GOING TOBE USED
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
public class DeviceCheckFragment extends Fragment {

    private Callbacks mCallbacks;

    private MyTextView tvLabelForgotPassword;
    private UnderlinedTextView tvOk;
    private UnderlinedTextView tvCancel;

    public static DeviceCheckFragment newInstance() {

        Bundle args = new Bundle();

        DeviceCheckFragment fragment = new DeviceCheckFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public DeviceCheckFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_device_check, container, false);

        findViews(rootView);

        setViews();

        return rootView;
    }

    private void setViews() {

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallbacks.logoutFromOthers();
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallbacks.onClickSignIn();
            }
        });

    }

    private void findViews(View rootView) {
        tvLabelForgotPassword = (MyTextView) rootView.findViewById(R.id.tvLabelForgotPassword);
        tvOk = (UnderlinedTextView) rootView.findViewById(R.id.tvOk);
        tvCancel = (UnderlinedTextView) rootView.findViewById(R.id.tvCancel);
    }


    public interface Callbacks {
        void logoutFromOthers();

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
