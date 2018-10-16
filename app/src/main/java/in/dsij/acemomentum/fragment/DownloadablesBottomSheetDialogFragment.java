package in.dsij.acemomentum.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import in.dsij.acemomentum.G;
import in.dsij.acemomentum.R;
import in.dsij.acemomentum.net.CallGenerator;
import in.dsij.acemomentum.net.res.ResGetDownloadables;
import in.dsij.acemomentum.net.res.ResMessage;
import in.dsij.acemomentum.net.res.ResPdfWithPassword;
import in.dsij.acemomentum.view.MyTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class DownloadablesBottomSheetDialogFragment extends BottomSheetDialogFragment {

    public static final int RESULT_CANCELED = 903;
    public static final int RESULT_PRIMARY = 901;
    public static final int RESULT_SECONDARY = 902;
    private static final String ARG_CALL_ID = "DownloadablesBottomSheetDialogFragment.Arg.ARG_CALL_ID";
    private static final String LOG_TAG = "DownloadablesFragment";
    int recall_flag=0;
    private int resultCode = RESULT_CANCELED;

    private ImageView ivClose;
    private RecyclerView mRecyclerView;
    private MyTextView tvMessage;

    private long mCallId;

    private ArrayList<Downloadable> mDownloadables;

    private Callbacks mCallbacks;

    public static DownloadablesBottomSheetDialogFragment newInstance(long callId) {

        Bundle args = new Bundle();
        args.putLong(ARG_CALL_ID, callId);
        DownloadablesBottomSheetDialogFragment fragment = new DownloadablesBottomSheetDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public DownloadablesBottomSheetDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Bundle args = getArguments();

        mCallId = args.getLong(ARG_CALL_ID);

        mDownloadables = new ArrayList<>();
    }

    @Override
    public void onStart() {
        super.onStart();

        getDownloadables();

    }

    private void getDownloadables() {


        CallGenerator.GetDownloadablesAcemomentum(mCallId).enqueue(new Callback<ResGetDownloadables>() {
            @Override
            public void onResponse(Call<ResGetDownloadables> call, Response<ResGetDownloadables> response) {
                if (response.isSuccessful()) {
                    try {
                        ResGetDownloadables resGetDownloadables = response.body();

                        Log.v(LOG_TAG, G.net.getDownloadables.TAG +
                                G.tag.RESPONSE +
                                new GsonBuilder()
                                        .setPrettyPrinting()
                                        .create()
                                        .toJson(resGetDownloadables));

                        if (resGetDownloadables == null) {
                            Log.w(LOG_TAG, G.net.getCalls.TAG +
                                    G.tag.RESPONSE + "Received NULL Call List");

                            showNotAvailable();

                        } else if (resGetDownloadables.getReason() == null && resGetDownloadables.getReport().isEmpty()) {
                            showNotAvailable();
                        } else {
                            showAvailableOptions(resGetDownloadables);
                        }

                    } catch (Exception e) {
                        Log.w(LOG_TAG, G.net.getCalls.TAG + G.tag.RESPONSE + "Parse response Error: " + response.body(), e);
                    }
                } else {

                    showNotAvailable();

                    ResMessage errorMessage = null;

                    try {
                        String errorRes = response.errorBody().string();

                        errorMessage = new GsonBuilder().create().fromJson(errorRes, ResMessage.class);

                    } catch (IOException e) {
                        //
                    }
                }
            }

            @Override
            public void onFailure(Call<ResGetDownloadables> call, Throwable t) {
                Log.e(LOG_TAG, G.net.getDownloadables.TAG + G.tag.FAILED + "Failed API Call : ", t);
                showNotAvailable();
            }
        });
    }
    private void getDownloadablesRefresh(final Downloadable downloadable,final Callbacks mCallbacks) {

        CallGenerator.getDownloadables(mCallId).enqueue(new Callback<ResGetDownloadables>() {
            @Override
            public void onResponse(Call<ResGetDownloadables> call, Response<ResGetDownloadables> response) {
                if (response.isSuccessful()) {
                    try {
                        ResGetDownloadables resPdfWithPassword = response.body();
                        mCallbacks.openPdf(resPdfWithPassword.getReason().getTitle(), resPdfWithPassword.getReason().getPDFLink());

                    } catch (Exception e) {
                        Log.w(LOG_TAG, G.net.getCalls.TAG + G.tag.RESPONSE + "Parse response Error: " + response.body(), e);
                    }
                } else {

                    showNotAvailable();

                    ResMessage errorMessage = null;

                    if(recall_flag==0)
                    {
                        recall_flag=1;
                        getDownloadablesRefresh(downloadable, mCallbacks);
                    }

                    try {
                        String errorRes = response.errorBody().string();

                        errorMessage = new GsonBuilder().create().fromJson(errorRes, ResMessage.class);

                    } catch (IOException e) {
                        //
                    }
                }
            }

            @Override
            public void onFailure(Call<ResGetDownloadables> call, Throwable t) {
                Log.e(LOG_TAG, G.net.getDownloadables.TAG + G.tag.FAILED + "Failed API Call : ", t);
                showNotAvailable();
            }
        });
    }

    private void showAvailableOptions(ResGetDownloadables resGetDownloadables) {
        mDownloadables.clear();

        if (!TextUtils.isEmpty(resGetDownloadables.getReason().getPDFLink())) {
            mDownloadables.add(new Downloadable()
                    .setTitle("Reason : PDF available")
                    .setLink(resGetDownloadables.getReason().getPDFLink())
                    .setSubscriptionNo(resGetDownloadables.getReason().getSubscriptionNo())
                    .setAvailable(true));
            //.setLink("http://192.168.1.154/productattachment/KYCProof/test_.pdf")
        } else {
            mDownloadables.add(new Downloadable()
                    .setTitle("Reason : Not available")
                    .setAvailable(false));
        }

        List<ResGetDownloadables.ReportBean> reports = resGetDownloadables.getReport();
        if (reports.isEmpty()) {
            mDownloadables.add(new Downloadable()
                    .setTitle("Review : Not available")
                    .setAvailable(false));
        } else {
            for (int i = 0; i < reports.size(); i++) {
                ResGetDownloadables.ReportBean report = reports.get(i);

                Calendar calendar = Calendar.getInstance(getResources().getConfiguration().locale);
                calendar.setTimeInMillis(report.getDate() * 1000);
                SimpleDateFormat dateFormat = new SimpleDateFormat("(dd MMM yyyy)");
                String reportTitle = "Review " + dateFormat.format(calendar.getTime()) + " PDF available";

                mDownloadables.add(new Downloadable()
                        .setTitle(reportTitle)
                        .setLink(report.getReview())
                        .setAvailable(true)
                        .setSubscriptionNo(resGetDownloadables.getReason().getSubscriptionNo()));

            }
        }

        setUpRecyclerView();

    }

    private void setUpRecyclerView() {

        setViews();

        try {
            mRecyclerView.getAdapter().notifyDataSetChanged();
        } catch (Exception e) {
            DownloadbleAdapter adapter = new DownloadbleAdapter();
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setAdapter(adapter);
        }

    }

    private void showNotAvailable() {
        mDownloadables.clear();

        mDownloadables.add(new Downloadable()
                .setTitle("Reason : Not available")
                .setAvailable(false));

        mDownloadables.add(new Downloadable()
                .setTitle("Review : Not available")
                .setAvailable(false));


    }


    private class DownloadbleAdapter extends RecyclerView.Adapter<DownloadableHolder> {

        @Override
        public DownloadableHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            return new DownloadableHolder(inflater.inflate(R.layout.item_downloadable, parent, false));
        }

        @Override
        public void onBindViewHolder(DownloadableHolder holder, int position) {
            holder.bindDownloadable(mDownloadables.get(position));
        }

        @Override
        public int getItemCount() {
            return mDownloadables.size();
        }
    }

    private class DownloadableHolder extends RecyclerView.ViewHolder {

        private ImageView ivDownload;
        private MyTextView tvDownloadableName;

        private Downloadable downloadable;

        public DownloadableHolder(final View itemView) {
            super(itemView);

            ivDownload = (ImageView) itemView.findViewById(R.id.ivDownload);
            tvDownloadableName = (MyTextView) itemView.findViewById(R.id.tvDownloadableName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (downloadable.isAvailable() && !TextUtils.isEmpty(downloadable.getLink())) {

                        Log.v(LOG_TAG, "Opening Link: " + downloadable.getLink());
                        mCallbacks = (Callbacks) getActivity();
                        getDownloadablesRefresh(downloadable,mCallbacks);
                        try {

                            /*Intent intent = new Intent(Intent.ACTION_VIEW);
                            String urlLink=downloadable.getLink();
                            urlLink=urlLink.replaceAll("~","");
                            downloadable.setLink(urlLink);
                            intent.setDataAndType(Uri.parse( G.net.BASE_URL+downloadable.getLink()), "text/html");
                            //   intent.setDataAndType(Uri.parse("http://192.168.1.154/productattachment/KYCProof/test_.pdf"), "text/html");
                            startActivity(intent);*/
                            //"http://docs.google.com/viewer?url=" +

                            //OR
                            //Use callbacks to open in webview
                          //  mCallbacks.openPdf(downloadable.getTitle(), downloadable.getLink());

                        } catch (Exception e) {
                            Snackbar.make(view, "! Can't open", Snackbar.LENGTH_SHORT).show();
                        }

                        dismissDialog(RESULT_PRIMARY);

                    }
                    //else do nothing
                }
            });

        }

        public void bindDownloadable(Downloadable downloadable) {
            this.downloadable = downloadable;

            tvDownloadableName.setText(this.downloadable.getTitle());
            if (this.downloadable.isAvailable()) {
                ivDownload.setVisibility(View.VISIBLE);
            } else {
                ivDownload.setVisibility(View.INVISIBLE);
            }

        }
    }

    private void setViews() {


        ivClose.setVisibility(View.VISIBLE);
        setCancelable(true);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog(RESULT_CANCELED);
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_downloadables_bottom_sheet_dialog, container, false);

        findViews(rootView);

        return rootView;
    }

    private void findViews(View rootView) {
        ivClose = (ImageView) rootView.findViewById(R.id.ivClose);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rvDownloadables);
        tvMessage = (MyTextView) rootView.findViewById(R.id.tvMessage);
    }

    public void dismissDialog(int resultCode) {
        this.resultCode = resultCode;
        dismiss();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        Intent resultData = new Intent();
        try {
            getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, resultData);
        } catch (Exception e) {
            //
        }
    }

    private class Downloadable {

        private String title;
        private String link;
        private boolean available;
        private String subscriptionNo;

        public String getSubscriptionNo() {
            return subscriptionNo;
        }
        public Downloadable setSubscriptionNo(String subscriptionNo) {
            this.subscriptionNo = subscriptionNo;
            return this;
        }

        public String getTitle() {
            return title;
        }

        public Downloadable setTitle(String title) {
            this.title = title;
            return this;
        }

        public String getLink() {
            return link;
        }

        public Downloadable setLink(String link) {
            this.link = link;
            return this;
        }

        public boolean isAvailable() {
            return available;
        }

        public Downloadable setAvailable(boolean available) {
            this.available = available;
            return this;
        }
    }

    public interface Callbacks {
        void openPdf(String title, String url);
    }


    @Override
    public void onStop() {
        super.onStop();
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
