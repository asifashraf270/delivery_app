package com.example.khushbakht.grocerjin.adapters;

/**
 * Created by Khushbakht on 7/17/2017.
 */

public interface ItemTouchHelperAdapter {
    boolean onItemMove(int fromPosition, int toPosition);
    void onItemDismiss(int position);

}
