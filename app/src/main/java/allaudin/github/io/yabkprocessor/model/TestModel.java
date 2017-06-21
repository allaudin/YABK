package allaudin.github.io.yabkprocessor.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created on 6/21/17.
 *
 * @author M.Allaudin
 */

public class TestModel implements Parcelable {

    List mylist;
    String mystring;
    List<String> names;
    // why not
    List<Integer> namesInt;

    protected TestModel(Parcel in) {
        mystring = in.readString();
        names = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mystring);
        dest.writeStringList(names);
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

