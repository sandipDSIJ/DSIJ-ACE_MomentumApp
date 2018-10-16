package in.dsij.acemomentum.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import in.dsij.acemomentum.R;
import in.dsij.acemomentum.view.MyTextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class OfflineBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private Callbacks mCallbacks;

    private ImageView ivClose;
    private MyTextView tvMessage;
    private MyTextView tvBtnSecondary;
    private MyTextView tvBtnPrimary;
    private LinearLayout llActionButtons;

    public static OfflineBottomSheetDialogFragment newInstance() {

        Bundle args = new Bundle();

        OfflineBottomSheetDialogFragment fragment = new OfflineBottomSheetDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public OfflineBottomSheetDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onStart() {
        super.onStart();

        setViews();
    }

    private void setViews() {

        tvMessage.setText("You are offline.\nPlease connect to the internet.");

        tvBtnPrimary.setText("Settings");

        tvBtnPrimary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallbacks.openSettings();
                dismiss();
            }
        });

        tvBtnSecondary.setText("Exit");

        tvBtnSecondary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallbacks.exitApp();
                dismiss();
            }
        });

        ivClose.setVisibility(View.VISIBLE);
        setCancelable(true);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_alert_bottom_sheet_dialog, container, false);

        findViews(rootView);

        return rootView;
    }

    private void findViews(View rootView) {
        ivClose = (ImageView) rootView.findViewById(R.id.ivClose);
        tvMessage = (MyTextView) rootView.findViewById(R.id.tvMessage);
        tvBtnSecondary = (MyTextView) rootView.findViewById(R.id.tvBtnSecondary);
        tvBtnPrimary = (MyTextView) rootView.findViewById(R.id.tvBtnPrimary);
        llActionButtons = (LinearLayout) rootView.findViewById(R.id.llActionButtons);
    }

    public interface Callbacks{
        void exitApp();
        void openSettings();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks= (Callbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks=null;
    }
}
