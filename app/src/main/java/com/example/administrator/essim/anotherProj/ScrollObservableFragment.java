package com.example.administrator.essim.anotherProj;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * @author linxiao
 * @version 1.0.0
 */
public abstract class ScrollObservableFragment extends Fragment {


    private OnScrollChangedListener scrollChangedListener;

    public void setOnScrollChangedListener(OnScrollChangedListener listener) {
        this.scrollChangedListener = listener;
    }

    protected void doOnScrollChanged(int scrolledX, int scrolledY, int dx, int dy) {
        if (scrollChangedListener != null) {
            scrollChangedListener.onScrollChanged(this, scrolledX, scrolledY, dx, dy);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public abstract void setScrolledY(int scrolledY);

    public interface OnScrollChangedListener {
        void onScrollChanged(ScrollObservableFragment fragment, int scrolledX, int scrolledY, int dx, int dy);
    }
}
