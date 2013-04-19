package mk.ukim.finki.jmm.todolist.model;

import java.util.Date;

public class TodoItem {

	private Long id;

	private String title;
	private boolean isAvailable;
	private Date publishedDate;

	public TodoItem() {
	}

	public TodoItem(String title, boolean isAvailable, Date publishedDate) {
		super();
		this.title = title;
		this.isAvailable = isAvailable;
		this.publishedDate = publishedDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isAvailable() {
		return isAvailable;
	}

	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	public Date getPublishedDate() {
		return publishedDate;
	}

	public void setPublishedDate(Date publishedDate) {
		this.publishedDate = publishedDate;
	}

}
