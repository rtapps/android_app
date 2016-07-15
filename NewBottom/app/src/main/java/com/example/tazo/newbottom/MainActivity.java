package com.example.tazo.newbottom;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.example.tazo.newbottom.Inbox.InboxFragment;

public class MainActivity extends AppCompatActivity {
    int curPage = 0;
    protected RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);

        PagerAdapter pa = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                if (position == 0) {
                  //  return firstFrag.newInstance("2", "3");
                    return new InboxFragment();
                }

                return secFragment.newInstance("2", "3");
            }

            @Override
            public int getCount() {
                return 2;
            }
        };

        viewPager.setAdapter(pa);


        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });


        Button b = (Button) findViewById(R.id.btn);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curPage = 1 - curPage;
                viewPager.setCurrentItem(curPage , false);
            }
        });


    }


}
