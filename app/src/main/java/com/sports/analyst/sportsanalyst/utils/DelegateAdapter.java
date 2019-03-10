package com.sports.analyst.sportsanalyst.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DelegateAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_STUB = -1;

    @NonNull
    protected final HashMap<Class, Integer> viewTypes = new HashMap<>();

    @NonNull
    protected final SparseArray<Delegate> delegates = new SparseArray<>();

    @NonNull
    protected final List<Object> items = new ArrayList<>(); // no restriction for now

    @NonNull
    private AdapterActionsBinder actionsBinder;

    public DelegateAdapter(Map<Class, Delegate> delegatesMap) {
        this(delegatesMap, new AdapterActionsBinder());
    }

    public DelegateAdapter(Map<Class, Delegate> delegatesMap, @NonNull AdapterActionsBinder actionsBinder) {
        this.actionsBinder = actionsBinder;
        int count = 0;
        for (Map.Entry<Class, Delegate> pair : delegatesMap.entrySet()) {
            this.viewTypes.put(pair.getKey(), count);
            this.delegates.append(count, pair.getValue());
            count++;
        }
        this.delegates.append(VIEW_TYPE_STUB, EmptyDelegate.INSTANCE); // для обработки моделей, для которых не установлен делегат
    }

    public Object getItem(int position) {
        return items.get(position);
    }

    @NonNull
    public List<Object> getItems() {
        return items;
    }

    public int getItemPosition(Object item) {
        return items.indexOf(item);
    }

    @Override
    public int getItemViewType(int position) {
        Integer viewType = viewTypes.get(getItem(position).getClass());
        return viewType != null ? viewType : VIEW_TYPE_STUB;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return delegates.get(viewType).createViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Object item = getItem(position);
        delegates.get(getItemViewType(position)).bind(holder, item);
        actionsBinder.bindActions(item, holder);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        int position = holder.getAdapterPosition();
        if (position >= 0) {
            delegates.get(getItemViewType(position)).onViewRecycled();
        }
    }


    public void setItems(@NonNull List<?> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void addItem(@NonNull Object item) {
        this.items.add(item);
        notifyItemInserted(items.size() - 1);
    }

    public void clearItems() {
        this.items.clear();
        notifyDataSetChanged();
    }

    public int indexOf(@Nullable Object item) {
        return this.items.indexOf(item);
    }

    public void removeItem(@NotNull Object item) {
        int position = this.items.indexOf(item);
        if (position > -1) {
            this.items.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void removeItemByPosition(int pos) {
        if (pos < this.items.size()) {
            this.items.remove(pos);
            notifyItemRemoved(pos);
        }
    }

    public void addItem(int pos, Object item) {
        this.items.add(pos, item);
        notifyItemInserted(pos);
    }

    public void removeItems(List<?> items) {
        if (items == null) return;
        this.items.removeAll(items);
        notifyDataSetChanged();
    }

    public void changeItem(@NotNull Object item) {
        int position = this.items.indexOf(item);
        if (position != -1) {
            notifyItemChanged(position);
        }
    }

    public void addItems(@NotNull List<?> items) {
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void addItems(int pos, @NotNull List items) {
        this.items.addAll(pos, items);
        notifyDataSetChanged();
    }

    public static class Builder {

        private final Map<Class, Delegate> delegates = new HashMap<>();

        private AdapterActionsBinder actionsBinder = new AdapterActionsBinder();

        public <M, H extends RecyclerView.ViewHolder> Builder addDelegate(Class<M> modelClass, Delegate<M, H> delegate) {
            delegates.put(modelClass, delegate);
            return this;
        }

        public <M, H extends RecyclerView.ViewHolder> Builder addDelegate(Class<M> modelClass, Delegate<M, H> delegate, AdapterAction<M, H> action) {
            delegates.put(modelClass, delegate);
            actionsBinder.addActionForModel(action, modelClass);
            return this;
        }

        public <M, H extends RecyclerView.ViewHolder> Builder addAction(Class<M> modelClass, AdapterAction<M, H> action) {
            actionsBinder.addActionForModel(action, modelClass);
            return this;
        }

        public DelegateAdapter build() {
            if (delegates.size() == 0) {
                throw new IllegalArgumentException("Необходимо добавить как минимум один делегат");
            }
            return new DelegateAdapter(delegates, actionsBinder);
        }
    }

    public static abstract class Delegate<M, H extends RecyclerView.ViewHolder> {

        public abstract H createViewHolder(@NonNull ViewGroup parent);

        public abstract void bind(H viewHolder, M model);

        public void onViewRecycled() {
        }
    }

    static final class EmptyDelegate extends Delegate<Object, RecyclerView.ViewHolder> {

        static final EmptyDelegate INSTANCE = new EmptyDelegate();

        private EmptyDelegate() {
        }

        @Override
        public RecyclerView.ViewHolder createViewHolder(@NonNull ViewGroup parent) {
            return new EmptyViewHolder(parent.getContext());
        }

        @Override
        public void bind(@NonNull RecyclerView.ViewHolder viewHolder, @NonNull Object model) {
// do nothing. it's empty
        }
    }

    static final class EmptyViewHolder extends RecyclerView.ViewHolder {
        EmptyViewHolder(Context context) {
            super(new View(context));
        }
    }

}