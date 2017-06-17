package allaudin.github.io.yabkprocessor.model;

import io.github.allaudin.yabk.YabkProcess;

/**
 * Created on 6/17/17.
 *
 * @author M.Allaudin
 */

@SuppressWarnings("WeakerAccess")
@YabkProcess(nonNullStrings = true)
public abstract class Person {

    protected String father;

    protected boolean name;
    private byte id;

}
