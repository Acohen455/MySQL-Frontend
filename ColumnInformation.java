
public class ColumnInformation {
	int size;
	String name;
	String dataType;
	
	
	
	
	
	//constructors
	public ColumnInformation() {
		super();
	}
	
	
	
	public ColumnInformation(int size, String name, String dataType) {
		super();
		this.size = size;
		this.name = name;
		this.dataType = dataType;
	}
	
	//getters and setters
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String datatype) {
		this.dataType = datatype;
	}



	
	
	
	
	
	
	
	
	
	
	
	
	
}
