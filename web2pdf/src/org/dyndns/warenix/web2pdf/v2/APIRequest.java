package org.dyndns.warenix.web2pdf.v2;

public class APIRequest {
	public String path;
	public Object payload;
	public Class<?> responseClass;

	public APIRequest(String path, Object payload, Class<?> responseClass) {
		this.path = path;
		this.payload = payload;
		this.responseClass = responseClass;
	}
}