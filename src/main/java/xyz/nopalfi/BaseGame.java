package xyz.nopalfi;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.views.SelfScrollingBackgroundView;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import com.almasb.fxgl.texture.Texture;
import javafx.geometry.Orientation;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;

import java.util.List;

public class BaseGame extends GameApplication {

    private boolean isPlaying;
    private Texture messageTexture;

    private static Entity bird;
    public static void main( String[] args ) {
        launch(args);
    }

    @Override
    protected void initGame() {
        SelfScrollingBackgroundView scrollView = new SelfScrollingBackgroundView(FXGL.getAssetLoader().loadImage("base.png"), 388, 112, Orientation.HORIZONTAL, 50);
        FXGL.entityBuilder().view(scrollView).buildAndAttach();
        messageTexture = FXGL.getAssetLoader().loadTexture("message.png");
        FXGL.getGameWorld().addEntityFactory(new BirdFactory());
        FXGL.spawn("bird");
        FXGL.getGameScene().setBackgroundRepeat("background-day.png");
        messageTexture.setTranslateX(85);
        messageTexture.setTranslateY(100);
        messageTexture.setFitWidth(220);
        messageTexture.setFitHeight(395);
        scrollView.setTranslateY(510);
        FXGL.getGameScene().addUINodes(messageTexture);
    }

    @Override
    protected void initSettings(GameSettings gameSettings) {
        gameSettings.setWidth(388);
        gameSettings.setHeight(612);
        gameSettings.setTitle("FlappyBirdRL-Java");
        gameSettings.setVersion("1.0");
    }

    @Override
    protected void initInput() {
        FXGL.onKey(KeyCode.SPACE, () -> {

        });
    }

    public static class BirdFactory implements EntityFactory {

        @Spawns("bird")
        public Entity newBird(SpawnData data) {
            PhysicsComponent physicsComponent = new PhysicsComponent();
            physicsComponent.setBodyType(BodyType.DYNAMIC);
            var channel = new AnimationChannel(List.of(
                    FXGL.image("redbird-downflap.png"),
                    FXGL.image("redbird-midflap.png"),
                    FXGL.image("redbird-upflap.png"),
                    FXGL.image("redbird-midflap.png")
            ), Duration.seconds(0.75));
            bird = FXGL.entityBuilder()
                    .view(new AnimatedTexture(channel).loop())
                    .with(physicsComponent)
                    .at(180, 360)
                    .buildAndAttach();
            return bird;
        };
    }
}
