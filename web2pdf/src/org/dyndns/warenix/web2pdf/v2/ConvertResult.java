package org.dyndns.warenix.web2pdf.v2;

public class ConvertResult {
	public Result result;

	public static class Result {
		/**
		 * requested url
		 */
		public String url;
		/**
		 * url to the converted pdf
		 */
		public String pdf_url;
	}
}
