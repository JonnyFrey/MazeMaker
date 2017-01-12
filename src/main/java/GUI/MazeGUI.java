package GUI;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import generator.Algorithm;
import generator.BinaryTree;
import generator.RecursiveBackTracking;
import generator.SideWinder;
import utils.SortedComboBoxModel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;


/**
 * Created by Jonny on 1/11/2016.
 */
public class MazeGUI implements ActionListener, ChangeListener {
    private static String current;
    private static HashMap<String, Algorithm> handler;
    private JPanel MainPanel;
    private JPanel MazePanel;
    private JButton Step;
    private JButton Reset;
    private JButton Run;
    private JButton Stop;
    private JComboBox ListOfAlgs;
    private JSlider Speed;
    private JPanel DisplayControl;
    private JPanel MazeControl;
    private JButton solve;
    private JCheckBox max;
    private JRadioButton Numbers;
    private JRadioButton None;
    private JButton XRay;
    private JTabbedPane tabbedPane1;
    private JButton quickGenButton;
    private JCheckBox boundMoveCheckBox;
    private JButton startColorButton;
    private JButton endColorButton;
    private JCheckBox rainbowCheckBox;

    public MazeGUI(String current) {
        $$$setupUI$$$();
        Run.addActionListener(this);
        Reset.addActionListener(this);
        Step.addActionListener(this);
        Stop.addActionListener(this);
        solve.addActionListener(this);
        Stop.setEnabled(false);
        Speed.addChangeListener(this);
        ListOfAlgs.addActionListener(this);
        handler.keySet().forEach(ListOfAlgs::addItem);
        ListOfAlgs.setSelectedItem(current);
        Numbers.addActionListener(this);
        None.addActionListener(this);
        Numbers.addActionListener(this);
        max.addActionListener(this);
        XRay.addActionListener(this);
        MazePanel.addKeyListener((MazeDisplay) MazePanel);
        MazePanel.setFocusable(true);
        MazePanel.setRequestFocusEnabled(true);
        quickGenButton.addActionListener(this);
        boundMoveCheckBox.addActionListener(this);
        startColorButton.addActionListener(this);
        endColorButton.addActionListener(this);
        rainbowCheckBox.addActionListener(this);
    }

    /*
 * IMPORTANT: IF WANT TO ADD NEW ALGORITHM, ADD DOWN HERE.
 */
    private static void initAlg() {
        handler = new HashMap();
        for (int i = 64; i >= 4; i /= 2) {
            addAlgorithm(new RecursiveBackTracking(i));
            addAlgorithm(new SideWinder(i));
            addAlgorithm(new BinaryTree(i));
        }
    }

