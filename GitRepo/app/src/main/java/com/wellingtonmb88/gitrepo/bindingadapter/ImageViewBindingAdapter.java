package com.wellingtonmb88.gitrepo.bindingadapter;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.wellingtonmb88.gitrepo.R;

public class ImageViewBindingAdapter {

    @BindingAdapter("url")
    public static void setUrl(final ImageView view, String url) {
        if (url != null) {
            Glide.with(view.getContext())
                    .load(url)
                    .placeholder(R.mipmap.ic_anonymous)
                    .listener(new RequestListener<String, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    view.setImageDrawable(resource);
                    return false;
                }
            }).preload();
        }
    }
}
