package net.zenjoy.photoeditor;

import android.app.MediaRouteButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.zentertain.common.ui.ProgressWheel;

public class MainActivity extends AppCompatActivity {
    
    public static MainActivity instance;
    public View fullScreenLoadView;
    public View fullScreenLoadAdView;
    public ProgressWheel adProgressWheel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;
        
        initViews();
    }

    private void initViews() {
        
    }
}
