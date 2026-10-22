# Knight Quest — NeoForge 26.1.2

Port of [Knight Quest](https://github.com/Xylonity/Knight-Quest) (original NeoForge 1.21.1
multi-loader project at `Knight-Quest-1.21-multi`) to **NeoForge 26.1.2 / Minecraft 26.1.2**
(Java 25, ModDevGradle 2.0.148, GeckoLib 5.5.2).

## Build

```bash
JAVA_HOME=/usr/java/jdk-25-oracle-aarch64 ./gradlew build
```

Run the client/server dev instances with the usual ModDevGradle tasks (`runClient`, `runServer`).
Use port 25599 for test servers (`server.properties` or `--port 25599`) since 25565 is taken.

### Running `runClient` on this aarch64 host

The Minecraft 26.x library set only ships `natives-linux` (x64) LWJGL binaries, so `runClient`
dies on load on this machine unless the arm64 natives are provided. Working recipe:

1. Download the `natives-linux-arm64` jars for the ten LWJGL 3.4.1 modules used by MC
   (`lwjgl`, `lwjgl-freetype`, `lwjgl-glfw`, `lwjgl-jemalloc`, `lwjgl-openal`, `lwjgl-opengl`,
   `lwjgl-shaderc`, `lwjgl-stb`, `lwjgl-tinyfd`, `lwjgl-spvc`; several are already in the local
   Gradle cache) and extract the `.so` files into one directory, e.g. `/tmp/lwjgl-arm64-natives`.
2. `libopenal.so` from LWJGL 3.4.1 needs `GLIBCXX_3.4.30`, newer than Oracle Linux 9's
   libstdc++ — grab `libstdc++.so.6` from a conda-forge `libstdcxx` aarch64 package into e.g.
   `/tmp/stdcxx/lib` and export `LD_LIBRARY_PATH=/tmp/stdcxx/lib`.
3. Add `jvmArgument '-Dorg.lwjgl.librarypath=/tmp/lwjgl-arm64-natives'` to the `client` run
   (temporarily) and run:
   `LD_LIBRARY_PATH=/tmp/stdcxx/lib JAVA_HOME=/usr/java/jdk-25-oracle-aarch64 DISPLAY=:99 ./gradlew runClient --no-daemon`
   (the box has an Xvfb on `:99`; `--no-daemon` so the game process inherits the env).
4. Optional smoke test: `--quickPlayMultiplayer localhost:25599` auto-joins the dev server, and
   `onboardAccessibility:false` in `run/options.txt` stops the accessibility onboarding screen
   from blocking quickplay. Sound will be off (`Failed to open OpenAL device` — no audio
   hardware; MC handles it).

## Port status

### Java code
- [x] Entry point, platform layer (ServiceLoader), config (`ModConfigSpec`)
- [x] Server verified: `./gradlew runServer` boots a dedicated server (port 25599, IPv4)
      with the mod fully registered and datapack loaded without errors
- [x] Materials: `Tier` -> `ToolMaterial`, `ArmorMaterial` registry -> data-driven records
- [x] Items: swords without `SwordItem` (`Properties#sword`), armor without `ArmorItem`
      (`Properties#humanoidArmor`), new `TooltipDisplay` tooltip signature
- [x] Weapons (Paladin, Khopesh, Kukri, Uchigatana, Nail, Cleaver) + unified `InteractionResult`
- [x] Entities + AI + boss (Netherman) + custom navigators
- [x] Events (spawn placements, Samhain spawn, datagen gather)
- [x] Client: GeckoLib 5 models/renderers, particles, armor items
- [x] Global loot modifiers (v2 datagen)
- [ ] Boss music (was KnightLib `IBossMusicProvider`) — dropped, see below
- [ ] Client visual polish pending runtime verification on a display (headless server only):
      geo head-tracking (`setCustomAnimations`) is ported to render-state data tickets but the
      final bone-rotation application is not wired yet; v4 glow layers (AutoGlowingGeoLayer)
      and the Fallen Knight armor/held-item geo layers were dropped because GeckoLib 5 layers
      require the runtime-injected `GeoRenderState` interface (compile-time it is not visible).
      The axe/projectile custom orientation is also simplified.

### Resources
- [x] Textures/lang/sounds/particles copied as-is
- [x] Geo models moved to `assets/knightquest/geckolib/models/{entity,item}/` and animations to
      `assets/knightquest/geckolib/animations/{entity,item}/` (GeckoLib 5 layout)
- [x] Item model definitions generated under `assets/knightquest/items/`
- [x] Equipment assets generated under `assets/knightquest/equipment/` (+ textures under
      `textures/entity/equipment/humanoid{,_leggings}/`)
- [x] Spawn-egg textures generated (tinted) since spawn eggs no longer use a runtime-tinted template
- [x] Recipes converted to the 26.x ingredient format
- [x] `starset` particle added as the replacement for KnightLib's `STARSET`

## Intentional simplifications (KnightLib dependency removed)

