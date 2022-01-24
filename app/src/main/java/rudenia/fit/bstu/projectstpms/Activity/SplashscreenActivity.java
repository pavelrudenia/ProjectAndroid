package rudenia.fit.bstu.projectstpms.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;

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
                startActivity(new Intent(SplashscreenActivity.this, MainActivity.class));
                finish();
            }
        }, 3*1111);
    }


}
