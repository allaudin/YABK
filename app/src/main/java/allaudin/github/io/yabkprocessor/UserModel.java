package allaudin.github.io.yabkprocessor;

import io.github.allaudin.yabk.YabkProcess;

/**
 * Created on 6/15/17.
 *
 * @author M.Allaudin
 */

@YabkProcess
public abstract class UserModel extends Model {
    protected String name;
    protected int id;
    private Model model;
}
