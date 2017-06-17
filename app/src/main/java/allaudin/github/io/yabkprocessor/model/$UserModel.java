package allaudin.github.io.yabkprocessor.model;

import io.github.allaudin.yabk.YabkProcess;

/**
 * Created on 6/15/17.
 *
 * @author M.Allaudin
 */

@SuppressWarnings("WeakerAccess")
@YabkProcess(nonNullStrings = true)
public abstract class $UserModel {
    protected String name;
    protected int id;
    protected long card;
    protected String phone;
    protected boolean isMarried;
}
