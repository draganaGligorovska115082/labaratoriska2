package mk.ukim.finki.jmm.todolist.tasks;

import java.util.List;

import mk.ukim.finki.jmm.todolist.R;
import mk.ukim.finki.jmm.todolist.TodoList;
import mk.ukim.finki.jmm.todolist.adapters.TodoItemsAdapter;
import mk.ukim.finki.jmm.todolist.database.ToDoDao;
import mk.ukim.finki.jmm.todolist.model.TodoItem;
import mk.ukim.finki.jmm.todolist.utils.Downloader;
import mk.ukim.finki.jmm.todolist.utils.OnContentDownloaded;
import mk.ukim.finki.jmm.todolist.utils.OnToDoItemsDownloaded;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.Toast;

public class DownloadTask extends AsyncTask<String, Void, List<TodoItem>> {

	public static final String ITEMS_DOWNLOADED_ACTION = "mk.ukim.finki.jmm.todolist.ITEMS_DOWNLOADED_ACTION";
	private Exception exception = null;
	protected Context context;

	public DownloadTask(Context context) {
		this.context = context;
	}

	@Override
	protected List<TodoItem> doInBackground(String... params) {
		if (params.length < 1) {
			exception = new IllegalArgumentException(
					"At least one argument for the download url expected. ");
			return null;
		} else {

			String url = params[0];
			OnContentDownloaded<List<TodoItem>> handler = new OnToDoItemsDownloaded();
			try {
				Downloader.getFromUrl(url, handler);
				publishProgress(null);
				return handler.getResult();
			} catch (Exception ex) {
				exception = ex;
				return null;
			}
		}
	}

	@Override
	protected void onPostExecute(List<TodoItem> result) {
		super.onPostExecute(result);
		if (exception != null) {
			Toast.makeText(context, "Error: " + exception.getMessage(),
					Toast.LENGTH_LONG).show();
		} else {

			ToDoDao dao = new ToDoDao(context, TodoList.DEFAULT_LANG);
			dao.open();

			for (TodoItem item : result) {
				dao.insert(item);
			}
			dao.close();
			Intent intent=new Intent(ITEMS_DOWNLOADED_ACTION);
			context.sendBroadcast(intent);

		}
	}

}
