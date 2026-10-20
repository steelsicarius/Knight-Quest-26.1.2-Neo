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
 * double-quad render() of the original is simplified to the standard quad.
 */
public class GremlinParticle extends SingleQuadParticle {
    private final SpriteSet spritesset;

    GremlinParticle(ClientLevel world, double x, double y, double z, SpriteSet sprites, double velX, double velY, double velZ) {
        super(world, x, y + 0.5, z, 0.0, 0.0, 0.0, sprites.get(world.getRandom()));

        this.quadSize = (float) velX;
        this.rCol = 1F;
        this.gCol = 1F;
        this.bCol = 1F;
        this.lifetime = (int) (11 + (Math.floor(velX / 5)));
        this.setSpriteFromAge(sprites);
        this.spritesset = sprites;
        this.setParticleSpeed(0, 0, 0);
    }

    @Override
    protected SingleQuadParticle.Layer getLayer() {
        return SingleQuadParticle.Layer.TRANSLUCENT_TERRAIN;
    }

    @Override
    public void tick() {
        super.tick();
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
            return new GremlinParticle(level, x, y, z, this.sprites, dx, dy, dz);
        }
    }

}
