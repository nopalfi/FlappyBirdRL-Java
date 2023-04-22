package xyz.nopalfi;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.app.scene.SceneFactory;
import org.jetbrains.annotations.NotNull;

public class MyCustomFactoryScene extends SceneFactory {
    @NotNull
    @Override
    public FXGLMenu newGameMenu() {
        return new MyCustomGameMenu(MenuType.GAME_MENU);
    }

    @NotNull
    @Override
    public FXGLMenu newMainMenu() {
        return new MyCustomGameMenu(MenuType.MAIN_MENU);
    }
}
