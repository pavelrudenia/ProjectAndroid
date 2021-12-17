package rudenia.fit.bstu.projectstpms;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.appcompat.app.AppCompatActivity;
import rudenia.fit.bstu.projectstpms.ProgressBar.CircularProgressBar;
import rudenia.fit.bstu.projectstpms.ProgressBar.RateTextCircularProgressBar;


public class SplashscreenActivity extends Activity {

  //  private CircularProgressBar mCircularProgressBar;
   // private RateTextCircularProgressBar mRateTextCircularProgressBar;
    public RateTextCircularProgressBar mRate;
    private int progress = 0;


    private Handler mHandler = new Handler() {

        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg) {

            mRate.setProgress(msg.what);
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
        setContentView(R.layout.activity_main);



        mRate = (RateTextCircularProgressBar)findViewById(R.id.rate_progress_bar);
        mRate.setMax(100);
        mRate.getCircularProgressBar().setCircleWidth(20);

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
