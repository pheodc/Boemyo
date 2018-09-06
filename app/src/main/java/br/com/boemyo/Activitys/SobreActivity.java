package br.com.boemyo.Activitys;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;

import br.com.boemyo.R;

public class SobreActivity extends AppCompatActivity {

    private Toolbar tbSobre;
    private TextView tvVersionApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobre);

        tbSobre = (Toolbar) findViewById(R.id.tb_sobre);
        tvVersionApp = (TextView) findViewById(R.id.tv_version_app);

        tbSobre.setTitle(R.string.title_sobre);
        setSupportActionBar(tbSobre);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tbSobre.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            tvVersionApp.setText("Versão: " + version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }



    }
}
