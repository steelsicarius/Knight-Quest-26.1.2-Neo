package dev.xylonity.knightquest.common.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import org.jspecify.annotations.Nullable;

/**
 * Port note (26.x): TextureSheetParticle merged into SingleQuadParticle; the custom
 * double-quad render() of the original is simplified to the standard quad (alpha
 * fade-in/out behaviour is preserved).
 */
public class PoisonCloudParticle extends SingleQuadParticle {
    private final SpriteSet spritesset;

    protected PoisonCloudParticle(ClientLevel world, double x, double y, double z, SpriteSet sprites, double velX, double velY, double velZ) {
        super(world, x, y + 0.5, z, 0.0, 0.0, 0.0, sprites.get(world.getRandom()));

        this.quadSize = 2.0F;
        this.rCol = 1F;
        this.gCol = 1F;
        this.bCol = 1F;
        this.lifetime = (int)((1.4 + this.random.nextDouble() * 2.0) * 20);
        this.setSpriteFromAge(sprites);
        this.spritesset = sprites;
        this.setParticleSpeed(0, 0, 0);

        this.alpha = 0.0F;

        float darknessFactor = 0.75F + (this.random.nextFloat() * 0.25F);
        this.setColor(darknessFactor * this.rCol, darknessFactor * this.gCol, darknessFactor * this.bCol);
    }

    @Override
    protected SingleQuadParticle.Layer getLayer() {
        return SingleQuadParticle.Layer.TRANSLUCENT_TERRAIN;
    }

    @Override
    public void tick() {
        super.tick();
        int fadeInEnd = 20;
        int fadeOutStart = this.lifetime - 20;

        if (this.age < fadeInEnd) {
            this.alpha = (float)this.age / (float)fadeInEnd;
        } else if (this.age >= fadeOutStart) {
            float fadeProgress = (float)(this.age - fadeOutStart) / 20.0F;
            this.alpha = 1.0F - fadeProgress;
        } else {
            this.alpha = 1.0F;
        }

        if (this.age >= this.lifetime) {
            this.remove();
        }

        this.setSpriteFromAge(spritesset);
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet spriteSet) {
            this.sprites = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType particleType, ClientLevel level,
                                       double x, double y, double z,
                                       double dx, double dy, double dz, RandomSource random) {
            return new PoisonCloudParticle(level, x, y, z, this.sprites, dx, dy, dz);
        }
    }

}
