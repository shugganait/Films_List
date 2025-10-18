package shug.filmslist.ui.activity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import shug.filmslist.R;

public class MainActivity extends AppCompatActivity {

    private View navHost;
    private View noConnectionLayout;
    private ConnectivityManager.NetworkCallback networkCallback;
    private ConnectivityManager connectivityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navHost = findViewById(R.id.nav_host_fragment);
        noConnectionLayout = findViewById(R.id.no_connection_layout);

        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                runOnUiThread(() -> updateUI(true));
            }

            @Override
            public void onLost(Network network) {
                runOnUiThread(() -> updateUI(false));
            }
        };

        if (connectivityManager != null) {
            connectivityManager.registerDefaultNetworkCallback(networkCallback);
        }
    }

    private void updateUI(boolean connected) {
        navHost.setVisibility(connected ? View.VISIBLE : View.GONE);
        noConnectionLayout.setVisibility(connected ? View.GONE : View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (connectivityManager != null && networkCallback != null) {
            connectivityManager.unregisterNetworkCallback(networkCallback);
        }
    }
}