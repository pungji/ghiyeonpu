package Notification;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.class_room_professor_identify_app.R;

import java.util.List;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ViewHolder> {
    private List<String> resultList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textMessage;

        public ViewHolder(View view) {
            super(view);
            textMessage = view.findViewById(R.id.textMessage);
        }
    }

    public ResultAdapter(List<String> resultList) {
        this.resultList = resultList;
    }

    @Override
    public ResultAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textMessage.setText(resultList.get(position));
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    public void addMessage(String msg) {
        resultList.add(msg);
        notifyItemInserted(resultList.size() - 1);
    }
}
