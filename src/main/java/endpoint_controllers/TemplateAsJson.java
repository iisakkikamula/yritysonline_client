package endpoint_controllers;

import java.io.Serializable;
import java.util.List;
import java.util.Vector;

public class TemplateAsJson implements Serializable{
	private static final long serialVersionUID = 1L;

	List<String> rows;

	public List<String> getRows() {
		if(rows == null) {
			rows = new Vector<String>();
		}
		return rows;
	}

	public void setRows(List<String> rows) {
		this.rows = rows;
	}
	
}