The original mod depends on `knightlib` 1.5.0 (a companion library by the same author) which is
not available for 26.1.2. Its usages were replaced in-mod:

| Original (KnightLib)                     | Ported behaviour                                             |
|------------------------------------------|--------------------------------------------------------------|
| `knightlib:great_essence` / `small_essence` items | Added as `knightquest:great_essence` / `knightquest:small_essence` |
| Great Chalice activation of weapons       | Weapons are always activated (no chalice step)               |
| Great Chalice interop of essences         | Reimplemented in-mod: `knightquest:great_chalice` block +    |
|                                           | block entity (charges + chaotic state), essence/weapon/      |
|                                           | goblet interactions and Netherman summon restored            |
| `knightlib:filled_grail` in recipes       | Remapped to `knightquest:filled_goblet`                      |
| KnightLib `STARSET` particle              | New `knightquest:starset` particle (spark textures)          |
| Netherman boss music (`IBossMusicProvider`) | Dropped                                                    |
| KnightLib items in creative tab           | Great Chalice block + KQ essence/goblet items                |

Epic Fight capability JSONs (`data/knightquest/capabilities/weapons`) are kept verbatim; they are
inert without the Epic Fight compat stack.

## Pending from the parity audit (vs 1.21.1 + KnightLib 1.5.0)

Full audit done 2026-10-22 comparing registries, assets, data, recipes, loot, configs, entities,
weapons, armor and the KnightLib interop. Verified 1:1: items/entities/particles/sounds/creative
tab, all assets (reorganised to the v5 layout), all data (recipes/loot/tags/spawns/jukebox/Epic
Fight capabilities, `filled_grail` -> `filled_goblet` in 170 recipes), the Great Chalice interop
(12 charges, +4/+1 essences, goblet drain, chaotic attunement, radiant summon, weapon
activation), the Netherman (goals/phases/music) and the 122 config values. Two real gaps found:

- **`KQArmorEvents` (neoforge 1.21.1) was not ported** — the 467-line behaviour layer with ALL
  damage/death/tick/arrow based bonuses. Missing:
  - weapon passives: Kukri freeze-on-hit; Uchigatana active bonus (+darkness; the
    `ShouldDoActiveAttack` tag is set but nothing consumes it) and its low-HP extra damage;
    Cleaver high-HP extra damage; Khopesh reflect window + burn chance; Paladin held-weapon
    HP regen; Tengu-helmet/activated-Nail mid-air double jump.
  - armour set bonuses: Squire (damage received x), Deepslate (fall damage x), Evoker
    (darkness chance on attacker), Blaze (ignite attacker), Dragon (dragon breath immunity +
    damage x), Bamboo Green (poison removal/damage cancel), Shinobi (speed when hit), Bamboo
    (fall: smoke ring + knockback), Enderman (dodge teleport), Veteran (buffs below 50% HP),
    Forze (deflect), Creeper (explosion x), Polar (freeze immunity), Silver (night burn on
    attack), Hollow (lifesteal), Wither (arrow wither chance), Apple (regen on apple eaten),
    Conquistador (Strength on kill), Witch (regen on kill), Skeleton (arrow pierce 5), Husk
    (resistance in desert/badlands/beach), Bamboo Blue (jungle speed), Warlord (resistance
    aura), Zombie (night regen), Silverfish (haste below Y level).
    Note: the new `KQArmorItem` tooltips already *describe* these bonuses (same config values),
    so the sets currently advertise effects that do not trigger. Only the 13 inventoryTick
    effect sets (Path, Bow, Bat, Shield, Phantom, Horn, Sea, Pirate, Spider, Nether, Skulk,
    Strawhat + tooltips) are active.
- **KnightLib's `homunculus` item was not ported**: it is in the original creative tab and
    craftable (leather + filled grail + rotten flesh + soul sand + bone) and exposes the
    `IHomunculusInteractable` interop. No KQ/KL block implements the interface, so it is
    inert-but-obtainable content in the original; porting it is cosmetic interop parity.

Not gaps (verified): Momma Lizzy is unregistered dead code in both versions (commented out in
1.21.1); boss music IS implemented (the two "dropped" README notes above are stale); the
Great Chalice starset ring entity is intentionally replaced by the `starset` particle.

## Notable API migrations (1.21.1 -> 26.1.2)

- `ResourceLocation` -> `Identifier`
- `Tier` -> `ToolMaterial` (record), repair via item tags (`knightquest:great_essence`)
- `ArmorMaterial` is no longer a registry: plain records applied via
  `Item.Properties#humanoidArmor(material, ArmorType)`; visual layers now data-driven
  (`assets/knightquest/equipment/*.json`)
- `SwordItem`/`ArmorItem` removed: plain `Item` + `Properties#sword(...)`
- `MobEffects` renames: `DAMAGE_RESISTANCE`->`RESISTANCE`, `DAMAGE_BOOST`->`STRENGTH`,
  `JUMP`->`JUMP_BOOST`, `MOVEMENT_SPEED`->`SPEED`
