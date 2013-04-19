package mk.ukim.finki.jmm.todolist;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class TodoDetails extends Activity {

	TextView hello;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intents);
		hello = (TextView) findViewById(R.id.textView1);
	}

	@Override
	protected void onResume() {
		super.onResume();

		hello.setText("" + new Date().getTime());
	}

	public void explicit(View view) {
		startActivity(new Intent(this, TodoDetails.class));
	}

	public void implicit(View view) {
		Intent detailsIntent = new Intent(TodoList.ACTION_TODO_DETAILS);
		detailsIntent.putExtra("time", (new Date()).getTime());
		startActivity(detailsIntent);
	}

}
