package EntityEngine.Renderer;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.Array;
import com.crashinvaders.vfx.VfxManager;
import com.crashinvaders.vfx.effects.ChainVfxEffect;

public class PostProcessing {
    public VfxManager vfxManager;
    public Array<ChainVfxEffect> effects;
    public boolean active = false;
    public PostProcessing(){
        vfxManager = new VfxManager(Pixmap.Format.RGBA8888);
        effects = new Array<>();
        vfxManager.setBlendingEnabled(true);
    }

    public void beginRender(){
        if (!active)
            return;

        vfxManager.cleanUpBuffers();
        vfxManager.beginInputCapture();
    }

    public void endRender(float dt){
        if (!active)
            return;

        vfxManager.endInputCapture();
        vfxManager.update(dt);
        vfxManager.applyEffects();
        vfxManager.renderToScreen();
    }

    public void dispose(){
        for (int i = 0; i < effects.size; i++){
            effects.get(i).dispose();
        }

        vfxManager.dispose();
    }

    public void addEffect(ChainVfxEffect effect){
        active = true;
        vfxManager.addEffect(effect);
        effects.add(effect);
    }
}
