package com.demo.openglbooksexample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.demo.openglbooksexample.eight.SimpleVertexShader;
import com.demo.openglbooksexample.fourteen.ParticleSystem;
import com.demo.openglbooksexample.nine_01.MipMap2D;
import com.demo.openglbooksexample.nine_02.SimpleTexture2D;
import com.demo.openglbooksexample.nine_03.SimpleTextureCubemap;
import com.demo.openglbooksexample.nine_04.TextureWrap;
import com.demo.openglbooksexample.six_01.Example6_3;
import com.demo.openglbooksexample.six_02.Example6_6;
import com.demo.openglbooksexample.six_03.MapBuffers;
import com.demo.openglbooksexample.six_04.VAO;
import com.demo.openglbooksexample.six_05.VBO;
import com.demo.openglbooksexample.ten.MultiTexture;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private BaseListViewAdapter<String> adapter;
    private List<String> data = new ArrayList<>();
    private String[] titles = {
            "第6章===01",
            "第六章===02",
            "第六章===03==MapBuffers",
            "第六章===04===VA0",
            "第六章===05===VB0",

            "第8章===SimpleVertexShader",

            "第9章===01===MipMap2D",
            "第9章===02===SimpleTexture2D",
            "第9章===03===SimpleTextureCubemap",
            "第9章===04===TextureWrap",

            "第10章===MultiTexture",

            "第14章===ParticleSystem"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listView);

        adapter = new BaseListViewAdapter<String>(this, data, R.layout.item_text_layout) {
            @Override
            public void convert(ViewHolder holder, String item, int position) {
                TextView tv = holder.getView(R.id.tv);
                tv.setText(item);
            }
        };
        init();
    }

    private void init() {
        data.addAll(Arrays.asList(titles));
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onItem(position);
            }
        });
    }

    private void onItem(int position) {
        Intent intent = null;
        switch (position) {
            case 0:
                intent = new Intent(this, Example6_3.class);
                break;
            case 1:
                intent = new Intent(this, Example6_6.class);
                break;
            case 2:
                intent = new Intent(this, MapBuffers.class);
                break;
            case 3:
                intent = new Intent(this, VAO.class);
                break;
            case 4:
                intent = new Intent(this, VBO.class);
                break;
            case 5:
                intent = new Intent(this, SimpleVertexShader.class);
                break;
            case 6:
                intent = new Intent(this, MipMap2D.class);
                break;
            case 7:
                intent = new Intent(this, SimpleTexture2D.class);
                break;
            case 8:
                intent = new Intent(this, SimpleTextureCubemap.class);
                break;
            case 9:
                intent = new Intent(this, TextureWrap.class);
                break;
            case 10:
                intent = new Intent(this, MultiTexture.class);
                break;
            case 11:
                intent = new Intent(this, ParticleSystem.class);
                break;
            default:
                intent = new Intent(this, ParticleSystem.class);
                break;
        }

        startActivity(intent);
    }
}
