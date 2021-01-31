package in.newdevpoint.ssnodejschat.model;

import java.util.HashMap;

public class ContactModel {
	private boolean isSelected = false;
	private String first_name;
	private String middle_name;
	private String last_name;
	private String mobile;

	public ContactModel(String first_name, String middle_name, String last_name, String mobile) {
		this.first_name = first_name;
		this.middle_name = middle_name;
		this.last_name = last_name;
		this.mobile = mobile;
	}

	public ContactModel() {
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean selected) {
		isSelected = selected;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getMiddle_name() {
		return middle_name;
	}

	public void setMiddle_name(String middle_name) {
		this.middle_name = middle_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getName() {
		return first_name + " " + last_name;
	}

	public HashMap getList() {
		HashMap<String, Object> tmpList = new HashMap<>();
		tmpList.put("first_name", first_name);
		tmpList.put("middle_name", middle_name);
		tmpList.put("last_name", last_name);
		tmpList.put("mobile", mobile);
		return tmpList;
	}
}