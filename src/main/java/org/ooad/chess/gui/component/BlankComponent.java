package org.ooad.chess.gui.component;

import org.ooad.chess.gui.model.Component;

public class BlankComponent extends Component {
    public BlankComponent() {
    }
    public BlankComponent(Runnable runnable) {
        runnable.run();
    }
}
