package TestFiles.scripts.Shaders;

import EntityEngine.Systems.System;
import com.badlogic.gdx.graphics.Pixmap;
import com.crashinvaders.vfx.effects.*;
import com.crashinvaders.vfx.effects.util.MixEffect;

public class ShaderTest extends System {

    @Override
    public void onCreate() {

        BloomEffect bloomEffect = new BloomEffect();
        bloomEffect.setBloomIntensity(1.5f);
        engine.postProcessing.addEffect(bloomEffect);

        VignettingEffect vignettingEffect = new VignettingEffect(false);
        engine.postProcessing.addEffect(vignettingEffect);
        engine.postProcessing.addEffect(new FilmGrainEffect());

    }
}
