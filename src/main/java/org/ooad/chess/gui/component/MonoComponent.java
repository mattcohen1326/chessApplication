package org.ooad.chess.gui.component;

import org.ooad.chess.gui.model.Component;

import java.util.Collections;
import java.util.List;

abstract class MonoComponent extends Component {

    protected final Component child;

    protected MonoComponent(Component child) {
        this.child = child;
    }

    @Override
    public final List<Component> getChildren() {
        return Collections.singletonList(child);
    }

    @Override
    public final boolean addChild(Component component) {
        throw new IllegalStateException("Cannot add child to mono-child component");
    }
}
