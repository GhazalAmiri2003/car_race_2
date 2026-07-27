package gui.raceview;

import driver.UserInteraction;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class RaceInputBinder {
    //COMPONENT is race panel here which extends J panel
    private final JComponent component;
    private final UserInteraction input;

    public RaceInputBinder(JComponent component, UserInteraction input) {
        if (component == null) {
            throw new IllegalArgumentException("Component cannot be null.");
        }
        if (input == null) {
            throw new IllegalArgumentException("Input cannot be null.");
        }

        this.component = component;
        this.input = input;
    }

    public void install() {
        bind("pressed F", "fast", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                input.pressFastKey();
               // پنل رو دوباره بکش
               // تا اگر UI چیزی برای تغییر داشت، سریع دیده بشه.
                component.repaint();
            }
        });

        bind("pressed S", "safe", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                input.pressSafeKey();
                component.repaint();
            }
        });

        bind("pressed J", "jump", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                input.pressJumpKey();
                component.repaint();
            }
        });

        bind("pressed SPACE", "turn", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                input.pressTurnKey();
                component.repaint();
            }
        });
    }
    //یک دکمه‌ی کیبورد رو به یک کار وصل می‌کنه.
    private void bind(String keyStroke, String name, Action action) {
        //این کلید = این اسم
        component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(keyStroke), name);
        //این اکشن = این اسم
        component.getActionMap().put(name, action);
    }
}