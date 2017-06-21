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

    String name;
    UserModel user;
    List<String> list;
    List<Person> personList;
    List<List<Person>> otherList;

    protected TestModel(Parcel in) {
        name = in.readString();
        user = in.readParcelable(UserModel.class.getClassLoader());
        list = in.createStringArrayList();
        personList = in.createTypedArrayList(Person.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeParcelable(user, flags);
        dest.writeStringList(list);
        dest.writeTypedList(personList);
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
