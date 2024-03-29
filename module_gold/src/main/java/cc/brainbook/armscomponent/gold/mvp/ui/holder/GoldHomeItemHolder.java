/*
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.brainbook.armscomponent.gold.mvp.ui.holder;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.utils.ArmsUtils;

import butterknife.BindView;
import io.reactivex.Observable;
import cc.brainbook.armscomponent.commonsdk.imgaEngine.config.CommonImageConfigImpl;
import cc.brainbook.armscomponent.gold.R;
import cc.brainbook.armscomponent.gold.R2;
import cc.brainbook.armscomponent.gold.mvp.model.entity.GoldListBean;

/**
 * ================================================
 * 展示 {@link BaseHolder} 的用法
 * <p>
 * Created by JessYan on 9/4/16 12:56
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public class GoldHomeItemHolder extends BaseHolder<GoldListBean> {

    @BindView(R2.id.iv_avatar)
    ImageView mAvatar;
    @BindView(R2.id.tv_name)
    TextView mName;
    private AppComponent mAppComponent;
    private ImageLoader mImageLoader;//用于加载图片的管理类,默认使用 Glide,使用策略模式,可替换框架

    public GoldHomeItemHolder(View itemView) {
        super(itemView);
        //可以在任何可以拿到 Context 的地方,拿到 AppComponent,从而得到用 Dagger 管理的单例对象
        mAppComponent = ArmsUtils.obtainAppComponentFromContext(itemView.getContext());
        mImageLoader = mAppComponent.imageLoader();
    }

    @Override
    public void setData(GoldListBean data, int position) {
        Observable.just(data.getTitle())
                .subscribe(s -> mName.setText(s));

        if (data.getScreenshot() != null && !TextUtils.isEmpty(data.getScreenshot().getUrl())) {
            //itemView 的 Context 就是 Activity, Glide 会自动处理并和该 Activity 的生命周期绑定
            mImageLoader.loadImage(itemView.getContext(),
                    CommonImageConfigImpl
                            .builder()
                            .url(data.getScreenshot().getUrl())
                            .imageView(mAvatar)
                            .build());
        } else {
            mAvatar.setImageResource(R.mipmap.gold_ic_logo);
        }
    }

    @Override
    protected void onRelease() {
        mImageLoader.clear(mAppComponent.application(), CommonImageConfigImpl.builder()
                .imageViews(mAvatar)
                .build());
    }
}
