package cn.baililuohui.shortcut_transform;

import com.intellij.openapi.actionSystem.KeyboardShortcut;
import com.intellij.openapi.actionSystem.Shortcut;
import com.intellij.openapi.keymap.Keymap;
import com.intellij.openapi.keymap.KeymapManager;
import com.intellij.openapi.keymap.impl.DefaultBundledKeymaps;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.wm.IdeFocusManager;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBList;
import com.intellij.util.ui.JBUI;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class MainPanel extends MyShortcutPanel<KeyboardShortcut> {
    private DefaultBundledKeymaps dfBundle;
    private List<String> keymapFileNames = new ArrayList<>();
    private ComboBox<String> sourceCheckbox;
    private ComboBox<String> destCheckbox;
    private JLabel sLabel;
    private JLabel dLabel;
    private JBList<String> result;
    private JButton action;
    private ShortcutInput input;

    MainPanel(LayoutManager layout) {
        super(layout);
        dfBundle = new DefaultBundledKeymaps();
        initKmFiles();
        setBorder(JBUI.Borders.empty(20));
        setLayout(layout);
        initSourceCb();
        initDestCb();
        initLabels();
        initShortcutInput();
    }

    /**
     * 获取保存快捷键的文件名 <没有后缀>
     */
    private void initKmFiles() {
        List<String> keymapFileNames = dfBundle.getKeymapFileNames();
        for (String keymapFileName : keymapFileNames) {
            String fName = dfBundle.getKeyFromFileName(keymapFileName);
            this.keymapFileNames.add(fName);
        }
    }

    /**
     * 初始化源下拉列表
     */
    private void initSourceCb() {
        sourceCheckbox = new ComboBox<>();
        sourceCheckbox.setSize(80, 30);
    }

    /**
     * 初始化目标下拉列表
     */
    private void initDestCb() {
        destCheckbox = new ComboBox<>();
        destCheckbox.setSize(80, 30);
    }

    private void initLabels() {
        sLabel = new JLabel("源平台");
        dLabel = new JLabel("目标平台");
        result = new JBList<String>();
        action = new JButton("搜索");
        sLabel.setSize(80, 20);
        dLabel.setSize(80, 20);
        result.setSize(80, 80);
        action.setSize(80, 20);
        sLabel.setHorizontalAlignment(SwingConstants.CENTER);
        sLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        dLabel.setHorizontalAlignment(SwingConstants.CENTER);
        dLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        action.setHorizontalAlignment(SwingConstants.CENTER);
        action.setHorizontalTextPosition(SwingConstants.CENTER);
        String[] preTell = {"根据你所熟悉的(源)平台的快捷键查找你不熟悉的(目标)平台的快捷键"};
        result.setListData(preTell);
        action.addActionListener(actionEvent -> {
            KeyboardShortcut shortcut = getShortcut();
            preTell[0] = "";
            if (shortcut == null) {
                preTell[0] = "请键入快捷键";
                result.setListData(preTell);
                return;
            }
            println("输入：" + shortcut.toString());
            String sourceName = sourceCheckbox.getSelectedItem().toString();
            Keymap sourceMap = KeymapManager.getInstance().getKeymap(sourceName);
            if (sourceMap == null) {
                preTell[0] = "没有找到'" + sourceName + "'快捷键文件";
                result.setListData(preTell);
                return;
            }
            String id = null;
            for (String s : sourceMap.getActionIds()) {
                for (Shortcut c : sourceMap.getShortcuts(s)) {
                    println("源：" + c.toString());
                    if (isShortcutSame(shortcut, c)) {
                        id = s;
                    }
                }
            }
            if (id == null) {
                preTell[0] = sourceName + "[源] 中没有找到'" + shortcut + "'快捷键";
                result.setListData(preTell);
                return;
            }

            String destName = destCheckbox.getSelectedItem().toString();
            Keymap destMap = KeymapManager.getInstance().getKeymap(destName);
            if (destMap == null) {
                preTell[0] = "没有找到'" + destName + "'快捷键文件";
                result.setListData(preTell);
                return;
            }
            Shortcut[] shortcuts = destMap.getShortcuts(id);
            if (shortcuts.length < 1) {
                preTell[0] = destName + "[靶] 中没有找到'" + shortcut + "'快捷键";
                result.setListData(preTell);
                return;
            }
            String[] items = new String[shortcuts.length + 1];
            for (int i = 0; i < shortcuts.length; i++) {
                items[i + 1] = getShortcutString(shortcuts[i]);
            }
            items[0] = destName + " 中找到的["+ id +"]快捷键：";
            result.setListData(items);
        });
    }

    /**
     * 设置数据并添加控件
     */
    public void setDataAndView() {
        GridBagLayout layout = (GridBagLayout) getLayout();
        GridBagConstraints cons = new GridBagConstraints();

        JPanel innerPanel = new JPanel();
        innerPanel.setLayout(new GridLayout(3, 2, 20, 20));
        innerPanel.add(sLabel);
        innerPanel.add(dLabel);
        innerPanel.add(sourceCheckbox);
        innerPanel.add(destCheckbox);
        innerPanel.add(input);
        innerPanel.add(action);
        innerPanel.setBorder(JBUI.Borders.emptyBottom(20));

        cons.gridwidth = 1;
        layout.setConstraints(innerPanel, cons);
        add(innerPanel);

        cons.gridwidth = GridBagConstraints.REMAINDER;
        cons.fill = GridBagConstraints.BOTH;
        JPanel container1 = new JPanel();
        layout.setConstraints(container1, cons);
        add(container1);

        cons.gridwidth = GridBagConstraints.REMAINDER;
        cons.fill = GridBagConstraints.BOTH;
        cons.weightx = 1;
        cons.weighty = 1;
        layout.setConstraints(result, cons);
        add(result);

        cons.gridwidth = 1;
        setSelections(keymapFileNames);
    }

    private void setSelections(List<String> selecttions) {
        for (String s : selecttions) {
            sourceCheckbox.addItem(s);
            destCheckbox.addItem(s);
        }
        sourceCheckbox.setSelectedIndex(0);
        destCheckbox.setSelectedIndex(0);
    }

    private void initShortcutInput() {
        input = new ShortcutInput(true);
        input.setSize(80, 20);
        input.setBackground(JBColor.background());
        addPropertyChangeListener("shortcut", myPropertyListener);
        input.addPropertyChangeListener("keyStroke", myPropertyListener);
    }

    private final PropertyChangeListener myPropertyListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent event) {
            if (MainPanel.this != event.getSource()) {
                setShortcut(newShortcut());
                if (null == input.getKeyStroke()) {
                    IdeFocusManager.getGlobalInstance().doWhenFocusSettlesDown(() -> IdeFocusManager.getGlobalInstance().requestFocus(input, true));
                }
            } else if (event.getNewValue() instanceof KeyboardShortcut) {
                KeyboardShortcut shortcut = (KeyboardShortcut) event.getNewValue();
                input.setKeyStroke(shortcut.getFirstKeyStroke());
            } else {
                input.setKeyStroke(null);
            }
        }
    };

    private KeyboardShortcut newShortcut() {
        KeyStroke key = input.getKeyStroke();
        return key == null ? null : new KeyboardShortcut(key, null);
    }

    private static void println(String s) {
        System.out.println(s);
    }

    /**
     * 判断快捷键是否相等
     * 防止[ctrl pressed D] 和 [ctrl released D] 判断不相等的情况
     *
     * @param src
     * @param dest
     * @return
     */
    private static boolean isShortcutSame(Shortcut src, Shortcut dest) {
        String s1 = src.toString();
        String s2 = dest.toString();
        String[] a1 = s1.split(" ");
        String[] a2 = s2.split(" ");
        if (a1.length != a2.length || a1.length < 1) {
            return false;
        }
        for (int i = 0; i < a1.length; i++) {
            if (i == a1.length - 2) continue;
            if (!a1[i].equals(a2[i])) {
                return false;
            }
        }
        return true;
    }

    private static String getShortcutString(Shortcut src) {
        String[] s = src.toString().split(" ");
        if (s.length == 1) return s[0];
        if (s.length < 1) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length; i++) {
            if (i == s.length - 2 && s.length != 2) continue;
            sb.append(s[i]);
            if (i != s.length - 1) {
                sb.append("+");
            }
        }
        return sb.toString();
    }
}
