package org.ooad.chess.gui;

import org.ooad.chess.gui.component.BackgroundColorComponent;
import org.ooad.chess.gui.component.FixedAspectComponent;
import org.ooad.chess.gui.model.Component;
import org.ooad.chess.gui.model.Window;
import org.ooad.chess.gui.screens.game.GameScreen;

import java.awt.*;

public class GuiMain {

    public static void main(String[] args) {
//        Component activeScreen = new MainMenu();
        Component activeScreen = new GameScreen();

        Window window = new Window("Main");
        BackgroundColorComponent fullBg = new BackgroundColorComponent(new Color(66, 66, 66));
        FixedAspectComponent letterbox = new FixedAspectComponent(1, activeScreen);
        fullBg.addChild(letterbox);
        window.setActiveComponent(fullBg);
        window.open();
    }
}