- `Level.isDay()/isNight()` -> `isBrightOutside()/isDarkOutside()`
- `Item.inventoryTick` is server-side only and takes `ServerLevel`
- `InteractionResultHolder` removed: `Item.use` returns the unified `InteractionResult`
- GeckoLib 4 (`software.bernie.geckolib`) -> GeckoLib 5 (`com.geckolib`): `AnimationTest`-based
  controllers, `DefaultedEntityGeoModel`, render-state based models, head tracking moved to
  `adjustModelBonesForRender`
- Access transformer updated for the `AbstractArrow` package move
- Dropped files: `datagen/KQItemModelProvider.java` (NeoForge removed `ItemModelProvider`);
  mixin configs (empty, KnightLib-only)

## Changelog of the port

- 26.1.2-1.9.3: initial port (feature-parity with 1.21.1 v1.9.3, minus the KnightLib features above).
- Fix: summon crash (`Can't find attribute minecraft:tempt_range`) — `TemptGoal` now reads the
  `tempt_range` attribute which is no longer implied by `Monster.createMobAttributes()`; added to
  Gremlin and Bad Patch (the only mobs with a TemptGoal).
- Fix: Netherman could get permanently stuck in its summoning state (invulnerable, no movement,
  no targeting) after a save/reload skipped the exact `tickCount == 100` transition; all one-shot
  transitions now use `>=`. Boss music added as a looping sound while the boss is alive (replaces
  KnightLib `IBossMusicProvider`). Radiant essence summons on a full NORMAL chalice (no chaotic
  attunement needed), matching the original gating.
- Fix: Great Chalice visuals — replaced the flat cross model with a 3D hollow-cup model (vanilla
  copper textures), proper collision shape, `noOcclusion`, 3D inventory render and the original
  8-copper-ingot ring recipe.
- Fix: Great Chalice purple-checker cube + raw `item.knightquest.great_chalice` name — 26.x no
  longer supports the `builtin/entity` model parent (the block model baked as the missing-model
  cube); replaced with an empty parentless model (the geo BER draws everything), the BER is now
  registered in `EntityRenderersEvent.RegisterRenderers`, and the block item uses the block
  description prefix.
- Fix: Gran Cáliz ITEM missing texture (purple/black checkerboard in the hotbar, inventory,
  hand and as a dropped item) — the item definition `items/great_chalice.json` used
  `{"type": "geckolib:geckolib"}` as a top-level ITEM-MODEL type, but GeckoLib 5.5.2 registers
  `geckolib:geckolib` as a *SpecialModelRenderer* type (through its `SpecialModelRenderersMixin`
  on `SpecialModelRenderers.ID_MAPPER`), not as an `ItemModel.Unbaked` type. The only item-model
  types in 26.x are `minecraft:{empty, model, range_dispatch, special, composite,
  bundle/selected_item, select, condition}`, so the definition failed to parse
  (`Couldn't parse item model 'knightquest:great_chalice'`) and the item fell back to the
  missing model. Fixed by wrapping it the same way vanilla wraps the shield:
  `{"model": {"type": "minecraft:special", "base": "knightquest:block/great_chalice",
  "model": {"type": "geckolib:geckolib"}}}` — the `base` block model supplies the display
  transforms (gui/hand/ground) and the particle texture, and GeckoLib's
  `GeckolibItemSpecialRenderer` renders the geo model via the `GeoItemRenderer` registered in
  `GreatChaliceBlockItem#createGeoRenderer`. Verified in-game on this host: 3D icon in the
  hotbar, first-person held-item render, no parse errors and zero missing-texture pixels
  (capture: `logs/verificacion_chalice_fixed.png`).
- Port: the ORIGINAL KnightLib Great Chalice (from the Knight-Lib-1.21.1 sources): GeckoLib geo
  block with the `liquid` bone that rises per charge (12 max charges, hidden when empty) and
  swaps to the chaotic texture when attuned, idle animation, animated textures, block entity
  particles, the `IGreatChaliceInteractable` system (great essence +4, small essence +1,
  chaotic essence attunes, radiant essence summons the Netherman, empty goblet drains into a
  filled goblet) and the item renderer for the 3D inventory display. Original 8-copper recipe.
- Add: Great Chalice block (`knightquest:great_chalice`) — block entity with 4 essence charges
  and chaotic state. Offer great essence (+2) or small essence (+1) until full; offer chaotic
  essence to attune it (weapons can then be activated by interacting); offer radiant essence to
  summon the Netherman; offer empty goblets to gather filled goblets (armor recipes). Crafted
  from copper ingots; great essence craftable from 4 small essences. Weapon abilities are gated
  behind chalice activation again (vanilla-style `Activated` custom data).
- Fix: missing textures — spawn egg models regenerated as `minecraft:item/generated` with
  pre-tinted textures (the old `item/template_spawn_egg` parent was removed in 26.x);
  `great_essence`/`small_essence` item models + definitions added (new KnightLib-replacement
  items had no model); swampman axe uses the swampman texture/animation (no axe-specific asset exists).
