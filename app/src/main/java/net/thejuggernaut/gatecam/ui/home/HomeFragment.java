package net.thejuggernaut.gatecam.ui.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import net.thejuggernaut.gatecam.R;
import net.thejuggernaut.gatecam.api.AlertApi;
import net.thejuggernaut.gatecam.api.Motion;
import net.thejuggernaut.gatecam.api.SetupRetro;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        getAlerts(root);

        return root;
    }

    public void getAlerts(View v){
        AlertApi api = SetupRetro.getRetro();
        Call<Motion[]> call = api.getMotions();

        call.enqueue(new Callback<Motion[]>() { @Override
        public void onResponse(Call<Motion[]> call, Response<Motion[]> response) {
            if(response.isSuccessful()) {
                System.out.println("Msg.. "+response.body()[0].getCode());
                displayMotion(v,response.body());
            } else {
                //not found
                Log.i("PRODUCT",response.message());
            }
        }


            @Override
            public void onFailure(Call<Motion[]> call, Throwable t) {
                t.printStackTrace();
            }

        });
    }

    public void displayMotion(View v, Motion[] m){
        TableLayout mt = (TableLayout) v.findViewById(R.id.motionTable);
        mt.removeAllViews();

        for(Motion i : m){
            TableRow tr = new TableRow(v.getContext());

            Button code = new Button(v.getContext());
            code.setText(i.getCode());
            code.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                   openVideo(i.getCode(),v);
                }
            });
            tr.addView(code);

            TextView stamp = new TextView(v.getContext());
            long unixSeconds = (long) Float.parseFloat(i.getStart());
// convert seconds to milliseconds
            Date date = new java.util.Date(unixSeconds * 1000L);
// the format of your date
            SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
// give a timezone reference for formatting (see comment at the bottom)
            //sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT-4"));
            String formattedDate = sdf.format(date);
            stamp.setText(formattedDate);
            tr.addView(stamp);

            mt.addView(tr);
        }


    }

    private  void openVideo(String code, View v){
        PopupWindow p = new PopupWindow(v.getContext());
        p.setFocusable(true);
        LinearLayout l = new LinearLayout(v.getContext());
        p.setContentView(l);

        VideoView videoView = new VideoView(v.getContext());
        String link="http://192.168.122.69:8000/motion/"+code;
        MediaController mediaController = new MediaController(v.getContext());
        mediaController.setAnchorView(videoView);
        Uri video = Uri.parse(link);
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(video);
        videoView.start();


        l.addView(videoView);
        p.showAtLocation(v, Gravity.TOP,10,10);


    }
}