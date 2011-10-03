package org.dyndns.warenix.web2pdf;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public class DownloadFileTool {
	public static void downloadFile(URL url, String saveInDir,
			String saveAsFilename) {

		// String filepath = Environment.getExternalStorageDirectory()
		// .getAbsolutePath();
		// Log.e("FilePath", filepath);
		String sdDrive = "e:";

		// Create one directory
		String fullLocalDirPath = String.format("%s/%s", sdDrive, saveInDir);
		boolean success = (new File(fullLocalDirPath)).mkdir();
		if (success) {
		}

		InputStream in = null;
		BufferedOutputStream out = null;

		try {

			// String filepath = Environment.getExternalStorageDirectory()
			// .getAbsolutePath();
			// Log.e("FilePath", filepath);
			String filepath = "e:";
			String full_local_file_path = String.format("%s/%s/%s", filepath,
					saveInDir, saveAsFilename);

			FileOutputStream fos = new FileOutputStream(full_local_file_path);
			BufferedOutputStream bfs = new BufferedOutputStream(fos);

			in = new BufferedInputStream(url.openStream(), IO_BUFFER_SIZE);

			final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
			out = new BufferedOutputStream(dataStream, IO_BUFFER_SIZE);
			copy(in, out);
			out.flush();
			final byte[] data = dataStream.toByteArray();

			bfs.write(data, 0, data.length);
			bfs.flush();

		} catch (IOException e) {
		} finally {
			closeStream(in);
			closeStream(out);
		}
	}

	// helper method
	static int IO_BUFFER_SIZE = 4096;

	private static void copy(InputStream in, OutputStream out)
			throws IOException {
		byte[] b = new byte[IO_BUFFER_SIZE];
		int read;
		while ((read = in.read(b)) != -1) {
			out.write(b, 0, read);
		}
	}

	private static void closeStream(Closeable stream) {
		if (stream != null) {
			try {
				stream.close();
			} catch (IOException e) {
				// android.util.Log.e(LOG_TAG, "Could not close stream", e);
			}
		}
	}

}
