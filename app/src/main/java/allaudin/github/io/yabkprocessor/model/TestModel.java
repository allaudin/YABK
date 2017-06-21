package allaudin.github.io.yabkprocessor.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created on 6/21/17.
 *
 * @author M.Allaudin
 */

public class TestModel implements Parcelable {

    String test;

    protected TestModel(Parcel in) {
        test = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(test);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TestModel> CREATOR = new Creator<TestModel>() {
        @Override
        public TestModel createFromParcel(Parcel in) {
            return new TestModel(in);
        }

        @Override
        public TestModel[] newArray(int size) {
            return new TestModel[size];
        }
    };
}
