package TestFiles.scripts.Shaders;

import EntityEngine.Systems.System;
import com.crashinvaders.vfx.effects.BloomEffect;
import com.crashinvaders.vfx.effects.FilmGrainEffect;
import com.crashinvaders.vfx.effects.VignettingEffect;

public class ShaderTest extends System {

    @Override
    public void onCreate() {

        BloomEffect bloomEffect = new BloomEffect();
        bloomEffect.setBloomIntensity(1.5f);
        engine.addEffect(bloomEffect);

        VignettingEffect vignettingEffect = new VignettingEffect(false);
        engine.addEffect(vignettingEffect);
        engine.addEffect(new FilmGrainEffect());

    }
}
