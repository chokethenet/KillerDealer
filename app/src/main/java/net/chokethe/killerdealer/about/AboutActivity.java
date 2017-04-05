package net.chokethe.killerdealer.about;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import net.chokethe.killerdealer.R;
import net.chokethe.killerdealer.utils.CommonUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AboutActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        findViewById(R.id.about_iv_mail).setOnClickListener(this);
        findViewById(R.id.about_tv_mail).setOnClickListener(this);
        findViewById(R.id.about_iv_github).setOnClickListener(this);
        findViewById(R.id.about_tv_github).setOnClickListener(this);

        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);

            TextView versionNameTextView = (TextView) findViewById(R.id.about_tv_version_name);
            versionNameTextView.setText("v" + pInfo.versionName);

            TextView sinceTextView = (TextView) findViewById(R.id.about_tv_since);
            SimpleDateFormat dt = new SimpleDateFormat("yyyy-mm-dd");
            sinceTextView.setText(dt.format(new Date(pInfo.lastUpdateTime)));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.about_iv_mail:
            case R.id.about_tv_mail:
                CommonUtils.showToast(this, "email");
                break;
            case R.id.about_iv_github:
            case R.id.about_tv_github:
                CommonUtils.showToast(this, "github");
                break;
        }
    }
}
