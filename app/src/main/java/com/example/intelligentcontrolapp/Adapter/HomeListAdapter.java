package com.example.intelligentcontrolapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.intelligentcontrolapp.R;

public class HomeListAdapter extends BaseAdapter {
    private final Context context;
    private final String[] items;
    private int selectedPosition = 0; // 默认选中1

    private OnHomeSelectedListener onHomeSelectedListener;

    public HomeListAdapter(Context context, String[] items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public Object getItem(int position) {
        return items[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.home_list_item, parent, false);
            holder = new ViewHolder();
            holder.radioButton = convertView.findViewById(R.id.radio_button);
            holder.textView = convertView.findViewById(R.id.spinner_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textView.setText(items[position]);
        holder.radioButton.setChecked(position == selectedPosition);

        View.OnClickListener clickListener = v -> {
            selectedPosition = position;
            notifyDataSetChanged();
            if (onHomeSelectedListener != null) {
                onHomeSelectedListener.onHomeSelected(items[position],position);
            }
        };

        holder.radioButton.setOnClickListener(clickListener);
        holder.textView.setOnClickListener(clickListener);

        return convertView;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }
    public void setSelectedPosition(int position) {
        selectedPosition = position;
        notifyDataSetChanged();
    }

    public void setOnHomeSelectedListener(OnHomeSelectedListener listener) {
        this.onHomeSelectedListener = listener;
    }

    public interface OnHomeSelectedListener {
        void onHomeSelected(String home,int position);
    }

    private static class ViewHolder {
        RadioButton radioButton;
        TextView textView;
    }
}
