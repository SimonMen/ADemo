package com.cedarwood.ademo;

public class EventConsts {
	// 消息 class 列表

	// public static class CaseListUpdate extends BaseEvent<Filter>{}
	// public static class LocationEvent extends BaseEvent<Object>{}


	// 消息基类
	public static class BaseEvent<T> {
		public T data;

		public BaseEvent() {

		}

		public BaseEvent(T data) {
			this.data = data;
		}

		// public static BaseEvent CreateFromJsonString(String jsonString,
		// Class<? extends BaseEvent> jsonClass) {
		// if (jsonString == null) return null;
		// try {
		// BaseEvent event = NetUtil.gson.fromJson(jsonString, jsonClass);
		// return event;
		// } catch (Exception e) {
		// return null;
		// }
		// }

		public T getData() {
			return data;
		}

		public BaseEvent setData(T data) {
			this.data = data;
			return this;
		}

		// public String toJsonString() {
		// try {
		// return NetUtil.gson.toJson(this);
		// } catch (Exception e) {
		// return null;
		// }
		// }
	}
}
