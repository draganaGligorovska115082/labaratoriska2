package mk.ukim.finki.jmm.todolist.utils;

import java.util.ArrayList;
import java.util.List;

import mk.ukim.finki.jmm.todolist.model.TodoItem;

import org.json.JSONArray;
import org.json.JSONObject;

public class OnToDoItemsDownloaded implements
		OnContentDownloaded<List<TodoItem>> {

	private List<TodoItem> items = new ArrayList<TodoItem>();

	@Override
	public void onContentDownloaded(String content, int httpStatus)
			throws Exception {
		JSONArray jsonItems = new JSONArray(content);

		for (int i = 0; i < jsonItems.length(); i++) {
			JSONObject jObj = (JSONObject) jsonItems.get(i);
			TodoItem item = new TodoItem();
			item.setTitle(jObj.getString("title"));
			item.setId(jObj.getLong("id"));
			item.setAvailable(jObj.getBoolean("isAvailable"));
			items.add(item);
		}

	}

	@Override
	public List<TodoItem> getResult() {
		return items;
	}

}
