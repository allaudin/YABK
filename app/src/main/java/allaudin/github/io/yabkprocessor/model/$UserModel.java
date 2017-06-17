package allaudin.github.io.yabkprocessor.model;

import io.github.allaudin.yabk.YabkProcess;
import io.github.allaudin.yabk.YabkSkip;

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
    @YabkSkip
    protected long card;
    protected String phone;
    protected boolean isMarried;
}
