package cn.baililuohui.shortcut_transform;

import com.intellij.openapi.actionSystem.Shortcut;

import javax.swing.*;
import java.awt.*;

public class MyShortcutPanel<T extends Shortcut> extends JPanel {
    private T myShortcut;

    MyShortcutPanel(LayoutManager layout) {
        super(layout);
    }

    T getShortcut() {
        return myShortcut;
    }

    void setShortcut(T shortcut) {
        T old = myShortcut;
        myShortcut = shortcut;
        firePropertyChange("shortcut", old, shortcut);
    }
}
