package com.mapway.database2java.model.schema;

public interface ITable {

	public abstract Columns getColumns();

	public abstract String getComment();

	public abstract void setComment(String comment);

	public abstract String getName();

	public abstract void setName(String name);

	public abstract String getOwner();

	public abstract void setOwner(String owner);

	public abstract String getJavaName();

	public abstract String getJavaNames();

	public abstract String getJavaAccessName();

	public abstract boolean hasPK();

	public abstract boolean hasAuto();

}