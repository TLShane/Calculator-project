package com.example.calculator_lawler;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

public class CustomAdapter extends BaseAdapter {
    Context context;
    String numbers[];
    private LayoutInflater mInflator;
    public CustomAdapter(Context applicationContext, String[] numbers) {
        this.context = applicationContext;
        this.numbers = numbers;
        mInflator = (LayoutInflater) context.getSystemService((Context.LAYOUT_INFLATER_SERVICE));
    }
    @Override
    public int getCount() {
        return numbers.length;
    }

    @Override
    public Object getItem(int i) {
        return numbers[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View squareview, ViewGroup viewGroup) {
        squareview = mInflator.inflate(R.layout.activity_gridview,viewGroup,false); // inflate the layout
        TextView val = (TextView) squareview.findViewById(R.id.textView);
        val.setText(numbers[i]);
        return squareview;
}
}
