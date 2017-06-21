package allaudin.github.io.yabkprocessor.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created on 6/22/17.
 *
 * @author M.Allaudin
 */

public class A implements Parcelable {

    int n;

    protected A(Parcel in) {
        n = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(n);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<A> CREATOR = new Creator<A>() {
        @Override
        public A createFromParcel(Parcel in) {
            return new A(in);
        }

        @Override
        public A[] newArray(int size) {
            return new A[size];
        }
    };
}
