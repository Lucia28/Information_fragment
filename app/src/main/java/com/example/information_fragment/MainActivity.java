package com.example.information_fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = "MainActivity";
    private ImageHandler handler = new ImageHandler(new WeakReference<MainActivity>(this));
    private ImageHandler handler_menu = new ImageHandler(new WeakReference<MainActivity>(this));
    private ViewPager viewPager;
    private ViewPager viewPager_menu;
    private TextView mTextMessage;

    Boolean look;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_search:
                    mTextMessage.setText(R.string.title_search);
                    return true;
                case R.id.navigation_booking:
                    mTextMessage.setText(R.string.title_booking);
                    return true;
                case R.id.navigation_account:
                    mTextMessage.setText(R.string.title_account);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        mTextMessage = findViewById(R.id.message);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        final Button text_more = findViewById(R.id.more);
        final TextView text = findViewById(R.id.intro);
        //Boolean look;
        look = true;
        text_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (look) {//展開
                    look = false;
                    text.setMaxLines(Integer.MAX_VALUE);
                    text.setEllipsize(null);
                    text_more.setText("顯示更少");
                }
                else {//收回
                    look = true;
                    text.setMaxLines(3);
                    text_more.setText("顯示更多");
                    text.setEllipsize(TextUtils.TruncateAt.END);
                }
            }
        });

        // 初始化viewPager的內容
        viewPager = (ViewPager) findViewById(R.id.viewP1);
        viewPager_menu = (ViewPager) findViewById(R.id.viewP2);

        //資料集
        ArrayList<ImageView> views = new ArrayList<ImageView>();
        ArrayList<ImageView> view_menu = new ArrayList<ImageView>();

        for (int i = 0; i < 3; i++) {
            ImageView view1 = new ImageView(this);
            ImageView view2 = new ImageView(this);
            if (i == 0) {
                view1.setImageResource(R.drawable.garten);
                view2.setImageResource(R.drawable.leaf);
            } else if (i == 1) {
                view1.setImageResource(R.drawable.location1);
                view2.setImageResource(R.drawable.food1);
            } else if (i == 2) {
                view1.setImageResource(R.drawable.location2);
                view2.setImageResource(R.drawable.food2);
            }
            views.add(view1);
            view_menu.add(view2);
        }

        viewPager.setAdapter(new Adapter(views));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                handler.sendMessage(Message.obtain(handler, ImageHandler.MSG_PAGE_CHANGED, i, 0));
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                switch (i) {
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        handler.sendEmptyMessage(ImageHandler.MSG_KEEP_SILENT);
                        break;
                    case ViewPager.SCROLL_STATE_IDLE:
                        handler.sendEmptyMessageDelayed(ImageHandler.MSG_UPDATE_IMAGE, ImageHandler.MSG_DELAY);
                        break;
                    default:
                        break;
                }
            }
        });

        viewPager.setCurrentItem(Integer.MAX_VALUE / 2);// 預設在中間，使使用者看不到邊界
        // 開始輪播效果
        handler.sendEmptyMessageDelayed(ImageHandler.MSG_UPDATE_IMAGE, ImageHandler.MSG_DELAY);

        viewPager_menu.setAdapter(new Adapter(view_menu));
        viewPager_menu.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                handler_menu.sendMessage(Message.obtain(handler_menu, ImageHandler.MSG_PAGE_CHANGED, i, 0));
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                switch (i) {
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        handler_menu.sendEmptyMessage(ImageHandler.MSG_KEEP_SILENT);
                        break;
                    case ViewPager.SCROLL_STATE_IDLE:
                        handler_menu.sendEmptyMessageDelayed(ImageHandler.MSG_UPDATE_IMAGE, ImageHandler.MSG_DELAY);
                        break;
                    default:
                        break;
                }
            }
        });

        viewPager_menu.setCurrentItem(Integer.MAX_VALUE / 2);
        handler_menu.sendEmptyMessageDelayed(ImageHandler.MSG_UPDATE_IMAGE, ImageHandler.MSG_DELAY);

    }// end of onCreate

    //加上動畫滑動
    public static class ImageHandler extends Handler {
        /**
         * 請求更新顯示的View。
         */
        public static final int MSG_UPDATE_IMAGE = 1;
        /**
         * 請求暫停輪播。
         */
        public static final int MSG_KEEP_SILENT = 2;
        /**
         * 請求恢復輪播。
         */
        public static final int MSG_BREAK_SILENT = 3;
        /**
         * 記錄最新的頁號，當使用者手動滑動時需要記錄新頁號，否則會使輪播的頁面出錯。
         * 例如當前如果在第一頁，本來準備播放的是第二頁，而這時候使用者滑動到了末頁，
         * 則應該播放的是第一頁，如果繼續按照原來的第二頁播放，則邏輯上有問題。
         */
        public static final int MSG_PAGE_CHANGED = 4;
        // 輪播間隔時間
        public static final long MSG_DELAY = 3000;
        // 使用弱引用避免Handler洩露.這裡的泛型引數可以不是Activity，也可以是Fragment等
        private WeakReference<MainActivity> weakReference;
        private int currentItem = 0;
        private int currentItem_menu = 0;

        public ImageHandler(WeakReference<MainActivity> wk) {
            weakReference = wk;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //Log.d(LOG_TAG, "receive message");
            MainActivity activity = weakReference.get();
            if (activity == null) {
                // Activity已經回收，無需再處理UI了
                return;
            }
            // 檢查訊息佇列並移除未傳送的訊息，這主要是避免在複雜環境下訊息出現重複等問題。
            if ((activity.handler.hasMessages(MSG_UPDATE_IMAGE)) && (currentItem != 0)) {
                activity.handler.removeMessages(MSG_UPDATE_IMAGE);
            }
            if ((activity.handler_menu.hasMessages(MSG_UPDATE_IMAGE)) && (currentItem_menu != 0)) {
                activity.handler_menu.removeMessages(MSG_UPDATE_IMAGE);
            }
            switch (msg.what) {
                case MSG_UPDATE_IMAGE:
                    currentItem++;
                    activity.viewPager.setCurrentItem(currentItem);
                    // 準備下次播放
                    activity.handler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
                    currentItem_menu++;
                    activity.viewPager_menu.setCurrentItem(currentItem_menu);
                    activity.handler_menu.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
                    break;
                case MSG_KEEP_SILENT:
                    // 只要不傳送訊息就暫停了
                    break;
                case MSG_BREAK_SILENT:
                    activity.handler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
                    activity.handler_menu.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
                    break;
                case MSG_PAGE_CHANGED:
                    // 記錄當前的頁號，避免播放的時候頁面顯示不正確。
                    currentItem = msg.arg1;
                    currentItem_menu = msg.arg1;
                    break;
                default:
                    break;
            }
        }
    }
}
