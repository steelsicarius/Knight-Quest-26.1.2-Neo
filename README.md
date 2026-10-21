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
