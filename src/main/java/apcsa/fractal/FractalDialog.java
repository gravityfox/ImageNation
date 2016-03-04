package apcsa.fractal;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.NumberFormat;

/**
 * Created by Fox on 3/1/2016.
 * Project: ImageNation
 */
public class FractalDialog extends JDialog {

    private FractalPane fractalPane;
    private JPanel configPane;
    private JFormattedTextField realLowerField, realUpperField, imaginaryLowerField, imaginaryUpperField;
    //private double realLower, realUpper, imaginaryLower, imaginaryUpper;
    private JComboBox fractal, colorScheme;

    public FractalDialog(Frame owner, String title) {
        super(owner, title, true);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setMinimumSize(new Dimension(300, 200));
        this.setSize((int) (dim.width * 0.5), (int) (dim.height * 0.5));
        this.setLocation((dim.width - this.getWidth()) / 2, (dim.height - this.getHeight()) / 2);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        this.init();
        this.configureLayout();

        this.setVisible(true);
    }

    private void init() {
        this.getContentPane().setLayout(new BorderLayout());
        this.fractalPane = new FractalPane();
        this.getContentPane().add(fractalPane, BorderLayout.CENTER);
        this.configPane = new JPanel(new GridBagLayout());
        this.configPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.getContentPane().add(configPane, BorderLayout.SOUTH);
        this.fractalPane.rebuffer();
        realLowerField = new JFormattedTextField(NumberFormat.getNumberInstance());
        realUpperField = new JFormattedTextField(NumberFormat.getNumberInstance());
        imaginaryLowerField = new JFormattedTextField(NumberFormat.getNumberInstance());
        imaginaryUpperField = new JFormattedTextField(NumberFormat.getNumberInstance());

    }

    private void configureLayout() {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.25;
        c.weighty = 1;
        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(4, 4, 4, 4);
        c.ipadx = 4;
        c.ipady = 4;
        c.fill = GridBagConstraints.BOTH;
        configPane.add(realLowerField, c);
        c.gridx = 1;
        configPane.add(realUpperField, c);
        c.gridx = 2;
        configPane.add(imaginaryLowerField, c);
        c.gridx = 3;
        configPane.add(imaginaryUpperField, c);
    }

    private void addListeners() {

    }


}
