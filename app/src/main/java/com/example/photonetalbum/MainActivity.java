package com.example.photonetalbum;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;
import com.album.Client2;
import com.client.Client;
import com.google.android.material.tabs.TabLayout;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private int requestCode = 100;
    private int pageCount = 1;
    private TabLayout Folder;
    private NonSweepViewPager viewPager;
    private View fragUser2;
    private View fragUserConnect;
    private EditText connectioCode;
    private Button btnConnect;
    private View fragChat;
    private final ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
    private boolean isconnect = false;
    private Client client;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            System.out.println(item);
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Folder.setVisibility(View.VISIBLE);
                    viewPager.setVisibility(View.VISIBLE);
                    fragUser2.setVisibility(View.INVISIBLE);
                    fragUserConnect.setVisibility(View.INVISIBLE);
                    fragChat.setVisibility(View.INVISIBLE);
                    return true;
                case R.id.navigation_dashboard:
                    viewPager.setVisibility(View.INVISIBLE);
                    Folder.setVisibility(View.INVISIBLE);
                    fragUser2.setVisibility(View.INVISIBLE);
                    fragUserConnect.setVisibility(View.INVISIBLE);
                    fragChat.setVisibility(View.VISIBLE);
                    return true;
                case R.id.navigation_notifications:
                    viewPager.setVisibility(View.INVISIBLE);
                    Folder.setVisibility(View.INVISIBLE);
                    fragChat.setVisibility(View.INVISIBLE);
                    if (isconnect) fragUser2.setVisibility(View.VISIBLE);
                    else fragUserConnect.setVisibility(View.VISIBLE);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermissions();
    }

    private void startProgram() {
        setContentView(R.layout.activity_main);
        Folder = (TabLayout) findViewById(R.id.Folder);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        viewPager = (NonSweepViewPager) findViewById(R.id.viewpager);
        fragUser2 = (View) findViewById(R.id.fragUser2);
        fragUser2.setVisibility(View.INVISIBLE);
        fragChat = (View) findViewById(R.id.fragChat);
        fragChat.setVisibility(View.INVISIBLE);
        fragUserConnect = (View) findViewById(R.id.fragUserConnect);
        connectioCode = (EditText) fragUserConnect.findViewById(R.id.conCode);
        fragUserConnect.setVisibility(View.INVISIBLE);
        btnConnect = (Button) fragUserConnect.findViewById(R.id.btnConnect);
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    connection();
                } catch (InterruptedException e) {
                    e.printStackTrace( );
                }
            }
        });
        setupViewPager(viewPager);
        Folder.setupWithViewPager(viewPager);
        Folder.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                System.out.println("position " + tab.getPosition());
                if (tab.getPosition() == pageCount) {
                    adapter.changeFragment(new PageFragmentUser1(), "Папка №" + (pageCount + 1), pageCount);
                    Folder.getTabAt(0).select();
                    pageCount++;
                    //adapter.allFragment();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //onTabSelected(tab);
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter.addFragment(new PageFragmentUser1(), "Папка №1", 0);
        adapter.addFragment(new PageFragmentUser1(), "+", 1);
        viewPager.setAdapter(adapter);
    }

    public void connection() throws InterruptedException {
        if (!connectioCode.getText().toString().trim().isEmpty()) {
            client = new Client(2, connectioCode.getText().toString());
            Thread thread = new Thread(new Runnable( ) {
                @Override
                public void run() {
                    client.run( );
                }
            });
            thread.setDaemon(true);
            thread.start();
            while (client.isClientCanConnect()) {
                Thread.sleep(500);
                if (client.isClientConnected()) {
                    if (Client2.sizeChangedListener()) {
                        InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        fragUser2.setVisibility(View.VISIBLE);
                        fragUserConnect.setVisibility(View.INVISIBLE);
                        isconnect = true;
                    }
                    else {
                        Toast.makeText(this, "Неверный код", Toast.LENGTH_SHORT)
                                .show();
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == this.requestCode) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                Toast.makeText(this, "Разрешение получено", Toast.LENGTH_SHORT)
                        .show();
                startProgram();
            } else {
                checkPermissions();
            }
        }
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
        } else {
            startProgram();
        }
    }
}