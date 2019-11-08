package cn.baililuohui.shortcut_transform;

import com.intellij.openapi.keymap.KeymapUtil;
import com.intellij.openapi.util.registry.Registry;
import com.intellij.ui.KeyStrokeAdapter;
import com.intellij.util.ui.accessibility.ScreenReader;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.KeyEvent;

public class ShortcutInput extends JTextField {
    private KeyStroke myKeyStroke;
    private int myLastPressedKeyCode = KeyEvent.VK_UNDEFINED;

    ShortcutInput(boolean isFocusTraversalKeysEnabled) {
        enableEvents(AWTEvent.KEY_EVENT_MASK);
        setFocusTraversalKeysEnabled(isFocusTraversalKeysEnabled);
        setCaret(new DefaultCaret() {
            @Override
            public boolean isVisible() {
                return false;
            }
        });
    }

    private static boolean absolutelyUnknownKey(KeyEvent e) {
        return e.getKeyCode() == 0
                && e.getKeyChar() == KeyEvent.CHAR_UNDEFINED
                && e.getKeyLocation() == KeyEvent.KEY_LOCATION_UNKNOWN
                && e.getExtendedKeyCode() == 0;
    }

    @Override
    protected void processKeyEvent(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (getFocusTraversalKeysEnabled() && e.getModifiers() == 0 && e.getModifiersEx() == 0) {
            if (keyCode == KeyEvent.VK_ESCAPE || (keyCode == KeyEvent.VK_ENTER && myKeyStroke != null)) {
                super.processKeyEvent(e);
                return;
            }
        }

        final boolean isNotModifierKey = keyCode != KeyEvent.VK_SHIFT &&
                keyCode != KeyEvent.VK_ALT &&
                keyCode != KeyEvent.VK_CONTROL &&
                keyCode != KeyEvent.VK_ALT_GRAPH &&
                keyCode != KeyEvent.VK_META &&
                !absolutelyUnknownKey(e);

        if (isNotModifierKey) {
            if (e.getID() == KeyEvent.KEY_PRESSED || (e.getID() == KeyEvent.KEY_RELEASED && myLastPressedKeyCode != keyCode)) {
                setKeyStroke(KeyStrokeAdapter.getDefaultKeyStroke(e));
            }

            if (e.getID() == KeyEvent.KEY_PRESSED)
                myLastPressedKeyCode = keyCode;
        }

        // Ensure TAB/Shift-TAB work as focus traversal keys, otherwise
        // there is no proper way to move the focus outside the text field.
        if (!getFocusTraversalKeysEnabled() && ScreenReader.isActive()) {
            setFocusTraversalKeysEnabled(true);
            try {
                KeyboardFocusManager.getCurrentKeyboardFocusManager().processKeyEvent(this, e);
            } finally {
                setFocusTraversalKeysEnabled(false);
            }
        }
    }

    void setKeyStroke(KeyStroke keyStroke) {
        KeyStroke old = myKeyStroke;
        if (old != null || keyStroke != null) {
            myKeyStroke = keyStroke;
            super.setText(KeymapUtil.getKeystrokeText(keyStroke));
            setCaretPosition(0);
            firePropertyChange("keyStroke", old, keyStroke);
        }
    }

    KeyStroke getKeyStroke() {
        return myKeyStroke;
    }

    @Override
    public void enableInputMethods(boolean enable) {
        super.enableInputMethods(enable && Registry.is("ide.settings.keymap.input.method.enabled"));
    }

    @Override
    public void setText(String text) {
        super.setText(text);
        setCaretPosition(0);
        if (text == null || text.isEmpty()) {
            myKeyStroke = null;
            firePropertyChange("keyStroke", null, null);
        }
    }
}
