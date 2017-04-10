package net.chokethe.killerdealer.about;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import net.chokethe.killerdealer.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AboutActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String URI_MAILTO = "mailto:";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        findViewById(R.id.about_iv_mail_logo).setOnClickListener(this);
        findViewById(R.id.about_tv_mail_uri).setOnClickListener(this);
        findViewById(R.id.about_iv_github_logo).setOnClickListener(this);
        findViewById(R.id.about_tv_github_uri).setOnClickListener(this);

        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);

            TextView versionNameTextView = (TextView) findViewById(R.id.about_tv_version_name);
            versionNameTextView.setText("v" + pInfo.versionName);

            TextView sinceTextView = (TextView) findViewById(R.id.about_tv_since);
            SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
            sinceTextView.setText(dt.format(new Date(pInfo.lastUpdateTime)));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.about_iv_mail_logo:
            case R.id.about_tv_mail_uri:
                intentMail();
                break;
            case R.id.about_iv_github_logo:
            case R.id.about_tv_github_uri:
                intentBrowser();
                break;
        }
    }

    private void intentMail() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse(URI_MAILTO));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.about_mail_to)});
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.about_mail_subject));
        startActivity(intent);
    }

    private void intentBrowser() {
        Uri uri = Uri.parse(getString(R.string.about_github));
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