    public static void main(String[] args) {
        initAlg();
        JFrame frame = new JFrame("MazeGUI");
        frame.setContentPane(new MazeGUI(current).MainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }


    private static void addAlgorithm(Algorithm algorithm) {
        handler.put(algorithm.toString(), algorithm);
        current = algorithm.toString();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        MazeDisplay stuff = ((MazeDisplay) MazePanel);
        switch (e.getActionCommand().toUpperCase()) {
            case "STEP":
                handler.get(current).step();
                break;
            case "RESET":
                handler.get(current).reset();
                stuff.solve(false, false, false, rainbowCheckBox.isSelected());
                enableExtraOptions(false);
                max.setSelected(false);
                stuff.setEndPoints(false);
                Run.setEnabled(true);
                Step.setEnabled(true);
                break;
            case "RUN":
                Run.setEnabled(false);
                Step.setEnabled(false);
                Reset.setEnabled(false);
                Stop.setEnabled(true);
                ListOfAlgs.setEnabled(false);
                handler.get(current).run();
                break;
            case "STOP":
                handler.get(current).stop();
                Run.setEnabled(true);
                Step.setEnabled(true);
                Reset.setEnabled(true);
                ListOfAlgs.setEnabled(true);
                Stop.setEnabled(false);
                break;
            case "COMBOBOXCHANGED":
                handler.get(current).stop();
                handler.get(current).reset();
                current = (String) ListOfAlgs.getSelectedItem();
                stuff.setCurrent(current).updateDrawHandler();
                stuff.solve(false, Numbers.isSelected(), false, rainbowCheckBox.isSelected());
                Run.setEnabled(true);
                Step.setEnabled(true);
                Reset.setEnabled(true);
                Stop.setEnabled(false);
                enableExtraOptions(false);
                max.setSelected(false);
                stuff.setEndPoints(false);
                break;
            case "SOLVE":
                stuff.solve(!stuff.isSolve(), Numbers.isSelected(), false, rainbowCheckBox.isSelected());
                break;
            case "NONE":
                stuff.setNumbers(false);
                break;
            case "NUMBERS":
                stuff.setNumbers(true);
                break;
            case "XRAY":
                stuff.solve(false, Numbers.isSelected(), true, rainbowCheckBox.isSelected());
                break;
            case "MAX":
                stuff.setEndPoints(max.isSelected());
                stuff.solve(false, false, false, rainbowCheckBox.isSelected());
                break;
            case "QUICK GEN":
                handler.get(current).stop();
                Run.setEnabled(true);
                Step.setEnabled(true);
                Reset.setEnabled(true);
                ListOfAlgs.setEnabled(true);
                Stop.setEnabled(false);
                handler.get(current).finish();
                break;
            case "BOUND MOVE":
                stuff.setBounded(boundMoveCheckBox.isSelected());
                break;
            case "START COLOR":
                Color startColor = JColorChooser.showDialog(new JFrame(), "Choose Start Color", Color.RED);
                stuff.setFromTo(startColor, null);
                break;
            case "END COLOR":
                Color endColor = JColorChooser.showDialog(new JFrame(), "Choose End Color", Color.BLUE);
                stuff.setFromTo(null, endColor);
                break;
            case "RAINBOW":
                stuff.setRainbow(rainbowCheckBox.isSelected());
                break;
        }
        if (handler.get(current).isComplete()) {
            Run.setEnabled(false);
            Step.setEnabled(false);
            Stop.setEnabled(false);
            Reset.setEnabled(true);
            enableExtraOptions(true);
        }
        MazePanel.requestFocusInWindow();
    }

    private void enableExtraOptions(boolean enable) {
        solve.setEnabled(enable);
        max.setEnabled(enable);
        XRay.setEnabled(enable);
        rainbowCheckBox.setEnabled(enable);
        None.setEnabled(enable);
        Numbers.setEnabled(enable);
        startColorButton.setEnabled(enable);
        endColorButton.setEnabled(enable);
    }

    private void createUIComponents() {
        MazePanel = new MazeDisplay(handler, current);
        SortedComboBoxModel<String> model = new SortedComboBoxModel();
        ListOfAlgs = new JComboBox(model);
        DisplayControl = new JPanel();
        MazeControl = new JPanel();
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        ((MazeDisplay) MazePanel).setSpeed(Speed.getValue());
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        MainPanel = new JPanel();
        MainPanel.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        MazePanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        MazePanel.setBackground(new Color(-1));
        MainPanel.add(MazePanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        MazePanel.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(640, 640), null, 1, false));
        MazeControl.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        MainPanel.add(MazeControl, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        Step = new JButton();
        Step.setForeground(new Color(-4601579));
        Step.setText("Step");
        MazeControl.add(Step, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        Run = new JButton();
        Run.setForeground(new Color(-16725729));
        Run.setText("Run");
        MazeControl.add(Run, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        Stop = new JButton();
        Stop.setForeground(new Color(-3595515));
        Stop.setText("Stop");
        MazeControl.add(Stop, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        Reset = new JButton();
        Reset.setForeground(new Color(-13857335));
        Reset.setText("Reset");
        MazeControl.add(Reset, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        tabbedPane1 = new JTabbedPane();
        MainPanel.add(tabbedPane1, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Maze Control", panel1);
        Speed = new JSlider();
        Speed.setMajorTickSpacing(1);
        Speed.setMaximum(60);
        Speed.setMinimum(1);
        Speed.setMinorTickSpacing(1);
        Speed.setPaintLabels(false);
        Speed.setPaintTicks(false);
        Speed.setPaintTrack(true);
        Speed.setSnapToTicks(true);
        Speed.setValue(5);
        Speed.setValueIsAdjusting(false);
        Speed.putClientProperty("Slider.paintThumbArrowShape", Boolean.FALSE);
        Speed.putClientProperty("JSlider.isFilled", Boolean.TRUE);
        panel1.add(Speed, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        ListOfAlgs.setMaximumRowCount(5);
        panel1.add(ListOfAlgs, new GridConstraints(0, 2, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        quickGenButton = new JButton();
        quickGenButton.setText("Quick Gen");
        panel1.add(quickGenButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        boundMoveCheckBox = new JCheckBox();
        boundMoveCheckBox.setEnabled(true);
        boundMoveCheckBox.setSelected(true);
        boundMoveCheckBox.setText("Bound Move");
        panel1.add(boundMoveCheckBox, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(3, 3, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Extra", panel2);
        XRay = new JButton();
        XRay.setEnabled(false);
        XRay.setText("Xray");
        panel2.add(XRay, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        DisplayControl.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(DisplayControl, new GridConstraints(0, 2, 3, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        max = new JCheckBox();
        max.setEnabled(true);
        max.setText("Max");
        DisplayControl.add(max, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        None = new JRadioButton();
        None.setSelected(true);
        None.setText("None");
        DisplayControl.add(None, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        Numbers = new JRadioButton();
        Numbers.setSelected(false);
        Numbers.setText("Numbers");
        DisplayControl.add(Numbers, new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        rainbowCheckBox = new JCheckBox();
        rainbowCheckBox.setEnabled(true);
        rainbowCheckBox.setSelected(true);
        rainbowCheckBox.setText("Rainbow");
        DisplayControl.add(rainbowCheckBox, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        solve = new JButton();
        solve.setEnabled(false);
        solve.setText("solve");
        panel2.add(solve, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        startColorButton = new JButton();
        startColorButton.setText("Start Color");
        panel2.add(startColorButton, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        endColorButton = new JButton();
        endColorButton.setText("End Color");
        panel2.add(endColorButton, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ButtonGroup buttonGroup;
        buttonGroup = new ButtonGroup();
        buttonGroup.add(Numbers);
        buttonGroup.add(None);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return MainPanel;
    }
}
