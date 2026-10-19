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
- [x] Materials: `Tier` -> `ToolMaterial`, `ArmorMaterial` registry -> data-driven records
- [x] Items: swords without `SwordItem` (`Properties#sword`), armor without `ArmorItem`
      (`Properties#humanoidArmor`), new `TooltipDisplay` tooltip signature
- [x] Weapons (Paladin, Khopesh, Kukri, Uchigatana, Nail, Cleaver) + unified `InteractionResult`
- [x] Entities + AI + boss (Netherman) + custom navigators
- [x] Events (spawn placements, Samhain spawn, datagen gather)
- [x] Client: GeckoLib 5 models/renderers, particles, armor items
- [x] Global loot modifiers (v2 datagen)
- [ ] Boss music (was KnightLib `IBossMusicProvider`) — dropped, see below

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
| Great Chalice interop of essences         | Dropped (plain items)                                        |
| `knightlib:filled_grail` in recipes       | Remapped to `knightquest:filled_goblet`                      |
| KnightLib `STARSET` particle              | New `knightquest:starset` particle (spark textures)          |
| Netherman boss music (`IBossMusicProvider`) | Dropped                                                    |
| KnightLib items in creative tab           | KQ essence/goblet items shown instead                        |

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
