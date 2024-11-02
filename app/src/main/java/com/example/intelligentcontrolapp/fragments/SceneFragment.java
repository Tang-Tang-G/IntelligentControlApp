package com.example.intelligentcontrolapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.fragment.app.Fragment;

import com.example.intelligentcontrolapp.R;
import com.example.intelligentcontrolapp.activities.CreateSceneActivity;

import java.util.ArrayList;
import java.util.List;


public class SceneFragment extends Fragment {
    private ImageButton scene_add;
    private View rootview;
private LinearLayout father;

private List<TextView> children;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_scene, container, false);
        //初始化控件
      scene_add = rootview.findViewById(R.id.ib_scene_add);
        //跳转到添加设备页面
        father = rootview.findViewById(R.id.ll_father);
        children = new ArrayList<>();
        scene_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(),CreateSceneActivity.class);
//                startActivity(intent);
                AddScene("123123");
            }
        });

        return rootview;
    }
private void AddScene(String scene_name)
{
    TextView text = new TextView(getContext());
//设置参数
    LinearLayout.LayoutParams text_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 250,1.0f);
    text.setLayoutParams(text_params);
    text.setTextSize(25);
    text.setGravity(Gravity.CENTER);
    text.setText(scene_name);

    children.add(text);
    father.addView(text);
}

}