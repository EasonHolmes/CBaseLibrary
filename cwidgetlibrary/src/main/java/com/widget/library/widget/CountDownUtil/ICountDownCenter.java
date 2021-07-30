package com.widget.library.widget.CountDownUtil;


import java.util.Observer;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by cuiyang on 2021/6/4.
 */
public interface ICountDownCenter {
    void addObserver(Observer observer);
    void deleteObservers();
    void startCountdown();
    void stopCountdown();
    boolean containHolder(Observer observer);
    void notifyAdapter();
    void bindRecyclerView(RecyclerView recyclerView);
}
