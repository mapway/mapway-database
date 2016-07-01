package com.mapway.database2java.model.schema;

public class Function {
	private String Name;
	private Arguments arguments;
	public Function()
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
}
