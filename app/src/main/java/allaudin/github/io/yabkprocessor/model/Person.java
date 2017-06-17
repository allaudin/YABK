package allaudin.github.io.yabkprocessor.model;

import io.github.allaudin.yabk.Methods;
import io.github.allaudin.yabk.YabkProcess;

/**
 * Created on 6/17/17.
 *
 * @author M.Allaudin
 */

@YabkProcess(methods = Methods.ACCESSORS)
public abstract class Person {

    protected String father;

    protected boolean name;
    private byte id;

}
