package com.milanapp.marvelretrofitdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.milanapp.marvelretrofitdemo.Adapter.MyAdapter;
import com.milanapp.marvelretrofitdemo.Model.Marvel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ArrayList<Marvel> marvels = new ArrayList<>();
    RecyclerView recyclerView;
    MyAdapter myAdapter;
    EditText etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView=findViewById(R.id.recycler);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        etSearch = findViewById(R.id.et_search);




        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);

        Call<List<Marvel>> call = api.getmarvel();

        call.enqueue(new Callback<List<Marvel>>() {
            @Override
            public void onResponse(Call<List<Marvel>> call, Response<List<Marvel>> response) {
                 marvels = new ArrayList<>(response.body());

                 myAdapter = new MyAdapter(MainActivity.this,marvels);
                recyclerView.setAdapter(myAdapter);



            }

            @Override
            public void onFailure(Call<List<Marvel>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();

            }
        });



        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                filter(s.toString());

                if (s.toString().isEmpty()){

                    etSearch.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(MainActivity.this,R.drawable.ic_search_black_24dp), null, null , null);
                }
                else {
                    etSearch.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(MainActivity.this,R.drawable.ic_search_black_24dp), null, ContextCompat.getDrawable(MainActivity.this,R.drawable.ic_search_black_24dp) , null);

                    etSearch.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            final int DRAWABLE_LEFT = 0;
                            final int DRAWABLE_TOP = 1;
                            final int DRAWABLE_RIGHT = 2;
                            final int DRAWABLE_BOTTOM = 3;


                            if(event.getAction() == MotionEvent.ACTION_UP) {

                                if(event.getRawX() >= (etSearch.getRight() - etSearch.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

                                    etSearch.setText("");
                                    etSearch.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(MainActivity.this,R.drawable.ic_search_black_24dp), null, ContextCompat.getDrawable(MainActivity.this,R.drawable.ic_back) , null);
                                    return true;
                                }
                            }
                            return false;
                        }
                    });

                }


            }
        });
    }




    private void filter(String text) {

        ArrayList<Marvel> filteredList = new ArrayList<>();

        for (Marvel item : marvels){
            if (item.getName().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(item);

            }
        }

        myAdapter.filterList(filteredList);
    }


}
