package xyz.nopalfi;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLDefaultMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.audio.Sound;
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

import static com.almasb.fxgl.dsl.FXGL.*;

import java.util.List;

public class BaseGame extends GameApplication {

    private BirdComponent birdComponent;

    private boolean isPlaying = false;
    private boolean isJumping = false;
    private Texture messageTexture;
    private Sound wing;
    private static Entity bird;
    private static Entity pipeUp;
    private static Entity pipeDown;
    public static void main( String[] args ) {
        launch(args);
    }

    @Override
    protected void initGame() {
        FXGLDefaultMenu defaultMenu = new FXGLDefaultMenu(MenuType.GAME_MENU);
        SelfScrollingBackgroundView scrollView = new SelfScrollingBackgroundView(getAssetLoader().loadImage("base.png"), 388, 112, Orientation.HORIZONTAL, 100);
        entityBuilder().view(scrollView).buildAndAttach();
        messageTexture = getAssetLoader().loadTexture("message.png");
        wing = getAssetLoader().loadSound("wing.wav");
        getGameWorld().addEntityFactory(new BirdFactory());
        getGameScene().setBackgroundRepeat("background-day.png");
        messageTexture.setTranslateX(85);
        messageTexture.setTranslateY(100);
        messageTexture.setFitWidth(220);
        messageTexture.setFitHeight(395);
        scrollView.setTranslateY(510);
        getGameScene().addUINodes(messageTexture);
    }

    @Override
    protected void initSettings(GameSettings gameSettings) {
        gameSettings.setWidth(388);
        gameSettings.setHeight(612);
        gameSettings.setTitle("FlappyBirdRL-Java");
        gameSettings.setVersion("1.0");
    }

    @Override
    protected void initPhysics() {
        getPhysicsWorld().setGravity(0, 1600);
    }

    @Override
    protected void initInput() {
        onKeyDown(KeyCode.SPACE, () -> {
            if (!isPlaying) {
                spawn("bird");
                getGameScene().removeUINode(messageTexture);
                isPlaying = true;
            }
            getAudioPlayer().playSound(wing);
            bird.getComponent(PhysicsComponent.class).setVelocityY(-500);
        });
        onKeyDown(KeyCode.ESCAPE, () -> {
            getGameController().startNewGame();
        });
    }

    public static class BirdFactory implements EntityFactory {
        @Spawns("bird")
        public Entity newBird(SpawnData data) {
            PhysicsComponent physicsComponent = new PhysicsComponent();
            physicsComponent.setBodyType(BodyType.DYNAMIC);
            var channel = new AnimationChannel(List.of(
                    image("redbird-downflap.png"),
                    image("redbird-midflap.png"),
                    image("redbird-upflap.png"),
                    image("redbird-midflap.png")
            ), Duration.seconds(0.75));
            bird = entityBuilder()
                    .view(new AnimatedTexture(channel).loop())
                    .with(physicsComponent)
                    .at(180, 360)
                    .buildAndAttach();
            return bird;
        };
    }

    public static class PipeFactory implements EntityFactory {
        @Spawns("pipeUp")
        public Entity newPipeUp(SpawnData data) {
            var pipeUpTexture = getAssetLoader().loadTexture("pipe-green.png");
            pipeUp = entityBuilder()
                    .view(pipeUpTexture)
                    .build();
            return pipeUp;
        }

        @Spawns("pipeDown")
        public Entity newPipeDown(SpawnData data) {
            var pipeUpTexture = getAssetLoader().loadTexture("pipe-green.png");
            pipeUpTexture.setRotate(180);
            pipeDown = entityBuilder()
                    .view(pipeUpTexture)
                    .build();
            return pipeDown;
        }

    }
}
