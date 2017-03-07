package com.hoshizora.ossstb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.DefaultItemAnimator;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.loopj.android.http.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    private ListView leftDrawer;
    private ArrayAdapter<String> listAdapter;

    private List<ProductItem> productItemList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ProductItemsAdapter pAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Sari-sari Store");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Find the ListView resource.
        leftDrawer = (ListView) findViewById( R.id.left_drawer );
        recyclerView = (RecyclerView) findViewById(R.id.item_collection);

        // Create and populate a List of planet names.
        String[] categories = new String[] {"Home", "Kitchen", "Food", "Outing"};
        ArrayList<String> categoryList = new ArrayList<String>();
        categoryList.addAll( Arrays.asList(categories) );

        // Create ArrayAdapter using the planet list.
        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, categoryList);

        // Set the ArrayAdapter as the ListView's adapter.
        leftDrawer.setAdapter( listAdapter );

        pAdapter = new ProductItemsAdapter(this, productItemList);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager pLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
//                LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(pLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, GridLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(pAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                ProductItem productItem = productItemList.get(position);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


    }

    protected void onResume(){
        super.onResume();
        prepareItemData();
    }
    private void prepareItemData(){
        productItemList.clear();

        String url = getString(R.string.host) + getString(R.string.products);

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("action", "get_products");

        client.get(url, params, new JsonHttpResponseHandler() {
            KProgressHUD hud;

            @Override
            public void onStart(){
                super.onStart();
                hud = KProgressHUD.create(MainActivity.this)
                        .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
                        .setLabel("Please wait")
                        .setMaxProgress(100)
                        .show();
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // Root JSON in response is an dictionary i.e { "data : [ ... ] }
                // Handle resulting parsed JSON response here

                try {
                    JSONArray result = response.getJSONArray("message");
                    Log.d("DEBUG", result.toString());

                    for (int i=0; i < result.length(); i++) {
                        JSONObject productItemJSON;

                        try {
                            productItemJSON = result.getJSONObject(i);
                        } catch (Exception e) {
                            e.printStackTrace();
                            continue;
                        }

                        ProductItem item = ProductItem.ProductItemFromJson(productItemJSON);
                        if (item != null) {
                            productItemList.add(item);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                pAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)

            }

            @Override
            public void onFinish(){
                super.onFinish();
                pAdapter.notifyDataSetChanged();
                hud.dismiss();
            }
        });


    }
}
