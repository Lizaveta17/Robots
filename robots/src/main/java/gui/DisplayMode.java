package gui;

import javax.swing.*;

enum DisplayMode {
    METAL("javax.swing.plaf.metal.MetalLookAndFeel"),
    NIMBUS("javax.swing.plaf.nimbus.NimbusLookAndFeel"),
    CROSS_PLATFORM(UIManager.getSystemLookAndFeelClassName()),
    SYSTEM(UIManager.getCrossPlatformLookAndFeelClassName());

    public final String className;

    DisplayMode(String className) {
        this.className = className;
    }
}
