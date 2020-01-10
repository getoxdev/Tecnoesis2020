package com.github.tenx.tecnoesis20.ui.about;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.os.Bundle;

import com.github.tenx.tecnoesis20.R;
import com.github.tenx.tecnoesis20.ui.about.home.AboutHomeFragment;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AboutActivity extends AppCompatActivity {


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initFragment(new AboutHomeFragment());
    }



    public void initFragment(Fragment frag){
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_about, frag).commit();

    }
}
