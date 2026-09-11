package com.treasure.basic.ui.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import com.treasure.basic.R;


public class FullLoadingView extends FrameLayout {

    public FullLoadingView(Context context) {
        this(context, null);
    }

    public FullLoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FullLoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.layout_full_loading, this, true);
        inflate.findViewById(R.id.view_bg).setOnClickListener(v -> {
        });
        hide();
    }

    public void show() {
        Context context = getContext();
        if (context instanceof Activity) {
            ((Activity) context).runOnUiThread(() -> FullLoadingView.this.setVisibility(VISIBLE));
        }
    }

    public void hide() {
        Context context = getContext();
        if (context instanceof Activity) {
            ((Activity) context).runOnUiThread(() -> FullLoadingView.this.setVisibility(GONE));
        }
    }
}
