package org.ooad.chess.gui.screens.main;

import org.ooad.chess.gui.component.FixedAspectComponent;
import org.ooad.chess.gui.component.ImageComponent;
import org.ooad.chess.gui.model.Component;

class MainMenuHeader extends Component {

    {
        addChild(new FixedAspectComponent(1, new ImageComponent("home.png")));
//        addChild(new ImageComponent("home.png"));
    }

}
