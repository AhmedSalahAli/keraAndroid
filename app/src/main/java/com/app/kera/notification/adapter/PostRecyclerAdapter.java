package com.app.kera.notification.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.app.kera.R;

import com.app.kera.notification.model.NotificationItemUIModel;

import java.util.List;

public class PostRecyclerAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private boolean isLoaderVisible = false;
    private Context context;
    private CallBack callBack;
    private List<NotificationItemUIModel.NotificationModel> mPostItems;
    public PostRecyclerAdapter(List<NotificationItemUIModel.NotificationModel> postItems, Context context,CallBack callBack) {
        this.mPostItems = postItems;
        this.context = context;
        this.callBack = callBack;
    }
    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:

                return  new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notfy, parent, false));
            case VIEW_TYPE_LOADING:
                return new ProgressHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false));
            default:
                return null;
        }
    }
    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);

    }
    @Override
    public int getItemViewType(int position) {
        if (isLoaderVisible) {
            return position == mPostItems.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }
    @Override
    public int getItemCount() {
        return mPostItems == null ? 0 : mPostItems.size();
    }
    public void addItems(List<NotificationItemUIModel.NotificationModel> postItems) {
        mPostItems.addAll(postItems);
        notifyDataSetChanged();
    }
    public List<NotificationItemUIModel.NotificationModel> getItems() {
      return mPostItems;
    }
    public void addLoading() {
        isLoaderVisible = true;
        mPostItems.add(new NotificationItemUIModel.NotificationModel());
        notifyItemInserted(mPostItems.size() - 1);
    }
    public void removeLoading() {
        isLoaderVisible = false;
        int position = mPostItems.size() - 1;
        NotificationItemUIModel.NotificationModel item = getItem(position);
        if (item != null) {
            mPostItems.remove(position);
            notifyItemRemoved(position);
        }
    }
    public void clear() {
        mPostItems.clear();
        notifyDataSetChanged();
    }
    NotificationItemUIModel.NotificationModel getItem(int position) {
        return mPostItems.get(position);
    }
    public class ViewHolder extends BaseViewHolder {
        ImageView imageView52;
        TextView textView81,textView82,textView84;
        ConstraintLayout notfy_container;
        ViewHolder(View itemView) {
            super(itemView);


            imageView52 = itemView.findViewById(R.id.imageView52);
            textView81 = itemView.findViewById(R.id.textView81);
            textView82 = itemView.findViewById(R.id.textView82);
            textView84 = itemView.findViewById(R.id.textView84);
            notfy_container = itemView.findViewById(R.id.notfy_container);
            if (context!=null){
                itemView.setAnimation(
                        AnimationUtils.loadAnimation(
                                context,
                                R.anim.fade_scale
                        )
                );
            }
        }
        protected void clear() {
        }
        public void onBind(int position) {
            super.onBind(position);

            NotificationItemUIModel.NotificationModel model = mPostItems.get(position);

            Glide.with(context).load(model.getNotificationIcon()).error(R.drawable.ic_notification_circle_selected).into(imageView52);
            textView81.setText(model.getTitle());
            textView82.setText(model.getBody());
            textView84.setText(model.getDate());
            notfy_container.setOnClickListener(v -> {
                callBack.onItemClicked(model);
            });

        }
    }
    public class ProgressHolder extends BaseViewHolder {
        ProgressHolder(View itemView) {
            super(itemView);

        }
        @Override
        protected void clear() {
        }
    }
    public interface CallBack {
        void onItemClicked(
                NotificationItemUIModel.NotificationModel notificationModel
        );


    }
}