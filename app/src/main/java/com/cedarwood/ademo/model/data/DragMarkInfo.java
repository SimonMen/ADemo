package com.cedarwood.ademo.model.data;

import java.io.Serializable;
import java.util.ArrayList;

public class DragMarkInfo implements Serializable {

	


	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4802586203048138146L;
//	"louzuo_title": "asasfdsf",
//    "map_list": [],
//    "photo_height": 768,
//    "photo_id": 368292913,
//    "photo_url": "http://i1.f.itc.cn/upload/bj/36830/a_368292913.jpg",
//    "photo_width": 1024
	
	
	
	private int photo_id;
	private String louzuo_title;
	private int photo_height;
	private String photo_url;
	private int photo_width;
	private boolean uploadFlag;
	
	
	private ArrayList<DragMarkInfoLocation> map_list;

	public int getPhoto_id() {
		return photo_id;
	}

	public void setPhoto_id(int photo_id) {
		this.photo_id = photo_id;
	}

	public String getLouzuo_title() {
		return louzuo_title;
	}

	public void setLouzuo_title(String louzuo_title) {
		this.louzuo_title = louzuo_title;
	}

	public int getPhoto_height() {
		return photo_height;
	}

	public void setPhoto_height(int photo_height) {
		this.photo_height = photo_height;
	}

	public String getPhoto_url() {
		return photo_url;
	}

	public void setPhoto_url(String photo_url) {
		this.photo_url = photo_url;
	}

	public int getPhoto_width() {
		return photo_width;
	}

	public void setPhoto_width(int photo_width) {
		this.photo_width = photo_width;
	}

	public ArrayList<DragMarkInfoLocation> getMap_list() {
		return map_list;
	}

	public void setMap_list(ArrayList<DragMarkInfoLocation> map_list) {
		this.map_list = map_list;
	}

	public boolean isUploadFlag() {
		return uploadFlag;
	}

	public void setUploadFlag(boolean uploadFlag) {
		this.uploadFlag = uploadFlag;
	}

	
	
	
	
	
	

}
