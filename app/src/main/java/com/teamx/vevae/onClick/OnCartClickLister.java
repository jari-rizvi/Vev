package com.teamx.vevae.onClick;

import android.widget.TextView;

public interface OnCartClickLister {

    void addProductClick(int position, TextView itemCount, TextView productTotal);
    void subProductClick(int position,TextView itemCount, TextView productTotal);
    void onProductClick(int position);
    void onDeleteProductClick(int position);

}
