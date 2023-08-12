package com.project.studiopatyzinha;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.FrameLayout;
import android.widget.TextView;

public class Notifications extends AppCompatActivity {
    Toolbar toolbar_noti;
    FrameLayout framelayout;
    public static TextView changefragmentoNoti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_livro_caixa);
        toolbar_noti = findViewById(R.id.toolbar_livroCaixa);
        changefragmentoNoti = new TextView(getBaseContext());
        changefragmentoNoti.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (String.valueOf(s).equals("dia")) {
                    changeFragment(NotidayFragment.class);
                } else {//week
                    changeFragment(NotidayFragment.class);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        toolbar_noti.setTitleTextColor(Color.WHITE);
        toolbar_noti.setNavigationIcon(R.drawable.ic_arrow_back);
        framelayout = findViewById(R.id.framelayout);
        setSupportActionBar(toolbar_noti);
        getSupportActionBar().setTitle("Notificações");
        toolbar_noti.setNavigationOnClickListener(v -> finish());
        changefragmentoNoti.setText("dia");
    }

    private void changeFragment(Class s) {
        Class fragmentResumoclass = s;
        Fragment fragmentmain = null;
        try {
            fragmentmain = (Fragment) fragmentResumoclass.newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        if (fragmentmain != null) {
            FragmentManager fragmentManeger = getSupportFragmentManager();
            fragmentManeger.beginTransaction().setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out).replace(R.id.framelayout, fragmentmain).commit();
        }
    }

}