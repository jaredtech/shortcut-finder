package cn.baililuohui.shortcut_transform;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.KeyboardShortcut;
import com.intellij.openapi.actionSystem.Shortcut;
import com.intellij.openapi.keymap.KeyMapBundle;
import com.intellij.openapi.keymap.Keymap;
import com.intellij.openapi.keymap.KeymapManager;
import com.intellij.openapi.keymap.KeymapUtil;
import com.intellij.openapi.keymap.impl.BundledKeymapProvider;
import com.intellij.openapi.keymap.impl.DefaultBundledKeymaps;
import com.intellij.openapi.keymap.impl.KeymapManagerImpl;
import com.intellij.openapi.keymap.impl.ui.KeymapPanel;
import com.intellij.openapi.keymap.impl.ui.ShortcutTextField;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.JBColor;
import com.intellij.util.ui.JBUI;
import org.apache.http.util.TextUtils;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.function.Function;

public class ShowPlatformAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent event) {
        JFrame.setDefaultLookAndFeelDecorated(true);
//        final DefaultBundledKeymaps defaultBundledKeymaps = new DefaultBundledKeymaps();
//        List<String> keymapFileNames = defaultBundledKeymaps.getKeymapFileNames();
//
//        ComboBox<String> sourceCheckbox = new ComboBox<>();
//        sourceCheckbox.setSize(80, 30);
//        for (String i : keymapFileNames) {
//            sourceCheckbox.addItem(defaultBundledKeymaps.getKeyFromFileName(i));
//        }
//
//        ComboBox<String> destCheckbox = new ComboBox<>();
//        destCheckbox.setSize(80, 30);
//        for (String i : keymapFileNames) {
//            destCheckbox.addItem(defaultBundledKeymaps.getKeyFromFileName(i));
//        }

        JDialog jDialog = new JDialog();
        jDialog.setBounds(100, 20, 500, 300);
        MainPanel mainPanel = new MainPanel(new GridBagLayout());
        mainPanel.setDataAndView();
//        JPanel panel = new JPanel();    //创建面板
//        panel.setBorder(JBUI.Borders.empty(20));
//        panel.setLayout(new GridLayout(4, 2, 30, 30)); //指定面板的布局为GridLayout，4行4列，间隙为5
//
//        JLabel sLabel = new JLabel("源平台");
//        JLabel dLabel = new JLabel("目标平台");
//        sLabel.setSize(80, 20);
//        dLabel.setSize(80, 20);
//        panel.add(sLabel);
//        panel.add(dLabel);

//        panel.add(sourceCheckbox);
//        sourceCheckbox.setSelectedIndex(0);
//        panel.add(destCheckbox);
//        destCheckbox.setSelectedIndex(0);

//        TextField textField = new TextField(1);
//        textField.setSize(80, 30);
//        textField.setEditable(false);
//        textField.setColumns(1);
//        textField.setBackground(JBColor.background());
//        textField.addKeyListener(new MyKeyListener() {
//            List<Integer> keycodes = new ArrayList<>();
//            int keyPressed = 0;
//
//            @Override
//            public void keyPressed(KeyEvent keyEvent) {
////                System.out.println(KeyEvent.getKeyText(keyEvent.getKeyCode()));
//                keyPressed++;
//                keycodes.add(keyEvent.getKeyCode());
//            }
//
//            @Override
//            public void keyReleased(KeyEvent keyEvent) {
//                keyPressed--;
//                if (keyPressed == 0) {
//                    StringBuilder sb = new StringBuilder();
//                    for (int i = 0; i < keycodes.size(); i++) {
//                        String keyText = KeyEvent.getKeyText(keycodes.get(i));
//                        if (keyText.equals("Ctrl")) keyText = "control";
//                        sb.append(keyText);
//                        if (i < keycodes.size() - 1) {
//                            sb.append(" ");
//                        }
//                    }
//                    keycodes.clear();
//                    textField.setText(sb.toString());
//                }
//            }
//        });
//        panel.add(textField);
//
//        JLabel shortcut = new JLabel("快捷键");
//        shortcut.setSize(80, 30);
//        panel.add(shortcut);
//
//        JLabel des = new JLabel("描述");
//        des.setSize(80, 30);
//        panel.add(des);
//
//        new ShortcutTextField(true);
//        EditorTextField editorTextField = new EditorTextField();
//        JButton search = new JButton("查询");
//        search.setSize(80, 30);
//        search.addActionListener(actionEvent -> {
//            String input = textField.getText();
//            if (TextUtils.isEmpty(input)) {
//                Messages.showInfoMessage("输入快捷键", "错误");
//                return;
//            }
//            Keymap activeKeymap = KeymapManager.getInstance().getActiveKeymap();
//            if (activeKeymap != null) {
//                String s = sourceCheckbox.getSelectedItem().toString();
//                Keymap keymap = KeymapManager.getInstance().getKeymap(s);
//                for (String actionId : keymap.getActionIds()) {
//                    Shortcut[] copies = activeKeymap.getShortcuts(actionId);
//                    for (Shortcut copy : copies) {
//                        println(copy.toString());
//                    }
//                }
//                return;
//            }
////            Messages.showInfoMessage("没有找到目标快捷键", "错误");
////            StringBuilder sb1 = new StringBuilder();
////            for (Element element : chortcut) {
////                sb1.append(element.getAttribute("first-keystroke").getValue()).append("\n");
////            }
////            shortcut.setText(sb1.toString());
////            des.setText("快捷键称呼: " + id);
//        });
//        panel.add(search);
//        KeymapPanel keymapPanel = new KeymapPanel();
        jDialog.getContentPane().add(mainPanel);
        jDialog.setVisible(true);
    }

    public static <T> boolean isNullOrEmpty(Collection<T> collection) {
        if (collection == null || collection.isEmpty()) {
            return true;
        }
        return false;
    }

    private static void println(String s) {
        System.out.println(s);
    }
}
