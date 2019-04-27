package com.uniscope.getappinfo;

import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.uniscope.getappinfo.utils.XmlParserUtils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    
    private TextView tv_info;
    private Button btn_parser;
    private XmlParserUtils xmlParserUtils;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        xmlParserUtils = new XmlParserUtils(this);
        initViews();
    }
    
    private void initViews() {
        tv_info = (TextView) findViewById(R.id.tv_info);
        btn_parser = (Button) findViewById(R.id.btn_parser);
        btn_parser.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        btn_parser.setEnabled(false);
        Observable.create(new ObservableOnSubscribe<Integer>() {

            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                if (!emitter.isDisposed()) {
                    AssetManager asset = getAssets();
                    InputStream inputStream = asset.open("packages.xml");
                    Integer result = xmlParserUtils.parserPackageXml(
                            getApplicationContext(),inputStream,StandardCharsets.UTF_8.name());
                    emitter.onNext(result);
                }

            }
        }).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer integer) {
                Log.i("majun","onNext & integer="+integer);
                btn_parser.setEnabled(true);
                if (integer == 1) {
                    tv_info.setTextColor(Color.GREEN);
                    tv_info.setText("packages.xml parser success");
                } else {
                    tv_info.setTextColor(Color.RED);
                    tv_info.setText("packages.xml parser error");
                }

            }

            @Override
            public void onError(Throwable e) {
                tv_info.setTextColor(Color.RED);
                tv_info.setText("err:"+e.getMessage());
            }

            @Override
            public void onComplete() {
            }
        });
    }
}
