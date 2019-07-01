package myapp.schedule.misha.myapplication.module.schedule.edit.page.dialogCopy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import myapp.schedule.misha.myapplication.R;
import myapp.schedule.misha.myapplication.SimpleItemClickListener;
import myapp.schedule.misha.myapplication.entity.CopyLesson;
import myapp.schedule.misha.myapplication.entity.SimpleItem;


public class DialogCopyFragmentAdapter extends RecyclerView.Adapter<DialogCopyFragmentAdapter.ViewHolder> {

    private List<CopyLesson> listItems;
    private SimpleItemClickListener itemClickListener;

    public DialogCopyFragmentAdapter(ArrayList<CopyLesson> items, SimpleItemClickListener simpleItemClickListener) {
        this.listItems = items;
        this.itemClickListener = simpleItemClickListener;
    }

    @Override
    public DialogCopyFragmentAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_copy_lesson, parent, false);
        return new ViewHolder(view);
    }

    public void setLessonList(List<CopyLesson> lessonList) {
        this.listItems = lessonList;
    }


    @Override
    public void onBindViewHolder(@NotNull ViewHolder holder, final int position) {
        holder.onBindView(position);
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView day;
        private final TextView timeLesson;
        private final ImageView imageDelete;

        private ViewHolder(View view) {
            super(view);
            day = view.findViewById(R.id.day);
            timeLesson = view.findViewById(R.id.timeLesson);
            imageDelete = view.findViewById(R.id.imageDelete);
            imageDelete.setOnClickListener(this);
        }

        private void onBindView(int position) {
            CopyLesson item = listItems.get(position);
            day.setText(item.getDay());
            timeLesson.setText(item.getTimeLesson());
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onItemClick(getAdapterPosition(), v);
        }
    }
}

