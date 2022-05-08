package rudenia.fit.bstu.projectstpms.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import rudenia.fit.bstu.projectstpms.ProgressBar.RateTextCircularProgressBar;
import rudenia.fit.bstu.projectstpms.R;


public class SplashscreenActivity extends Activity {

  //  private CircularProgressBar mCircularProgressBar;
   // private RateTextCircularProgressBar mRateTextCircularProgressBar;
    public RateTextCircularProgressBar mRateTextCircularProgressBar;
    private int progress = 0;


    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            mRateTextCircularProgressBar.setProgress(msg.what);
            if( progress >= 100 ) {
                progress = 100;
            }
            mHandler.sendEmptyMessageDelayed(progress++, 25);
            super.handleMessage(msg);
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);



        mRateTextCircularProgressBar = (RateTextCircularProgressBar)findViewById(R.id.rate_progress_bar);
        mRateTextCircularProgressBar.setMax(100);
        mRateTextCircularProgressBar.getCircularProgressBar().setCircleWidth(20);

        mHandler.sendEmptyMessageDelayed(progress++, 100);
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AreYouOnline();
                finish();
            }
        }, 3*1111);
    }

    public void AreYouOnline(){
        if ( !isOnline() ){
            Intent intent = new Intent(this,StartActivityNoEthernet.class);
            startActivity(intent);
        }
        else{
            Intent intent2 = new Intent(this,StartActivity.class);
            startActivity(intent2);
        }
    }
    protected boolean isOnline() {
        String cs = Context.CONNECTIVITY_SERVICE;
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(cs);
        if (cm.getActiveNetworkInfo() == null) {
            return false;
        } else {
            return true;
        }
    }
}
