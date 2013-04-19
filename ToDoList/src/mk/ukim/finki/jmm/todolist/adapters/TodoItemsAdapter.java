package mk.ukim.finki.jmm.todolist.adapters;

import java.util.ArrayList;
import java.util.List;

import mk.ukim.finki.jmm.todolist.R;
import mk.ukim.finki.jmm.todolist.model.TodoItem;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TodoItemsAdapter extends BaseAdapter implements
		OnItemClickListener {

	private List<TodoItem> items;
	private Context ctx;
	private LayoutInflater inflater;

	public TodoItemsAdapter(Context ctx) {
		items = new ArrayList<TodoItem>();
		this.ctx = ctx;
		inflater = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public TodoItemsAdapter(List<TodoItem> items, Context ctx) {
		this.items = items;
		this.ctx = ctx;
		inflater = (LayoutInflater) this.ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	class TodoHoler {
		public RelativeLayout itemLayout;
		public TextView title;
		public TextView publishedDate;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		TodoItem item = items.get(position);
		TodoHoler holder = null;
		if (convertView == null) {
			holder = new TodoHoler();
			holder.itemLayout = (RelativeLayout) inflater.inflate(
					R.layout.item_todo, null);

			holder.title = (TextView) holder.itemLayout
					.findViewById(R.id.todoName);
			holder.publishedDate = (TextView) holder.itemLayout
					.findViewById(R.id.todoDate);
			convertView = holder.itemLayout;
			convertView.setTag(holder);

		}

		holder = (TodoHoler) convertView.getTag();

		holder.title.setText(item.getTitle());
		holder.publishedDate.setText(item.getPublishedDate().toString());

		
		return convertView;
	}

	public void add(TodoItem item) {
		items.add(item);
		notifyDataSetChanged();
	}
	
	public void addAll(List<TodoItem> items) {
		this.items.addAll(items);
		notifyDataSetChanged();
	}
	
	public void clear(){
		items.clear();
		notifyDataSetInvalidated();
	}
	

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		TodoItem item = items.get(position);
		item.setAvailable(!item.isAvailable());
		notifyDataSetChanged();
	}

}
