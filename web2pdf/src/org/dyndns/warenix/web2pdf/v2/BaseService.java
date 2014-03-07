package org.dyndns.warenix.web2pdf.v2;

/**
 * Pass this service object to {@link API#makeCall(String, String, Class)}
 * 
 * @author warenix
 * 
 */
public class BaseService {
	public String path;
	public Object payload;
	public Class<?> responseClass;

	public BaseService(String path, Class<?> responseClass) {
		this.path = path;
		this.responseClass = responseClass;
	}

	public void setPayload(Object payload) {
		this.payload = payload;
	}
}
