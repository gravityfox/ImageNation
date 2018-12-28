package apcsa;

import apcsa.gui.TightCardLayout;
import apcsa.types.ComponentFactory;
import apcsa.types.IParameterComponent;
import apcsa.types.Signatures;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.*;
import java.util.function.Supplier;

/**
 * Created by Fox on 3/18/2016.
 * Project: ImageNation
 */
public class MethodCollection extends AbstractListModel<String> {

    private Map<String, List<Method>> methods = new CacheMap<>((k, m) -> {
        if (k instanceof String) {
            ArrayList<Method> list = new ArrayList<>();
            m.put((String) k, list);
            return list;
        } else return null;
    });

    public boolean addMethod(Method method) {
        List<Method> list = methods.get(method.getName());
        if (!list.contains(method)) {
            list.add(method);
            list.sort(METHOD_PARAMETER_COMPARATOR);
            fireContentsChanged(this, 0, methods.size() - 2);
            fireIntervalAdded(this, methods.size() - 1, methods.size() - 1);
            return true;
        } else {
            return false;
        }
    }

    public Object invokeMethod(IImage image, String name, Window parent) throws IllegalAccessException, InvocationTargetException {
        List<Method> methodList = methods.get(name);
        if (methodList.size() == 0) throw new IllegalArgumentException("No method with that name!");
        if (methodList.size() == 1 && methodList.get(0).getParameterCount() == 0) {
            return methodList.get(0).invoke(image);
        }
        JDialog dialog = new JDialog(parent, "Run Method...", Dialog.ModalityType.APPLICATION_MODAL);
        boolean[] confirmed = {false};
        Container container = dialog.getContentPane();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        JPanel cardPanel = new JPanel(new TightCardLayout());
        cardPanel.setBorder(BorderFactory.createEmptyBorder(8, 24, 8, 24));
        Supplier<Method> method;
        Object[] parameters;
        Map<Method, List<IParameterComponent>> paramComponentMap = new HashMap<>();

        List<String> names = new ArrayList<>();

        for (Method m : methodList) {
            List<IParameterComponent> componentList = ComponentFactory.createComponents(m);
            paramComponentMap.put(m, componentList);
            JPanel p = new JPanel();
            p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
            for (IParameterComponent pc : componentList) {
                p.add(pc.getComponent());
            }
            String signature = Signatures.getSignature(m);
            names.add(signature);
            cardPanel.add(p, signature);
        }


        if (methodList.size() != 1) {
            JComboBox<String> comboBox = new JComboBox<>(names.toArray(new String[0]));
            comboBox.addItemListener(e -> {
                CardLayout cl = (CardLayout) (cardPanel.getLayout());
                cl.show(cardPanel, (String) e.getItem());
                dialog.pack();
            });
            container.add(comboBox);
            method = () -> methodList.get(comboBox.getSelectedIndex());
        } else {
            method = () -> methodList.get(0);
        }

        container.add(cardPanel);
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(e -> dialog.setVisible(false));
        JButton ok = new JButton("Ok");
        ok.addActionListener(e -> {
            List<IParameterComponent> temp = paramComponentMap.get(method.get());
            for (IParameterComponent pc : temp) {
                if (!pc.isValuePresent()) {
                    JOptionPane.showMessageDialog(dialog, "You must not have any empty fields!", "Error!", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            confirmed[0] = true;
            dialog.setVisible(false);
        });
        JPanel buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
        buttons.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        buttons.add(Box.createHorizontalGlue());
        buttons.add(cancel);
        buttons.add(Box.createRigidArea(new Dimension(8, 0)));
        buttons.add(ok);
        buttons.add(Box.createRigidArea(new Dimension(8, 0)));
        container.add(buttons);
        dialog.setMinimumSize(new Dimension(300, 100));
        dialog.pack();
        dialog.setResizable(false);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        dialog.setLocation((dim.width - dialog.getWidth()) / 2, (dim.height - dialog.getHeight()) / 2);
        dialog.setVisible(true);
        if (!confirmed[0]) return null;
        List<IParameterComponent> componentList = paramComponentMap.get(method.get());
        parameters = new Object[componentList.size()];
        for (int i = 0; i < componentList.size(); i++) {
            parameters[i] = componentList.get(i).getValue();

        }
        dialog.dispose();
        return method.get().invoke(image, parameters);
    }

    public List<String> getSimpleList() {
        List<String> list = new ArrayList<>();
        for (Map.Entry<String, List<Method>> entry : methods.entrySet()) {
            if (entry.getValue().size() == 1) {
                list.add(entry.getKey());
            } else if (entry.getValue().size() > 1) {
                list.add(entry.getKey() + "...");
            }
        }
        Collections.sort(list);
        return list;
    }

    public List<List<Method>> getList() {
        return null;
    }

    @Override
    public int getSize() {
        return getSimpleList().size();
    }

    @Override
    public String getElementAt(int index) {
        return getSimpleList().get(index);
    }

    private static class MethodComparator implements Comparator<Method> {

        @Override
        public int compare(Method m1, Method m2) {
            if (m1 == m2) return 0;
            if (m1.getParameterCount() != m2.getParameterCount())
                return m1.getParameterCount() - m2.getParameterCount();
            return 0;
        }
    }

    public static final Comparator<Method> METHOD_PARAMETER_COMPARATOR = new Comparator<Method>() {
        @Override
        public int compare(Method o1, Method o2) {
            if (o1 == o2) return 0;

            int ret;
            int count = o1.getParameterCount();

            ret = count - o2.getParameterCount();
            if (ret != 0) return ret;

            Class<?>[] types1 = o1.getParameterTypes();
            Class<?>[] types2 = o2.getParameterTypes();

            for (int i = 0; i < count; i++) {
                Class<?> type1 = types1[i];
                Class<?> type2 = types2[i];

                if (type1.isPrimitive()) ret--;
                if (type2.isPrimitive()) ret++;
                if (ret != 0) return ret;

                ret = types1[i].getName().compareTo(types2[i].getName());
                if (ret != 0) return ret;
            }

            return ret;
        }
    };
}
