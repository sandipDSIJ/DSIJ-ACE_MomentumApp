package in.dsij.acemomentum.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.dsij.acemomentum.R;
import in.dsij.acemomentum.view.MyTextView;


public class LoadingFragment extends Fragment {

    private static final String ARG_TEXT = "LoadingFragment.Arg.ARG_TEXT";
    private MyTextView tvText;


    public static LoadingFragment newInstance(String loadingText) {
        Bundle args = new Bundle();
        args.putString(ARG_TEXT,loadingText);
        LoadingFragment fragment = new LoadingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public LoadingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_loading, container, false);

        findViews(rootView);

        setViews();

        return rootView;
    }

    private void setViews() {
        String text=getArguments().getString(ARG_TEXT);
        if (TextUtils.isEmpty(text)){
            tvText.setText("Loading...");
        }else {
            tvText.setText(text);
        }
    }

    private void findViews(View rootView) {
        tvText = (MyTextView)rootView.findViewById( R.id.tvText );
    }

}
