package com.mario_antolovic.downloadmanager;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {

    long queid;
    DownloadManager dm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // to see its download completed !
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {


                String action  = intent.getAction();
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action )) {
                    DownloadManager.Query request_query = new DownloadManager.Query();
                    request_query.setFilterById(queid);

                    Cursor c = dm.query(request_query);
                   // FIRST we check there is record from our download / STATUS is download success or not
                    if (c.moveToFirst()) {

                        int columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
                        //where file is gonna be saved
                        if (DownloadManager.STATUS_SUCCESSFUL== c.getInt(columnIndex)) {
                            VideoView videoView = (VideoView) findViewById(R.id.videoView);
                            String uriString = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));

                            //play pause option
                            MediaController mediaController = new MediaController(getApplicationContext());
                            mediaController.setAnchorView(videoView);
                            videoView.requestFocus();
                            videoView.setVideoURI(Uri.parse(uriString));
                            videoView.start();

                        }

                    }


                }
            }
        };
    }


    public void Click_Download(View v) {

        dm = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse("http://mario-antolovic.com/testing/testing.mp4"));

         queid = dm.enqueue(request);
    }
    public void View_Click(View v) {
        Intent implicit = new Intent();
        implicit.setAction(DownloadManager.ACTION_VIEW_DOWNLOADS);
        startActivity(implicit);
    }

}
