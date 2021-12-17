package rudenia.fit.bstu.projectstpms;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashscreenActivity extends AppCompatActivity {

    private ImageView image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashscreenActivity.this, MainActivity.class));
                finish();
            }
        }, 3*1000);
    }
}