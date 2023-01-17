package com.doctogo;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.doctogo.model.Notification;
import com.doctogo.model.NotificationType;

public class NotificationListAdaptor
        extends ListAdapter<Notification, NotificationListAdaptor.NotificationMainViewHolder> {
    private static final String TAG = NotificationListAdaptor.class.getName();

    private static final DiffUtil.ItemCallback<Notification> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Notification>() {
        @Override
        public boolean areItemsTheSame(@NonNull Notification oldData,
                                       @NonNull Notification newData) {
            return oldData.getId().equals(newData.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Notification oldData,
                                          @NonNull Notification newData) {
            return oldData.getId().equals(newData.getId());
        }
    };

    public interface OnItemClickListener { // 1
        void onItemClicked(Notification n );
    }

    private OnItemClickListener clickListener;

    public NotificationListAdaptor() {
        super(DIFF_CALLBACK);
    }

    public NotificationListAdaptor(OnItemClickListener clickListener) { // 2
        super(DIFF_CALLBACK);
        this.clickListener = clickListener;
    }

    protected NotificationListAdaptor(@NonNull AsyncDifferConfig<Notification> config) {
        super(config);
    }

    @NonNull
    @Override
    public NotificationMainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.notification_item_layout, parent, false);
        return new NotificationMainViewHolder(view, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationMainViewHolder holder, int position) {
        holder.bindTo(getItem(position));
    }
    static class NotificationMainViewHolder extends RecyclerView.ViewHolder {
        private TextView textTitle;
        private TextView textDate;
        private Notification notification;

        NotificationMainViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitle);
            textDate = itemView.findViewById(R.id.textDate);
        }
        NotificationMainViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            itemView.setOnClickListener(v ->  listener.onItemClicked(notification));
            textTitle = itemView.findViewById(R.id.textTitle);
            textDate = itemView.findViewById(R.id.textDate);
        }

        void bindTo(Notification notification) {
            Log.i(TAG," got notification "+ notification);
            Log.i(TAG," got notification "+ notification.getStatus());

            switch(notification.getNotificationType()){
                case 0 :
                    textTitle.setText(NotificationType.HIGH_BLOOD_PRESSURE.toString()+  " : "+ notification.getBloodPressure());
                    break;
                case 1 :
                    textTitle.setText(NotificationType.LOW_BLOOD_PRESSURE.toString()+  " : "+ notification.getBloodPressure());
                    break;
                case 2 :
                    textTitle.setText(NotificationType.HIGH_BLOOD_OXYGEN.toString()+  " : "+ notification.getOxygenSaturation());
                    break;
                case 3 :
                    textTitle.setText(NotificationType.LOW_BLOOD_OXYGEN.toString()+  " : "+ notification.getOxygenSaturation());
                    break;
                case 4 :
                    textTitle.setText(NotificationType.HIGH_HEART_RATE.toString()+  " : "+ notification.getHeartBeat());
                    break;
                case 5 :
                    textTitle.setText(NotificationType.LOW_HEART_RATE.toString()+  " : "+ notification.getHeartBeat());
                    break;
            }
            textTitle.setText(textTitle.getText() + " : "+ notification.getStatus()) ;
            if(notification.getCreatedDate() != null)
                textDate.setText(notification.getCreatedDate());
            else
                textDate.setText("");
        }
    }
}
