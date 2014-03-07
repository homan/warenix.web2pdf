package org.dyndns.warenix.web2pdf.v2;

public class ConvertService extends BaseService {
	public ConvertService(String url) {
		super(API.URL_CONVERT, ConvertResult.class);

		ConvertRequest payload = new ConvertRequest(url);
		setPayload(payload);
	}

}
