package pl.opencaching.android.ui.main;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import pl.opencaching.android.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Volfram on 15.07.2017.
 */

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {
    static int HEADER = 0;
    private static int GROUP = 1;
    static int ITEM = 2;
    static int SEPARATOR = 3;

    private List<MenuItem> items;
    private OnMenuItemCheckedListener listener;
    private int newSelectedPosition;

    MenuAdapter(List<MenuItem> items, OnMenuItemCheckedListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        ViewHolder holder = null;

        if (viewType == HEADER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu_header, null);
            holder = new ViewHolder(view);
        } else if (viewType == GROUP) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu, null);
            holder = new ViewHolder(view);
            holder.iconImage.setVisibility(View.GONE);
        } else if (viewType == ITEM) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu, null);
            holder = new ViewHolder(view);
        }else if (viewType == SEPARATOR) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu_separator, null);
            holder = new ViewHolder(view);
        }
        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        MenuItem item = items.get(position);
        return item.type;
    }

    private MenuItem findItemWithTag(int tag){
        for (MenuItem item : items){
            if (item.tag == tag){
                return item;
            }
        }
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final MenuItem item = items.get(position);
        holder.iconImage.setImageResource(item.iconResId);
        Context context = holder.parent.getContext();
        int accentColor = ContextCompat.getColor(context, R.color.colorPrimaryDark);
        int grayColor = ContextCompat.getColor(context, R.color.black);
        if (item.type == ITEM && position == newSelectedPosition){
            holder.parent.setBackgroundColor(ContextCompat.getColor(context,R.color.accent_gray));
            holder.iconImage.getDrawable().setColorFilter(accentColor, PorterDuff.Mode.SRC_ATOP);
            holder.titleTextView.setTextColor(accentColor);
        } else {
            holder.parent.setBackgroundColor(Color.TRANSPARENT);
            if(item.type != HEADER) {
                holder.titleTextView.setTextColor(grayColor);
            }
            Drawable drawable = holder.iconImage.getDrawable();
            if (drawable != null) {
                drawable.setColorFilter(grayColor, PorterDuff.Mode.SRC_ATOP);
            }
        }

        if (item.type == GROUP){
            holder.parent.setBackgroundColor(ContextCompat.getColor(context,R.color.white));
            holder.iconImage.setColorFilter(null);
        }
        if(item.type != SEPARATOR) {
            holder.titleTextView.setText(item.title);
        }

        holder.parent.setTag(item.tag);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItemSelected(int tag) {
        int oldSelectedPosition = newSelectedPosition;
        MenuItem item = findItemWithTag(tag);
        newSelectedPosition = items.indexOf(item);
        notifyItemChanged(newSelectedPosition);
        notifyItemChanged(oldSelectedPosition);
        listener.onMenuItemChecked(tag);
    }

    public static class MenuItem {
        private int tag;
        private int type;
        private int title;
        private int iconResId;

        public MenuItem(int type, int title, int iconResId) {
            this.type = type;
            this.title = title;
            this.iconResId = iconResId;
            this.tag = title;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.icon_image)
        ImageView iconImage;
        @BindView(R.id.titleTextView)
        TextView titleTextView;
        @BindView(R.id.parent)
        ConstraintLayout parent;

        @OnClick(R.id.parent)
        public void onClick(View view){
            if (items.get(getAdapterPosition()).type == ITEM) {
                setItemSelected((Integer) view.getTag());
            }
        }

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnMenuItemCheckedListener {
        void onMenuItemChecked (int tag);
    }
}
