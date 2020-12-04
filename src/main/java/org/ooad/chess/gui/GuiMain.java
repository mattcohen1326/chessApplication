package org.ooad.chess.gui;

import org.ooad.chess.gui.model.Window;
import org.ooad.chess.gui.scenes.main.MainMenu;

public class GuiMain {

    public static void main(String[] args) {
        Window window = new Window("OOAD Final Project: Chess");
        window.setScene(new MainMenu());
        window.open();
    }
}
