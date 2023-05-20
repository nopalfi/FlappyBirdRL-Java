package xyz.nopalfi;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.audio.Sound;
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

import static com.almasb.fxgl.dsl.FXGL.*;

public class BaseGame extends GameApplication {

    private BirdComponent birdComponent;

    private boolean isPlaying = false;
    private boolean isJumping = false;
    private Texture messageTexture;
    private Sound wing;
    private static Entity bird;
    private static Entity pipeUp;
    private static Entity pipeDown;
    private Sound die;
    private Sound hit;
    public static void main( String[] args ) {
        launch(args);
    }

    @Override
    protected void initGame() {
        Texture baseTexture = getAssetLoader().loadTexture("base.png");
        SelfScrollingBackgroundView scrollView = new SelfScrollingBackgroundView(getAssetLoader().loadImage("base.png"), 500, 112, Orientation.HORIZONTAL, 100);
        entityBuilder().type(EntityType.WALL).viewWithBBox(scrollView).collidable().buildAndAttach();
        messageTexture = getAssetLoader().loadTexture("message.png");
        wing = getAssetLoader().loadSound("wing.wav");
        die = getAssetLoader().loadSound("die.wav");
        hit = getAssetLoader().loadSound("hit.wav");
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
        gameSettings.setSceneFactory(new MyCustomFactoryScene());
        gameSettings.setWidth(388);
        gameSettings.setHeight(612);
        gameSettings.setTitle("FlappyBirdRL-Java");
        gameSettings.setVersion("1.0");
    }

    @Override
    protected void initPhysics() {
        onCollisionBegin(EntityType.PLAYER, EntityType.WALL, (player, wall) -> {
            System.out.println("Hit wall");
        });
        getPhysicsWorld().setGravity(0, 1600);
    }

    @Override
    protected void onUpdate(double tpf) {
        if (isPlaying) {
            if (bird.getPosition().getY() > 490) {
                getAudioPlayer().playSound(hit);
                isPlaying = false;
                getGameController().startNewGame();
            }
        }
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
                    .viewWithBBox(new AnimatedTexture(channel).loop())
                    .with(physicsComponent)
                    .collidable()
                    .at(180, 360)
                    .build();
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
