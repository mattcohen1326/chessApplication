package org.ooad.chess.gui.screens.game;

import com.jogamp.opengl.GL2;
import org.ooad.chess.gui.graphics.GraphicsUtils;
import org.ooad.chess.gui.model.Component;
import org.ooad.chess.gui.model.DrawBox;
import org.ooad.chess.model.ChessmanColor;
import org.ooad.chess.model.ChessmanTypes;

import java.util.HashMap;
import java.util.Map;

import static org.ooad.chess.gui.graphics.GraphicsUtils.loadTexture;

class GameChessman extends Component {

    private static final Map<String, Integer> TEXTURES = new HashMap<>() {
        {
            for (ChessmanTypes type : ChessmanTypes.values()) {
                for (ChessmanColor color : ChessmanColor.values()) {
                    String textureName = String.format("chess-%s-%s.png",
                            type.name().toLowerCase(),
                            color.name().toLowerCase());
                    put(getKey(color, type), loadTexture(textureName));
                }
            }
        }
    };

    private final ChessmanColor color;
    private final ChessmanTypes type;
    private final int texture;

    GameChessman(ChessmanColor color, ChessmanTypes type) {
        this.color = color;
        this.type = type;
        this.texture = TEXTURES.get(getKey(color, type));
    }

    @Override
    public void drawBeforeChildren(GL2 gl, DrawBox drawBox) {
        GraphicsUtils.drawTexture(gl, texture);
    }

    private static String getKey(ChessmanColor color, ChessmanTypes type) {
        return String.format("%s-%s", color.name(), type.name());
    }
}
