package net.thejuggernaut.gatecam.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import net.thejuggernaut.gatecam.R;
import net.thejuggernaut.gatecam.WebSocketEcho;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class DashboardFragment extends Fragment {



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(100,  TimeUnit.MILLISECONDS)
                .build();
        WebSocketEcho w = new WebSocketEcho(getActivity());
        Request request = new Request.Builder()
                .url("ws://192.168.122.69:8000/stream/Hello")
                .build();
        client.newWebSocket(request, w);

        // Trigger shutdown of the dispatcher's executor so this process can exit cleanly.
        client.dispatcher().executorService().shutdown();

        return root;
    }



}