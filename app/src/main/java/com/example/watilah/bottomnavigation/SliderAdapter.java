package com.example.watilah.onboardingapp;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter (Context context){
        this.context = context;
    }

    // Arrays

    public int[] slide_images = {
            R.drawable.ah,
            R.drawable.wf_logox,
            R.drawable.assingment,
            R.drawable.sfiti
    };

    public String[] slide_heading = {
            "ASSIGNMENT HELP",
            "WATILAH FELIX",
            "ASSIGNMENT",
            "SHARE FITI"
    };

    public String[] slide_description = {
            "Assignment Help is a Writers Club that helps  students in doing their assignment.",
            "Watilah Felix is an IT student at Multimedia University of Kenya.",
            "Assignment displays the completed work.",
            "Share Fiti is the Social Media Platform at Multimedia University of Kenya."
    };

    @Override
    public int getCount() {
        return slide_heading.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);

        ImageView slideImageView = view.findViewById(R.id.slide_image);
        TextView slideHeading = view.findViewById(R.id.slider_heading);
        TextView slideDescription = view.findViewById(R.id.slide_desc);

        slideImageView.setImageResource(slide_images[position]);
        slideHeading.setText(slide_heading[position]);
        slideDescription.setText(slide_description[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}
