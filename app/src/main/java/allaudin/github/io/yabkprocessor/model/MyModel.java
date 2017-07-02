package allaudin.github.io.yabkprocessor.model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created on 6/23/17.
 *
 * @author M.Allaudin
 */

public class MyModel implements Parcelable{


    Bundle bundle;


    MyModel[] model;

    protected MyModel(Parcel in) {
        bundle = in.readBundle(bundle.getClass().getClassLoader());
        model = in.createTypedArray(MyModel.CREATOR);
    }

    //

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBundle(bundle);
        dest.writeTypedArray(model, flags);
    }

    @Override
    public int describeContents() {
        return 0;
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
}
