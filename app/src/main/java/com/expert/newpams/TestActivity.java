package com.expert.newpams;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.expert.net_framework.NetworkManager;
import com.expert.net_framework.bean.DownloadBaseDataBean;
import com.expert.net_framework.bean.DownloadPlanDataBean;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        testapi();
    }

    private void testapi() {

        //initdownloadbasicdata();
        initdownloadplandata();
    }

    private void initdownloadbasicdata() {
        NetworkManager.getInstance().Downloadbasicdata("hujd","123456")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DownloadBaseDataBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(DownloadBaseDataBean downloadBaseDataBean) {

                    }
                });
    }

    private void initdownloadplandata() {
        NetworkManager.getInstance().Downloadplandata("hujd","123456")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DownloadPlanDataBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(DownloadPlanDataBean downloadPlanDataBean) {

                    }
                });
    }

}
