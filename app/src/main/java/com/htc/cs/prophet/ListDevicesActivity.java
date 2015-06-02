package com.htc.cs.prophet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.htc.cs.prophet.service.RecommendRequest;

import java.util.List;


public class ListDevicesActivity extends ActionBarActivity {

    private static final String TAG = "[Prophet][" + ListDevicesActivity.class.getSimpleName() + "]";

    private ListView listView;
    private Activity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_devices);

        listView = (ListView) findViewById(R.id.devices_list_view);
        context = this;

        RecommendRequest.getUserList(this, new RecommendRequest.OnGetUserListListener() {
            @Override
            public void onSuccess(final List<String> users) {
                //String[] tmp = new String[]{"HT41JW9A0245", "FA3CPPN03795", "HT547YC01574", "FA456S904090", "HT35SW905380", "HT442SF03450", "FA4B3SR03566", "FA41LS900243", "FA43XWM05622", "FA455SF01594"};
                users.add(0, "My Device");
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, users);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Log.d(TAG, users.get(position));
                        Intent intent = new Intent();
                        intent.putExtra("DSN", users.get(position));
                        context.setResult(Activity.RESULT_OK, intent);
                        context.finish();
                    }
                });
            }
            @Override
            public void onError() {

            }
        });
//        final String[] tmp = new String[]{"My Device", "FA484WM01569", "HT3AMW900450", "FA3BJS900643", "FA454WM02005", "HT4BBWMA0019", "FA35SS900242", "HT365W905497", "FA3BYSC00743", "FA41ES903595", "FA38XS900498", "HT4BKWM03566", "HT348W910322", "FA46TWM01115", "FA37LS909486", "HT53NSV12421", "HT434SF02095", "FA37AS901411", "FA38LW915411", "FA53YSV00210", "FA459WM00946", "HT34GW916956", "HT4CAWM00785", "LC4BKYA44056", "HT441SF09486", "HT437SF03679", "FA39HW904266", "HT42CW902410", "FA4ALSR05499", "HT447SF02553", "FA53SYJ03309"};
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, tmp) {

//        };
//        listView.setAdapter(adapter);

    }




}
