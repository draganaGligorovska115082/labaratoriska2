package mk.ukim.finki.jmm.todolist.database;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mk.ukim.finki.jmm.todolist.model.TodoItem;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ToDoDao {
	// Database fields
	private SQLiteDatabase database;
	private ToDoDbOpenHelper dbHelper;
	private String[] allColumns = { ToDoDbOpenHelper.COLUMN_ID,
			ToDoDbOpenHelper.COLUMN_TITLE, ToDoDbOpenHelper.COLUMN_IS_AVAILABLE,
			ToDoDbOpenHelper.COLUMN_SUBTITLE };

	public ToDoDao(Context context, String lang) {
		dbHelper = new ToDoDbOpenHelper(context, lang);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		database.close();
		dbHelper.close();
	}

	public boolean insert(TodoItem item) {

		if (item.getId() != null) {
			return update(item);
		}

		long insertId = database.insert(ToDoDbOpenHelper.TABLE_NAME, null,
				itemToContentValues(item));

		if (insertId > 0) {
			item.setId(insertId);
			return true;
		} else {
			return false;
		}

	}

	public boolean update(TodoItem item) {
		long numRowsAffected = database.update(ToDoDbOpenHelper.TABLE_NAME,
				itemToContentValues(item), ToDoDbOpenHelper.COLUMN_ID + " = "
						+ item.getId(), null);
		return numRowsAffected > 0;
	}

	public List<TodoItem> getAllItems() {
		List<TodoItem> items = new ArrayList<TodoItem>();

		Cursor cursor = database.query(ToDoDbOpenHelper.TABLE_NAME, allColumns,
				null, null, null, null, null);

		if (cursor.moveToFirst()) {
			do {
				items.add(cursorToItem(cursor));
			} while (cursor.moveToNext());
		}
		cursor.close();
		return items;
	}

	public TodoItem getById(long id) {

		Cursor cursor = database
				.query(ToDoDbOpenHelper.TABLE_NAME, allColumns,
						ToDoDbOpenHelper.COLUMN_ID + " = " + id, null, null,
						null, null);
		try {
			if (cursor.moveToFirst()) {
				return cursorToItem(cursor);
			} else {
				// no items found
				return null;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			cursor.close();
		}

	}

	protected TodoItem cursorToItem(Cursor cursor) {
		TodoItem item = new TodoItem();
		item.setId(cursor.getLong(cursor
				.getColumnIndex(ToDoDbOpenHelper.COLUMN_ID)));

		item.setTitle(cursor.getString(cursor
				.getColumnIndex(ToDoDbOpenHelper.COLUMN_TITLE)));

		item.setAvailable(1 == cursor.getInt((cursor
				.getColumnIndex(ToDoDbOpenHelper.COLUMN_IS_AVAILABLE))));

		item.setPublishedDate(new Date(cursor.getLong(cursor
				.getColumnIndex(ToDoDbOpenHelper.COLUMN_SUBTITLE))));

		return item;
	}

	protected ContentValues itemToContentValues(TodoItem item) {
		ContentValues values = new ContentValues();
		if (item.getId() != null) {
			values.put(ToDoDbOpenHelper.COLUMN_ID, item.getId());
		}
		values.put(ToDoDbOpenHelper.COLUMN_TITLE, item.getTitle());
		values.put(ToDoDbOpenHelper.COLUMN_IS_AVAILABLE, item.isAvailable() ? 1 : 0);
		values.put(ToDoDbOpenHelper.COLUMN_SUBTITLE, item.getPublishedDate()
				.getTime());
		return values;
	}
}
