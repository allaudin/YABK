package allaudin.github.io.yabkprocessor.model;

import io.github.allaudin.yabk.YabkProcess;

/**
 * Created on 6/15/17.
 *
 * @author M.Allaudin
 */

@YabkProcess(nonNullStrings = true)
abstract class $UserModel {
    String name;
    int id;
    byte mybyte;
    String phone;
    boolean isMarried;
}
