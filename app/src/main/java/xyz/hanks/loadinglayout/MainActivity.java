package xyz.hanks.loadinglayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import xyz.hanks.loadinglayout_library.LoadingLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final LoadingLayout loadingLayout =
                (LoadingLayout) findViewById(R.id.layout);

        loadingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("AAA", "click");
                final boolean normalStatus = loadingLayout.isNormalStatus();
                final boolean selectedStatus = loadingLayout.isSelectedStatus();
                loadingLayout.loading();

                new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (selectedStatus) {
                                    loadingLayout.normal();
                                } else if (normalStatus) {
                                    loadingLayout.select();
                                }
                            }
                        });
                    }
                }.start();

            }
        });
    }
}
