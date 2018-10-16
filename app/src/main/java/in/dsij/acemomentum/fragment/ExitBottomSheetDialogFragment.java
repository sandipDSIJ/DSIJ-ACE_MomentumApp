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

import in.dsij.acemomentum.G;
import in.dsij.acemomentum.R;
import in.dsij.acemomentum.view.MyTextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExitBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private Callbacks mCallbacks;

    private ImageView ivClose;
    private MyTextView tvMessage;
    private MyTextView tvBtnSecondary;
    private MyTextView tvBtnPrimary;
    private LinearLayout llActionButtons;

    public static ExitBottomSheetDialogFragment newInstance() {

        Bundle args = new Bundle();

        ExitBottomSheetDialogFragment fragment = new ExitBottomSheetDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public ExitBottomSheetDialogFragment() {
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

        tvMessage.setText("Do you want to exit ACEMomentum?");

        tvBtnPrimary.setText("Exit");

        tvBtnPrimary.setOnClickListener(new View.OnClickListener() {
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
