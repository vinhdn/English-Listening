package effortlessenglish.estorm.vn.effortlessenglish.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import effortlessenglish.estorm.vn.effortlessenglish.Adapters.SubMenuItemAdapter;
import effortlessenglish.estorm.vn.effortlessenglish.Base.BaseActivity;
import effortlessenglish.estorm.vn.effortlessenglish.Models.Lession;
import effortlessenglish.estorm.vn.effortlessenglish.Models.Models;
import effortlessenglish.estorm.vn.effortlessenglish.Models.SubMenu;
import effortlessenglish.estorm.vn.effortlessenglish.R;
import effortlessenglish.estorm.vn.effortlessenglish.Utils.Constants;
import effortlessenglish.estorm.vn.effortlessenglish.volley.VolleySingleton;

public class LessionActivity extends BaseActivity implements ListView.OnItemClickListener {

    private ListView listView;
    private ArrayList<Models> listData;
    private SubMenu parMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showProgressDialog(true);
        if (Constants.selectedModel != null && Constants.selectedModel.getId() == getIntent().getIntExtra(Constants.EXTRA_ID, 0)) {
            parMenu = new SubMenu(Constants.selectedModel);
        } else
            parMenu = new SubMenu();
        setContentView(R.layout.activity_menu);
        getData();
    }

    @Override
    public void setActionView() {
        listView = (ListView) findViewById(R.id.menu_item);
        listView.setOnItemClickListener(this);
    }

    private void getData() {
        if (isNetwork()) {
            HashMap<String, String> params = new HashMap<>();
            params.put("id", parMenu.getId() + "");
            VolleySingleton
                    .getInstance()
                    .getRequestQueue()
                    .add(new JsonObjectRequest(Request.Method.GET,
                            Constants.SUBMENU + "?id=" + parMenu.getId() , null,
                            new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject data) {
                                    Log.d("LessionActivity",data.toString());
                                    try {
                                        if(data.getInt("error") == 1) {
                                            showProgressDialog(false);
                                            showView(R.id.list_no_have_item, true);
                                            return;
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    listData = new ArrayList<>();
                                    try {
                                        JSONArray list = data.getJSONArray("data");
                                        for (int i = 0; i < list.length(); i++) {
                                            try {
                                                Lession menu = new Lession(list.getJSONObject(i), parMenu);
                                                listData.add(menu);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        processData();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    showProgressDialog(false);
                                }
                            }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError arg0) {
                            showProgressDialog(false);
                            arg0.printStackTrace();
                            showView(R.id.list_no_have_item, true);
                        }
                    }));
        } else {
            getOffline();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Constants.selectedModel = parMenu;
    }

    private void getOffline() {
        try {
            listData = mApp.getLocalStorage().getListsModelsOfParent(parMenu, Models.TYPE_MODEL.LESSION.getValue());
            processData();
            showProgressDialog(false);
        } catch (Exception ex) {
            ex.printStackTrace();
            showProgressDialog(false);
            showView(R.id.list_no_have_item, true);
        }
    }

    private void processData() {
        if (listData.size() == 0)
            showView(R.id.list_no_have_item, true);
        SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(new SubMenuItemAdapter(this, null, listData));
        swingBottomInAnimationAdapter.setAbsListView(listView);
        assert swingBottomInAnimationAdapter.getViewAnimator() != null;
        swingBottomInAnimationAdapter.getViewAnimator().setInitialDelayMillis(INITIAL_DELAY_MILLIS);
        listView.setAdapter(swingBottomInAnimationAdapter);
    }

    @Override
    public void onPlayerChange(String action) {

    }

    @Override
    public void onConnectServcie() {
        if (service != null && service.getCurrentLession() != null && service.getCurrentLession().getId() > 0) {
            showController();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (service == null)
            return;
        Constants.selectedModel = listData.get(position);
        Lession lession = new Lession(listData.get(position));
        service.playSong(position, listData);
        Intent i = new Intent(LessionActivity.this, PlayerActivity.class);
        i.putExtra(Constants.EXTRA_ID, listData.get(position).getId());
        startActivity(i);
    }
}
