package org.dyndns.warenix.web2pdf;

import org.dyndns.warenix.expsdk.api.pdf.ConvertRequest.Orientation;
import org.dyndns.warenix.expsdk.api.pdf.ConvertRequest.PageSize;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Pass this object to
 * {@link Web2PDFIntentService#startService(android.content.Context, Web2PDFArgument)}
 * 
 * @author warenix
 * 
 */
public class Web2PDFArgument implements Parcelable {
  public String url;
  public PageSize size;
  public Orientation orientation;
  public String filename;

  public Web2PDFArgument() {

  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(url);
    dest.writeInt(size.ordinal());
    dest.writeInt(orientation.ordinal());
    dest.writeString(filename);
  }

  private Web2PDFArgument(Parcel in) {
    url = in.readString();
    size = PageSize.values()[in.readInt()];
    orientation = Orientation.values()[in.readInt()];
    filename = in.readString();
  }

  public static final Parcelable.Creator<Web2PDFArgument> CREATOR =
      new Parcelable.Creator<Web2PDFArgument>() {

        @Override
        public Web2PDFArgument createFromParcel(Parcel source) {
          return new Web2PDFArgument(source);
        }

        @Override
        public Web2PDFArgument[] newArray(int size) {
          return new Web2PDFArgument[size];
        }
      };

}
