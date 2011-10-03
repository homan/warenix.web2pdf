package org.dyndns.warenix.web2pdf;

import android.net.Uri;

public class PDFmyURL {

	private String url = "";
	private String orientation = "";
	private String pageSize = "";

	public PDFmyURL() {
		orientation = Option.ORIENTATION_LANDSCAPE;
	}

	public String toString() {
		String urlString = "http://pdfmyurl.com?";

		String parameterString = "";
		parameterString += String.format("&%s=%s",
				Option.PARAMETER_ORIENTATION, orientation);
		parameterString += String.format("&%s=%s", Option.PARAMETER_PAGE_SIZE,
				pageSize);

		parameterString += String.format("&%s=%s", Option.PARAMETER_URL, url);
		parameterString += "&--redirect-delay=200&--quiet";

		urlString = String
				.format("%s%s", urlString, Uri.parse(parameterString));
		return urlString;
	}

	public void setURL(String url) {
		this.url = url;
	}

	public void setOrientation(String o) {
		orientation = o;
	}

	public void setPageSize(String s) {
		pageSize = s;
	}

	public class Option {
		public static final String PARAMETER_URL = "url";

		public static final String PARAMETER_ORIENTATION = "--orientation";
		public static final String ORIENTATION_LANDSCAPE = "Landscape";
		public static final String ORIENTATION_PORTRAIT = "Portrait";

		public static final String PARAMETER_PAGE_SIZE = "--page-size";
		public static final String PAGE_SIZE_A4 = "A4";
		public static final String PAGE_SIZE_LETTER = "Letter";
	}
}
