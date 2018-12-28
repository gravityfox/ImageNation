package apcsa.types;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Fox on 3/18/2016.
 * Project: ImageNation
 */
public class ComponentFactory {

    private static final Border PAR_BORDER = BorderFactory.createEmptyBorder(4, 4, 4, 4);

    public static IParameterComponent createComponent(Parameter parameter, String name) {
        if (name == null || name.isEmpty()) name = parameter.getName();

        if (!TypeCatalog.isValidParameter(parameter.getType()))
            throw new IllegalArgumentException("Parameter \"" + parameter.getName() + "\" of type \"" + parameter.getType() + "\" is not supported!");
        if (parameter.getType() == Byte.class || parameter.getType() == Byte.TYPE) {
            return createByte(name);
        } else if (parameter.getType() == Short.class || parameter.getType() == Short.TYPE) {
            return createShort(name);
        } else if (parameter.getType() == Integer.class || parameter.getType() == Integer.TYPE) {
            return createInt(name);
        } else if (parameter.getType() == Long.class || parameter.getType() == Long.TYPE) {
            return createLong(name);
        } else if (parameter.getType() == Float.class || parameter.getType() == Float.TYPE) {
            return createFloat(name);
        } else if (parameter.getType() == Double.class || parameter.getType() == Double.TYPE) {
            return createDouble(name);
        } else if (parameter.getType() == String.class) {
            return createString(name);
        }
        return null;
    }

    public static List<IParameterComponent> createComponents(Method method) {
        List<IParameterComponent> list = new ArrayList<>();
        List<String> names = Signatures.getParameterNames(method);

        Iterator<String> namesIt = names.iterator();
        for (Parameter p : method.getParameters()) {
            list.add(createComponent(p, (namesIt.hasNext() ? namesIt.next() : p.getName())));
        }
        return list;
    }

    private static IParameterComponent createByte(String name) {
        return new IParameterComponent() {

            JPanel pane;
            JFormattedTextField number;

            {
                pane = new JPanel();
                pane.setLayout(new BoxLayout(pane, BoxLayout.X_AXIS));
                pane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
                pane.setBorder(PAR_BORDER);
                pane.add(new JLabel("Byte - " + name + ":"));
                number = new JFormattedTextField(NumberFormat.getIntegerInstance());
                number.setMinimumSize(new Dimension(60, 20));
                pane.add(Box.createRigidArea(new Dimension(12, 0)));
                pane.add(number);
            }

            @Override
            public JComponent getComponent() {
                return pane;
            }

            @Override
            public Object getValue() {
                long value = (Long) number.getValue();
                if (value > Byte.MAX_VALUE) value = Byte.MAX_VALUE;
                if (value < Byte.MIN_VALUE) value = Byte.MIN_VALUE;
                return (byte) value;
            }

            @Override
            public boolean isValuePresent() {
                return number.getValue() != null;
            }

        };
    }

    private static IParameterComponent createShort(String name) {
        return new IParameterComponent() {

            JPanel pane;
            JFormattedTextField number;

            {
                pane = new JPanel();
                pane.setLayout(new BoxLayout(pane, BoxLayout.X_AXIS));
                pane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
                pane.setBorder(PAR_BORDER);
                pane.add(new JLabel("Short - " + name + ":"));
                number = new JFormattedTextField(NumberFormat.getIntegerInstance());
                number.setMinimumSize(new Dimension(60, 20));
                pane.add(Box.createRigidArea(new Dimension(12, 0)));
                pane.add(number);
            }

            @Override
            public JComponent getComponent() {
                return pane;
            }

            @Override
            public Object getValue() {
                long value = (Long) number.getValue();
                if (value > Short.MAX_VALUE) value = Short.MAX_VALUE;
                if (value < Short.MIN_VALUE) value = Short.MIN_VALUE;
                return (short) value;
            }

            @Override
            public boolean isValuePresent() {
                return number.getValue() != null;
            }
        };
    }

