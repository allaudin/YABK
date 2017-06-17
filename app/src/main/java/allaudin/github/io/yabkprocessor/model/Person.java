package allaudin.github.io.yabkprocessor.model;

import io.github.allaudin.yabk.YabkProcess;

/**
 * Created on 6/17/17.
 *
 * @author M.Allaudin
 */

@SuppressWarnings("WeakerAccess")
@YabkProcess(genClassName = "MyPerson")
public abstract class Person {
    protected boolean name;
    protected byte id;
    protected int myInt;
    protected float myFloat;
    protected long myLong;
    protected String myString;
    protected double myDouble;
}
