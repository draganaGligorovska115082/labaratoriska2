package mk.ukim.finki.jmm.todolist.tasks;

import java.util.List;

import mk.ukim.finki.jmm.todolist.R;
import mk.ukim.finki.jmm.todolist.adapters.TodoItemsAdapter;
import mk.ukim.finki.jmm.todolist.model.TodoItem;
import mk.ukim.finki.jmm.todolist.utils.Downloader;
import mk.ukim.finki.jmm.todolist.utils.OnContentDownloaded;
import mk.ukim.finki.jmm.todolist.utils.OnToDoItemsDownloaded;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.Settings;
import android.widget.Toast;

public class DownloadWithProgressTask extends
		AsyncTask<String, Void, List<TodoItem>> {

	private Exception exception = null;
	private ProgressDialog loadingDialog;
	private TodoItemsAdapter adapter;
	protected Context context;
	Handler handler;

	public DownloadWithProgressTask(Context context, TodoItemsAdapter adapter) {
		this.context = context;
		this.adapter = adapter;
		handler = new Handler();
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
				ex.printStackTrace();
				return null;
			}
		}
	}

	@Override
	protected void onProgressUpdate(Void... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		createDialog();

	}

	

	@Override
	protected void onPostExecute(List<TodoItem> result) {
		super.onPostExecute(result);
		if (exception != null) {
			Toast.makeText(context, "Error: " + exception.getMessage(),
					Toast.LENGTH_LONG).show();
		} else {
			adapter.clear();
			adapter.addAll(result);
		}
		dismiss();
	}

	private void createDialog() {
		loadingDialog = new ProgressDialog(context);
		loadingDialog.setTitle(context.getResources().getString(
				R.string.download_title));
		loadingDialog.setMessage(context.getResources().getString(
				R.string.download_description));
		loadingDialog.setIndeterminate(true);
		loadingDialog.setCancelable(false);
	}

	public void dismiss() {
		if (loadingDialog != null && loadingDialog.isShowing()) {
			loadingDialog.dismiss();
		}
	}

}
