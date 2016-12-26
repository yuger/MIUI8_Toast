package com.wxy.miuitoast;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MiUI8Toast miUI8Toast = MiUI8Toast.makeText(getApplicationContext());
        if (miUI8Toast != null) miUI8Toast.show();
        Toast.makeText(this, "isMiUI8:" + isMiUI8plus(), Toast.LENGTH_LONG).show();
    }

    private boolean isMiUI8plus() {
        Properties property = new Properties();
        try {
            property.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
        } catch (IOException ignore) {
        }
        String versionName = property.getProperty("ro.miui.ui.version.name");
        try {
            return versionName != null && Integer.decode(versionName.substring(1)) >= 8;
        } catch (Exception e) {
            return false;
        }
    }
}
