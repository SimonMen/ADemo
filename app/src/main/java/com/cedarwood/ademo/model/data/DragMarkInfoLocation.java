package com.cedarwood.ademo.model.data;

import java.io.Serializable;

public class DragMarkInfoLocation implements Serializable {

	


	
/**
	 * 
	 */
	private static final long serialVersionUID = 2119330751058034438L;
//	"building_id": 10371,
//    "city_id": 1,
//    "left": 213,
//    "title": "测试楼栋三",
//    "top": 215
	
	
	
	private int building_id;
	private int city_id;
	private int left;
	private String title;
	private int top;
	
	private String localId;
	
	
	
	
//	private boolean show = true;

	public String getLocalId() {
		return localId;
	}
	public void setLocalId(String localId) {
		this.localId = localId;
	}
	//	public boolean isShow() {
//		return show;
//	}
//	public void setShow(boolean show) {
//		this.show = show;
//	}
	public int getBuilding_id() {
		return building_id;
	}
	public void setBuilding_id(int building_id) {
		this.building_id = building_id;
	}
	public int getCity_id() {
		return city_id;
	}
	public void setCity_id(int city_id) {
		this.city_id = city_id;
	}
	public int getLeft() {
		return left;
	}
	public void setLeft(int left) {
		this.left = left;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getTop() {
		return top;
	}
	public void setTop(int top) {
		this.top = top;
	}
	
	
	
	

}
