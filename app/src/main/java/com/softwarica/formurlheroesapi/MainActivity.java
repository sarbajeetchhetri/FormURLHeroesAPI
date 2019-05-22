package com.softwarica.formurlheroesapi;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ReplacementTransformationMethod;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int imageRequestCode =  0;





    

    private EditText etName, etDesc;
    private ImageButton btnimage;
    private ListView listView;
    private List list = new ArrayList();
    private ArrayAdapter<String> arrayAdapter;
    private Button btnsubmit;
    private Retrofit retrofit;
    private Employee_Interface employee_interface;
    private String imagePath;
    private ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        getConnection();
        LoadData();
    }

    private void getConnection() {
        retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:3000/").addConverterFactory(GsonConverterFactory.create()).build();
        employee_interface = retrofit.create(Employee_Interface.class);
    }

    private void createAdapter(List list){
        arrayAdapter =  new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,list);
    }

    private void init() {
        etDesc = findViewById(R.id.etDesc);
        etName = findViewById(R.id.etName);
        btnimage = findViewById(R.id.imageButton);
        listView = findViewById(R.id.listView);
        btnsubmit = findViewById(R.id.btnsubmit);
        img = findViewById(R.id.profileImage);
        btnsubmit.setOnClickListener(this);
        btnimage.setOnClickListener(this);
    }
    
    
    private void LoadData(){
        Call<List<Employee>> datas =  employee_interface.getAllData();
        datas.enqueue(new Callback<List<Employee>>() {
            @Override
            public void onResponse(Call<List<Employee>> call, Response<List<Employee>> response) {
                if(response.isSuccessful()){
                    List<Employee> items = response.body();
                    for(Employee emp : items){
                        list.add(emp.getName());
                    }
                    createAdapter(list);
                    listView.setAdapter(arrayAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Employee>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Could no load data", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void PutData(String name,String desc){
        Call<Void> put = employee_interface.putData(name,desc);
        put.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(MainActivity.this, "Data inserted Successfully", Toast.LENGTH_SHORT).show();
                list.clear();
                LoadData();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void PutDataByMap(String name,String desc){
        HashMap<String,String> map = new HashMap<>();
        map.put("name",name);
        map.put("desc",desc);
        Call<Void> put = employee_interface.putAllData(map);
        put.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(MainActivity.this, "Data inserted Successfully", Toast.LENGTH_SHORT).show();
                list.clear();
                LoadData();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnsubmit){
            if(TextUtils.isEmpty(etName.getText())){
                return;
            }
            if(TextUtils.isEmpty(etDesc.getText())){
                return;
            }
            String username = etName.getText().toString();
            String description = etDesc.getText().toString();
            PutData(username,description);

        }else if (v.getId() == R.id.imageButton){
            startActivityForResult(new Intent(Intent.ACTION_PICK).setType("image/*"),imageRequestCode);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == imageRequestCode){
            if(data == null){
                Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
                return;
            }
            Uri uri = data.getData();
            imagePath = getPath(uri);
            Toast.makeText(this, imagePath, Toast.LENGTH_SHORT).show();
            showimage(imagePath);

        }
    }

    private void showimage(String imagePath) {
        File imgFile = new File(imagePath);
        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            img.setImageBitmap(myBitmap);
        }

    }

    private String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        CursorLoader loader =  new CursorLoader(getApplicationContext(), uri, projection,null,null,null);
        Cursor cursor = loader.loadInBackground();
        int columnindex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(columnindex);
        cursor.close();
        return result;
    }
}
