package apcsa.types;

import javax.swing.*;

/**
 * Created by Fox on 3/18/2016.
 * Project: ImageNation
 */
public interface IParameterComponent {

    JComponent getComponent();

    Object getValue();

    boolean isValuePresent();
}
