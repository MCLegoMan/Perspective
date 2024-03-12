## **Current Goals for Perspective:**  
- **1.3.0**  
  - Test and Fix **_every_** vanilla textured entity.  
  - Shader Selection Screen will now open when selecting shaders using the config screen.
- Documentation on the [Perspective Wiki](https://mclegoman.com/Perspective)  
- Fix Depth Shaders rendering over the player's hand.  
  - I have found a fix for this in 1.20.4, but it gets broken in 1.20.5 snapshots.
    - As there are some hand renderer related bug fixes marked as "Fixed" in "Future Update", I'll wait till the snapshot drops before trying to make the 1.20.5 fix.
      - Until this is fixed, the Shader Hand Renderer Experiment will render the player's hand over shaders.
- Fix Panorama Screenshot compatibility with iris/canvas/fabulous. (unlikely to be fixed anytime soon, maybe custom screenshotting?)
  - It's possible that fabulous graphics could be fixed first as it's likely to be related to the framebuffers in some way.
- Update Config to use `.json` instead of `.properties`.  
  - This will require both the config to be updated to use JSON and the config updater to update old configs to JSON.  


### Shader Uniforms  
| Uniform                      | Description                           | Recommended Defaults | Extra Info                                              |
|------------------------------|---------------------------------------|----------------------|---------------------------------------------------------|
| `lu_viewDistance`            | Render Distance                       | 12.0                 | This will be moved to a separate library mod in future. |
| `lu_eyePosition`             | Camera Eye Position (x, y, z)         | 0.0, 0.0, 0.0        | This will be moved to a separate library mod in future. |
| `lu_pitch`                   | Player Pitch (0.0-360.0)              | 0.0                  | This will be moved to a separate library mod in future. |
| `lu_yaw`                     | Player Yaw (0.0-360.0)                | 0.0                  | This will be moved to a separate library mod in future. |
| `lu_currentHealth`           | Player Health                         | 20.0                 | This will be moved to a separate library mod in future. |
| `lu_maxHealth`               | Max Player Health                     | 20.0                 | This will be moved to a separate library mod in future. |
| `lu_currentAir`              | Player Air                            | 10.0                 | This will be moved to a separate library mod in future. |
| `lu_maxAir`                  | Max Player Air                        | 10.0                 | This will be moved to a separate library mod in future. |
| `perspective_zoomMultiplier` | Perspective Zoom Multiplier (0.0-1.0) | 1.0                  | This uniform will stay in Perspective.                  |

### Textured Entity Testing:

| **Entity**                         | **Test Status** |  
|------------------------------------|-----------------|
| `minecraft:allay`                  | ✔️              |  
| `minecraft:armadillo`              | ❓               |  
| `minecraft:armor_stand`            | ❓               |  
| `minecraft:arrow`                  | ❓               |  
| `minecraft:axolotl`                | ✔️              |  
| `minecraft:bat`                    | ❓               |  
| `minecraft:bee`                    | ✔️              |  
| `minecraft:blaze`                  | ❓               |  
| `minecraft:boat`                   | ✔️              |  
| `minecraft:breeze`                 | ✔️              |  
| `minecraft:breeze_wind_charge`     | ✔️              |  
| `minecraft:camel`                  | ❓               |  
| `minecraft:cat`                    | ✔️              |  
| `minecraft:cave_spider`            | ❓               |  
| `minecraft:chest_boat`*            | ✔️              |  
| `minecraft:chest_minecart`*        | ❓               |  
| `minecraft:chicken`                | ❓               |  
| `minecraft:cod`                    | ❓               |  
| `minecraft:command_block_minecart` | ❓               |  
| `minecraft:cow`                    | ❓               |  
| `minecraft:creeper`                | ❓               |  
| `minecraft:dolphin`                | ❓               |  
| `minecraft:donkey`                 | ❓               |  
| `minecraft:dragon_fireball`        | ✔️              |  
| `minecraft:drowned`                | ❓               |  
| `minecraft:elder_guardian`         | ❓               |  
| `minecraft:end_crystal`            | ✔️              |
| `minecraft:enderman`               | ✔️              |  
| `minecraft:endermite`              | ✔️              |  
| `minecraft:evoker`                 | ❓               |  
| `minecraft:evoker_fangs`           | ❓               |  
| `minecraft:experience_orb`         | ❓               |  
| `minecraft:firework_rocket`        | ❓               |  
| `minecraft:fox`                    | ✔️              |  
| `minecraft:frog`                   | ❓               |  
| `minecraft:furnace_minecart`       | ❓               |  
| `minecraft:ghast`                  | ❓               |  
| `minecraft:giant`                  | ❓               |  
| `minecraft:glow_squid`             | ❓               |  
| `minecraft:goat`                   | ✔️              |  
| `minecraft:guardian`               | ❓               |  
| `minecraft:hoglin`                 | ❓               |  
| `minecraft:hopper_minecart`        | ❓               |  
| `minecraft:horse`                  | ✔️              |  
| `minecraft:husk`                   | ❓               |  
| `minecraft:illusioner`             | ❓               |  
| `minecraft:iron_golem`             | ✔️              |  
| `minecraft:leash_knot`             | ❓               |  
| `minecraft:llama`                  | ❓               |  
| `minecraft:llama_spit`             | ❓               |  
| `minecraft:magma_cube`             | ❓               |  
| `minecraft:minecart`               | ❓               |  
| `minecraft:mooshroom`              | ❓               |  
| `minecraft:mule`                   | ❓               |  
| `minecraft:ocelot`                 | ❓               |  
| `minecraft:panda`                  | ✔️              |  
| `minecraft:parrot`                 | ✔️              |  
| `minecraft:phantom`                | ❓               |  
| `minecraft:pig`                    | ✔️              |  
| `minecraft:piglin`                 | ❓               |  
| `minecraft:piglin_brute`           | ❓               |  
| `minecraft:pillager`               | ❓               |  
| `minecraft:polar_bear`             | ❓               |  
| `minecraft:pufferfish`             | ❓               |  
| `minecraft:rabbit`                 | ❓               |  
| `minecraft:ravager`                | ❓               |  
| `minecraft:salmon`                 | ❓               |  
| `minecraft:sheep`                  | ❓               |  
| `minecraft:shulker`                | ❓               |  
| `minecraft:shulker_bullet`         | ❓               |  
| `minecraft:silverfish`             | ❓               |  
| `minecraft:skeleton`               | ✔️              |  
| `minecraft:skeleton_horse`         | ❓               |  
| `minecraft:slime`                  | ❓               |  
| `minecraft:sniffer`                | ❓               |  
| `minecraft:snow_golem`             | ❓               |  
| `minecraft:spawner_minecart`       | ❓               |  
| `minecraft:spectral_arrow`         | ❓               |  
| `minecraft:spider`                 | ❓               |  
| `minecraft:squid`                  | ❓               |  
| `minecraft:stray`                  | ✔️              |  
| `minecraft:strider`                | ✔️              |  
| `minecraft:tadpole`                | ❓               |  
| `minecraft:tnt`                    | ❓               |  
| `minecraft:tnt_minecart`           | ❓               |  
| `minecraft:trader_llama`           | ❓               |  
| `minecraft:trident`                | ❓               |  
| `minecraft:tropical_fish`          | ❓               |  
| `minecraft:turtle`                 | ❓               |  
| `minecraft:vex`                    | ❓               |  
| `minecraft:villager`               | ❓               |  
| `minecraft:vindicator`             | ❓               |  
| `minecraft:wandering_trader`       | ❓               |  
| `minecraft:warden`                 | ❓               |  
| `minecraft:wind_charge`            | ✔️              |  
| `minecraft:witch`                  | ❓               |  
| `minecraft:wither`                 | ❓               |  
| `minecraft:wither_skeleton`        | ❓               |  
| `minecraft:wither_skull`           | ❓               |  
| `minecraft:wolf`                   | ✔️              |  
| `minecraft:zoglin`                 | ❓               |  
| `minecraft:zombie`                 | ❓               |  
| `minecraft:zombie_horse`           | ❓               |  
| `minecraft:zombie_villager`        | ❓               |  
| `minecraft:zombified_piglin`       | ❓               |  

`* = Entity Variation`  
`✔️ = Working`  
`❌ = Not Working`  
`❓ = Not Tested`  

#### Incompatible Vanilla Entities:  
- `minecraft:ender_dragon`

### 
Vanilla Minecraft uses both `_outer_layer` and `_overlay` as suffixes for the outer layer of mobs,
Perspective will use the same suffix when using textured entities as the vanilla counterpart,
but for new custom overlays, Perspective will use `_overlay`.


### Related Projects  
*These projects are either in early development or still in the planning stages.*  
- [ ] [Perspective (LegacyFabric)](https://github.com/MCLegoMan/Legacy-Perspective)  
    - This mod is planned to be a LegacyFabric port of Perspective. (early working builds containing minimal features)  
- [ ] [Spectacle](https://github.com/MCLegoMan/Spectacle)  
    - This mod is planned to be an addon to Perspective that adds Twitch Integration. (project not started yet)  