package com.mapway.database2java.model.schema;


public class Procedure {

	private String Name;
	private Arguments arguments;
	private String PackageName;
	public Procedure()
	{
		Name="";
		arguments=new Arguments();
	}
	
		public Arguments getArguments() {
		return arguments;
	}

	public void setArguments(Arguments arguments) {
		this.arguments = arguments;
	}

		public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}

	public String getPackageName() {
		return PackageName;
	}

	public void setPackageName(String packageName) {
		PackageName = packageName;
	}
}
