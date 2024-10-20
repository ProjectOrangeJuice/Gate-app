package net.thejuggernaut.gatecam;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import net.thejuggernaut.gatecam.ui.dashboard.DashboardFragment;

import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public final class WebSocketEcho extends WebSocketListener {

  private Activity act;
  public WebSocketEcho(Activity act){
    this.act = act;
  }

  @Override public void onOpen(WebSocket webSocket, Response response) {
    System.out.println("Connected");
    webSocket.send("Hello");
  }

  @Override public void onMessage(WebSocket webSocket, final String text) {
    act.runOnUiThread(new Runnable() {
      @Override
      public void run() {
        //Handle UI here
        ImageView i = (ImageView) act.findViewById(R.id.imageView);
        byte[] decodedString = Base64.decode(text, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        i.setImageBitmap(decodedByte);
      }
    });

    System.out.println("MESSAGE: " + text);
  }

  @Override public void onClosing(WebSocket webSocket, int code, String reason) {
    webSocket.close(1000, null);
    System.out.println("CLOSE: " + code + " " + reason);
  }

  @Override public void onFailure(WebSocket webSocket, Throwable t, Response response) {
    t.printStackTrace();
  }

}