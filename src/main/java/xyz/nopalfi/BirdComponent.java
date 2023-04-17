package xyz.nopalfi;

import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.entity.component.Component;

import static com.almasb.fxgl.dsl.FXGL.*;

public class BirdComponent extends Component {

    private final Vec2 acceleration = new Vec2(6, 0);

    @Override
    public void onUpdate(double tpf) {
        acceleration.x += tpf * 0.1;
        acceleration.y += tpf * 10;

        if (acceleration.y < -5) acceleration.y = -5;
        if (acceleration.y > 5) acceleration.y = 5;

        entity.translate(acceleration.x, acceleration.y);
    }

    public void jump() {
        acceleration.addLocal(0, -5);
        play("wing.wav");
    }
}
