package in.dsij.acemomentum.fragment;


import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

import in.dsij.acemomentum.R;
import in.dsij.acemomentum.view.MyAutoCompleteTextView;
import in.dsij.acemomentum.view.MyTextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class RateDialogFragment extends DialogFragment {

    /*public static final int MODE_FIRST_TIME = 1;
    public static final int MODE_SECOND_TIME = 2;
    private static final String ARG_MODE = "RateDialogFragment.Arg.Mode";
    private static final String LOG_TAG = "RateDialogFragment";

    private String title;
    private String msg;

    private int mode;

    View rootView;
    private MyTextView tvTitle;
    private LinearLayout llRate;
    private MyTextView tvMsg;
    private RatingBar ratingBar;
    private MyAutoCompleteTextView etComment;
    private MyTextView tvPlayStore;
    private MyTextView tvBtnNever;
    private MyTextView tvBtnNotNow;
    private MyTextView tvBtnContinue;
    private MyTextView tvBtnDone;
    private MyTextView tvBtnPlayStore;

    private Callbacks mCallbacks;

    public static RateDialogFragment newInstance(int mode) {

        Bundle args = new Bundle();
        args.putInt(ARG_MODE, mode);
        RateDialogFragment fragment = new RateDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public RateDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        mode = args.getInt(ARG_MODE);
        this.setCancelable(false);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_rate_dialog, null);

        tvTitle = (MyTextView) rootView.findViewById(R.id.tvTitle);
        llRate = (LinearLayout) rootView.findViewById(R.id.llRate);
        tvMsg = (MyTextView) rootView.findViewById(R.id.tvMsg);
        tvPlayStore = (MyTextView) rootView.findViewById(R.id.tvPlayStore);

        ratingBar = (RatingBar) rootView.findViewById(R.id.ratingBar);

        etComment = (MyAutoCompleteTextView) rootView.findViewById(R.id.etComment);

        tvBtnNever = (MyTextView) rootView.findViewById(R.id.tvBtnNever);
        tvBtnNotNow = (MyTextView) rootView.findViewById(R.id.tvBtnNotNow);

        tvBtnContinue = (MyTextView) rootView.findViewById(R.id.tvBtnContinue);
        tvBtnDone = (MyTextView) rootView.findViewById(R.id.tvBtnDone);
        tvBtnPlayStore = (MyTextView) rootView.findViewById(R.id.tvBtnPlayStore);

        tvTitle.setVisibility(View.GONE);
        etComment.setVisibility(View.GONE);
        tvPlayStore.setVisibility(View.GONE);

        tvBtnPlayStore.setVisibility(View.GONE);
        tvBtnDone.setVisibility(View.GONE);

        if (mode == MODE_FIRST_TIME) {
            tvBtnNever.setVisibility(View.GONE);
        } else {
            tvBtnNever.setVisibility(View.VISIBLE);
        }

        tvBtnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int rating = (int)ratingBar.getRating();
                tvBtnNotNow.setText("Cancel");
                tvBtnNever.setVisibility(View.GONE);
                ratingBar.setIsIndicator(true);

                Log.d(LOG_TAG,"Num Stars : "+rating);


                tvBtnContinue.setVisibility(View.GONE);
                if (rating >= 4) {
                    tvPlayStore.setVisibility(View.VISIBLE);

                    tvBtnPlayStore.setVisibility(View.VISIBLE);
                    tvBtnDone.setVisibility(View.GONE);
                } else {
                    etComment.setVisibility(View.VISIBLE);

                    tvBtnDone.setVisibility(View.VISIBLE);
                    tvBtnPlayStore.setVisibility(View.GONE);
                }
            }
        });

        tvBtnPlayStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallbacks.rateGotoPlayStore();
                dismiss();
            }
        });

        tvBtnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallbacks.rateUnderrated((int)ratingBar.getRating(), etComment.getText().toString());
                dismiss();
            }
        });

        tvBtnNever.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallbacks.rateNever();
                dismiss();
            }
        });

        tvBtnNotNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallbacks.rateNotNow();
                dismiss();
            }
        });

        return new AlertDialog.Builder(getActivity())
                .setView(rootView)
                .create();
    }

    public interface Callbacks {
        void rateNotNow();

        void rateNever();

        void rateGotoPlayStore();

        void rateUnderrated(int stars, String comments);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }*/
}