package allaudin.github.io.yabkprocessor.model;

import java.util.List;

import io.github.allaudin.yabk.YabkGenerated;
import io.github.allaudin.yabk.YabkProcess;

/**
 * Created on 6/18/17.
 *
 * @author M.Allaudin
 */

@YabkProcess
abstract class $Person {
    String name;
    boolean isMarried;
    byte myBtye;
    List<String> stringList;
    @YabkGenerated
    User user;

}
