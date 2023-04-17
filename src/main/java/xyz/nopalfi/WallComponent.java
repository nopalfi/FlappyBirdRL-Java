package xyz.nopalfi;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.texture.Texture;

import static com.almasb.fxgl.dsl.FXGL.*;

public class WallComponent extends Component {
    private double lastWall = 1000;

    @Override
    public void onUpdate(double tpf) {
        if (lastWall - entity.getX() < FXGL.getAppWidth()) {
            buildWalls();
        }
    }

    private Texture wallTexture() {
        return getAssetLoader().loadTexture("base.png");
    }

    private void buildWalls() {
        double height = getAppHeight();
        double distance = height / 2;

        for (int i = 0; i <= 10; i++) {
            double topHeight = Math.random() * (height - distance);

            entityBuilder()
                    .type(EntityType.WALL)
                    .at(lastWall + i * 500, -25)
                    .view("base.png")
                    .with(new CollidableComponent(true))
                    .buildAndAttach();

            entityBuilder()
                    .type(EntityType.WALL)
                    .at(lastWall + i * 500, 0 + topHeight + distance -25)
                    .view("base.png")
                    .with(new CollidableComponent(true))
                    .buildAndAttach();
        }
        lastWall += 10 * 500;
    }
}
