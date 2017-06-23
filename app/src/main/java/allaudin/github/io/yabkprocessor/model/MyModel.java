package allaudin.github.io.yabkprocessor.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created on 6/23/17.
 *
 * @author M.Allaudin
 */

public class MyModel implements Parcelable {
    List<Object> objects;

    protected MyModel(Parcel in) {
    }

    public static final Creator<MyModel> CREATOR = new Creator<MyModel>() {
        @Override
        public MyModel createFromParcel(Parcel in) {
            return new MyModel(in);
        }

        @Override
        public MyModel[] newArray(int size) {
            return new MyModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
