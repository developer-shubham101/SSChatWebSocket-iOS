package in.newdevpoint.ssnodejschat.realmSample;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Task extends RealmObject {
	@PrimaryKey
	private String name;
	@Required
	private String status = TaskStatus.Open.name();

	public Task(String _name) {
		this.name = _name;
	}

	public Task() {
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(TaskStatus status) {
		this.status = status.name();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}