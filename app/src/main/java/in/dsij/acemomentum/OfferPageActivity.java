package in.dsij.acemomentum;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import in.dsij.acemomentum.view.MyTextView;

public class OfferPageActivity extends AppCompatActivity {

    private SimpleDraweeView ivOfferImg;
    private MyTextView tvHeadline, tvDescription, tvDone,tvTitle;
    private String imageUrl="https:\\/\\/api.androidhive.info\\/images\\/minion.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_page);
        try {
            imageUrl = getIntent().getStringExtra("imageUrl");
        } catch (Exception e) {
            e.printStackTrace();
        }

        findView();

    }

    private void findView() {
        tvTitle= (MyTextView) findViewById(R.id.tvTitle);
        tvHeadline = (MyTextView) findViewById(R.id.tvHealine);
        tvDescription = (MyTextView) findViewById(R.id.tvSummery);
        tvDone = (MyTextView) findViewById(R.id.tvDone);
        ivOfferImg = (SimpleDraweeView) findViewById(R.id.ivImage);
        setView();
    }

    private void setView() {
        tvHeadline.setText("headline");
     //   imageUrl="https:/api.androidhive.info/images/minion.jpg";
        tvDescription.setText("To be a successful trader, one needs to gauge the trend and place a bet in favour of the trend. Most traders often make their trading decisions based solely on a single timeframe. They devote all their time and vitality in investigating the trend on a single time frame, however, they neglect to comprehend what might occur indifferent timeframes which would impact their analysis");
       // Bitmap bm=getBitmapfromUrl("https://api.androidhive.info/images/minion.jpg");
        ivOfferImg.setImageURI(Uri.parse(imageUrl));
        tvDone.setText("Done");
        tvTitle.setText("ACE Momentum");
    }

    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }
    }
}
