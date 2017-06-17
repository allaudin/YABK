package allaudin.github.io.yabkprocessor.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created on 6/17/17.
 *
 * @author M.Allaudin
 */

public class TestMode implements Parcelable{

    boolean b1;
    String name;
    byte bt;
    short shrt;
    int ins;
    long lng;
    char chr;
    float flt;
    double dbl;


    protected TestMode(Parcel in) {
        b1 = in.readByte() != 0;
        name = in.readString();
        bt = in.readByte();
        ins = in.readInt();
        lng = in.readLong();
        flt = in.readFloat();
        dbl = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (b1 ? 1 : 0));
        dest.writeString(name);
        dest.writeByte(bt);
        dest.writeInt(ins);
        dest.writeLong(lng);
        dest.writeFloat(flt);
        dest.writeDouble(dbl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TestMode> CREATOR = new Creator<TestMode>() {
        @Override
        public TestMode createFromParcel(Parcel in) {
            return new TestMode(in);
        }

        @Override
        public TestMode[] newArray(int size) {
            return new TestMode[size];
        }
    };
}
