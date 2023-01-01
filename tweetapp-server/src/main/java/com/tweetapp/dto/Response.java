package com.tweetapp.dto;

public class Response {
	private String status;
	private String code;
	private String message;
	private Object data;

	public Response() {
		super();
	}

	public Response(String status, String code) {
		super();
		this.status = status;
		this.code = code;
	}

	public Response(String status, String code, String message) {
		super();
		this.status = status;
		this.code = code;
		this.message = message;
	}

	public Response(String status, String code, String message, Object data) {
		super();
		this.status = status;
		this.code = code;
		this.message = message;
		this.data = data;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "Response [status=" + status + ", code=" + code + ", message=" + message + ", data=" + data + "]";
	}

}
