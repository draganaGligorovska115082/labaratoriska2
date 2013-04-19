package mk.ukim.finki.jmm.todolist;

import java.util.Date;
import java.util.List;

import mk.ukim.finki.jmm.todolist.adapters.TodoItemsAdapter;
import mk.ukim.finki.jmm.todolist.database.ToDoDao;
import mk.ukim.finki.jmm.todolist.model.TodoItem;
import mk.ukim.finki.jmm.todolist.tasks.DownloadTask;
import mk.ukim.finki.jmm.todolist.tasks.DownloadWithProgressTask;
import mk.ukim.finki.jmm.todolist.utils.DatePickerUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class TodoList extends Activity {

	public static final String DEFAULT_LANG = "mk";

	static final String ACTION_TODO_DETAILS = "mk.ukim.finki.jmm.todolist.ACTION_TODO_DETAILS";

	private EditText mItemTitle;
	private DatePicker mItemPublishedDate;
	private ListView mTodoItemsList;
	private ToDoDao mDao;
	private TodoItemsAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo_list);
		loadViews();
		initList();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mDao = new ToDoDao(this, DEFAULT_LANG);
		mDao.open();
		loadData();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mDao.close();
	}

	private void loadData() {

		List<TodoItem> res = mDao.getAllItems();
		mAdapter.clear();
		mAdapter.addAll(res);
	}

	/**
	 * Inflates the views from the xml layout
	 */
	private void loadViews() {
		mItemTitle = (EditText) findViewById(R.id.todoName);
		mItemPublishedDate = (DatePicker) findViewById(R.id.todoDueDate);
		mTodoItemsList = (ListView) findViewById(R.id.todoItems);
	}

	private void initList() {
		mAdapter = new TodoItemsAdapter(this);
		mTodoItemsList.setAdapter(mAdapter);
		mTodoItemsList.setOnItemClickListener(mAdapter);
		mTodoItemsList
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> arg0,
							View parent, int position, long id) {
						Toast.makeText(TodoList.this, "Item long click",
								Toast.LENGTH_LONG).show();
						return true;
					}

				});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(1, 1, 1, "Refresh");
		menu.add(1, 2, 2, "Refresh with service");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == 1) {
			if (checkConnection()) {
				DownloadWithProgressTask task = new DownloadWithProgressTask(
						this, mAdapter);
				task.execute(getString(R.string.all_todo_items));

				return true;
			}

		} else if (item.getItemId() == 2) {
			if (checkConnection()) {
				createDialog();
				IntentFilter filter = new IntentFilter(
						DownloadTask.ITEMS_DOWNLOADED_ACTION);
				registerReceiver(new OnDownloadRefreshReceiver(), filter);
				startService(new Intent(this, DownloadService.class));
				return true;
			}
		}

		return false;
	}

	private boolean checkConnection() {

		ConnectivityManager connectivityMannager = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = connectivityMannager.getActiveNetworkInfo();
		if (netInfo == null) {
			showSettingsAlert();
			return false;
		} else {
			return true;
		}

	}

	public void showSettingsAlert() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

		// Setting Dialog Title
		alertDialog.setTitle("Internet settings");

		// Setting Dialog Message
		alertDialog
				.setMessage("No active connection. Do you want to go to settings menu?");

		// On pressing Settings button
		alertDialog.setPositiveButton("Settings",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface promptDialog, int which) {
						Intent intent = new Intent(
								Settings.ACTION_WIFI_SETTINGS);
						TodoList.this.startActivity(intent);
					}
				});

		// on pressing cancel button
		alertDialog.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface promptDialog, int which) {
						promptDialog.dismiss();

					}
				});

		// Showing Alert Message
		alertDialog.show();
	}

	private ProgressDialog loadingDialog;

	private void createDialog() {
		loadingDialog = new ProgressDialog(this);
		loadingDialog.setTitle(this.getResources().getString(
				R.string.download_title));
		loadingDialog.setMessage(this.getResources().getString(
				R.string.download_description));
		loadingDialog.setIndeterminate(true);
		loadingDialog.setCancelable(false);
	}

	public void explicit(View view) {
		startActivity(new Intent(this, TodoDetails.class));
	}

	public void implicit(View view) {
		Intent detailsIntent = new Intent(ACTION_TODO_DETAILS);
		detailsIntent.putExtra("time", (new Date()).getTime());
		startActivity(detailsIntent);
	}

	public void addTodoItem(View view) {

		TodoItem item = new TodoItem(mItemTitle.getText().toString(), false,
				DatePickerUtils.getDate(mItemPublishedDate));

		mAdapter.add(item);
		mDao.insert(item);
	}

	class OnDownloadRefreshReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {

			loadData();

			if (loadingDialog != null && loadingDialog.isShowing()) {
				loadingDialog.dismiss();
			}

			TodoList.this.unregisterReceiver(this);

		}
	}

}