    private static IParameterComponent createInt(String name) {
        return new IParameterComponent() {

            JPanel pane;
            JFormattedTextField number;

            {
                pane = new JPanel();
                pane.setLayout(new BoxLayout(pane, BoxLayout.X_AXIS));
                pane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
                pane.setBorder(PAR_BORDER);
                pane.add(new JLabel("Integer - " + name + ":"));
                number = new JFormattedTextField(NumberFormat.getIntegerInstance());
                number.setMinimumSize(new Dimension(60, 20));
                pane.add(Box.createRigidArea(new Dimension(12, 0)));
                pane.add(number);
            }

            @Override
            public JComponent getComponent() {
                return pane;
            }

            @Override
            public Object getValue() {
                long value = (Long) number.getValue();
                if (value > Integer.MAX_VALUE) value = Integer.MAX_VALUE;
                if (value < Integer.MIN_VALUE) value = Integer.MIN_VALUE;
                return (int) value;
            }

            @Override
            public boolean isValuePresent() {
                return number.getValue() != null;
            }
        };
    }

    private static IParameterComponent createLong(String name) {
        return new IParameterComponent() {

            JPanel pane;
            JFormattedTextField number;

            {
                pane = new JPanel();
                pane.setLayout(new BoxLayout(pane, BoxLayout.X_AXIS));
                pane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
                pane.setBorder(PAR_BORDER);
                pane.add(new JLabel("Long - " + name + ":"));
                number = new JFormattedTextField(NumberFormat.getIntegerInstance());
                number.setMinimumSize(new Dimension(60, 20));
                pane.add(Box.createRigidArea(new Dimension(12, 0)));
                pane.add(number);
            }

            @Override
            public JComponent getComponent() {
                return pane;
            }

            @Override
            public Object getValue() {
                return number.getValue();
            }

            @Override
            public boolean isValuePresent() {
                return number.getValue() != null;
            }
        };
    }

    private static IParameterComponent createFloat(String name) {
        return new IParameterComponent() {

            JPanel pane;
            JFormattedTextField number;

            {
                pane = new JPanel();
                pane.setLayout(new BoxLayout(pane, BoxLayout.X_AXIS));
                pane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
                pane.setBorder(PAR_BORDER);
                pane.add(new JLabel("Float - " + name + ":"));
                number = new JFormattedTextField(NumberFormat.getNumberInstance());
                number.setMinimumSize(new Dimension(60, 20));
                pane.add(Box.createRigidArea(new Dimension(12, 0)));
                pane.add(number);
            }

            @Override
            public JComponent getComponent() {
                return pane;
            }

            @Override
            public Object getValue() {
                return number.getValue();
            }

            @Override
            public boolean isValuePresent() {
                return number.getValue() != null;
            }
        };
    }

    private static IParameterComponent createDouble(String name) {
        return new IParameterComponent() {

            JPanel pane;
            JFormattedTextField number;

            {
                pane = new JPanel();
                pane.setLayout(new BoxLayout(pane, BoxLayout.X_AXIS));
                pane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
                pane.setBorder(PAR_BORDER);
                pane.add(new JLabel("Double - " + name + ":"));
                number = new JFormattedTextField(NumberFormat.getNumberInstance());
                number.setMinimumSize(new Dimension(60, 20));
                pane.add(Box.createRigidArea(new Dimension(12, 0)));
                pane.add(number);
            }

            @Override
            public JComponent getComponent() {
                return pane;
            }

            @Override
            public Object getValue() {
                return number.getValue();
            }

            @Override
            public boolean isValuePresent() {
                return number.getValue() != null;
            }
        };
    }

    private static IParameterComponent createString(String name) {
        return new IParameterComponent() {

            JPanel pane;
            JTextField string;

            {
                pane = new JPanel();
                pane.setLayout(new BoxLayout(pane, BoxLayout.X_AXIS));
                pane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
                pane.setBorder(PAR_BORDER);
                pane.add(new JLabel("String - " + name + ":"));
                string = new JTextField();
                string.setMinimumSize(new Dimension(60, 20));
                pane.add(Box.createRigidArea(new Dimension(12, 0)));
                pane.add(string);
            }

            @Override
            public JComponent getComponent() {
                return pane;
            }

            @Override
            public Object getValue() {
                return string.getText();
            }

            @Override
            public boolean isValuePresent() {
                return !(string.getText() == null || string.getText().isEmpty());
            }
        };
    }
}
