![](https://mclegoman.com/images/a/a7/Perspective_Development_Logo.png)  
## Perspective 1.3.0-alpha.5 for 1.20.5-rc1  
I hope everyone enjoyed the chaos that was April Fools', we're back at it with another alpha!  

For those of you who tried out my April Fools' mod [Mysterious Update](https://modrinth.com/mod/mclmaf2024), you'll be happy to see that the Mooblooms have made it into Perspective!  

## Changelog  
### Technical Changes  
- Perspective now **requires Java 21 or higher**.  
  - Minecraft Snapshot 24w14a changed the included Java distribution to the Microsoft build of OpenJDK 21.0.2.  
    - Perspective is now also built using the Microsoft build of OpenJDK 21.0.2.  
### Config Version 17  
- `zoom_type` is now stored as namespace:key string.  
  - The default value is set to `perspective:logarithmic`.
- Renamed `dirt_title_screen` to `title_screen`.
  - Updated to use string instead of boolean.
    - The valid values for this option are: `dirt`, `default`.
- Added `ui_background`.
  - This sets the background blur to be `default`, `gaussian`, or `legacy`
    - `gaussian` replaces the blur shader with a nicer looking gaussian blur.
    - `legacy` removes the blur shader and renders a dirt/darkened background.
      - When in a world, the background is darkened.
      - When not in a world, the background uses the `ui_background_texture` texture.
- Added `ui_background_texture`.
  - This sets the texture of Dirt Title Screen and Legacy UI Background.
  - This config option is stored as a namespace:key string
    - The key doesn't require "textures/" at the beginning or ".png" at the end as they are automatically added, but you can still add them if you want.
    - The default value is set to `minecraft:blocks/dirt`.
### Feature Changes  
#### Entity  
- Mooshrooms now have an overlay texture.
  - The texture is located at `minecraft:textures/entity/mooshroom/mooshroom_overlay.png`.  
#### Textured Entity  
- Added `entity_specific` to Textured Entity dataloader.  
  - This allows for textured entities to have data specific to some entities.  
- Mooshroom textured entities will now require an overlay texture, and a texture for each variant.  
  - The overlay texture should be located at `minecraft:textures/textured_entity/mooshroom/<name>_overlay.png`.  
  - The variant texture should be located at `minecraft:textures/textured_entity/mooshroom/<variant>_<name>.png`.  
- Mooshroom textured entities now can optionally change the mushroom blockstate, and optionally set textured entity enabled for a specific variant(s) of mooshroom.  
  - **Here's an example:**  
```
  {
    "entity": "minecraft:mooshroom",
    "name": "Herobrine",
    "entity_specific": {
      "variants": {
        "red": {
          "enabled": true,
          "mushroom": {
            "identifier": "minecraft:red_candle"
          }
        },
        "brown": {
          "enabled": true,
          "mushroom": {
            "identifier": "minecraft:brown_candle"
          }
        }
      }
    },
    "enabled": true
  }
```
- Wolf textured entities will now require textures for each variant.  
  - The variant textures should be located at:  
    - `<variant_namespace>:textures/textured_entity/wolf/<name>_<variant>_tame.png`.  
    - `<variant_namespace>:textures/textured_entity/wolf/<name>_<variant>_angry.png`.  
    - `<variant_namespace>:textures/textured_entity/wolf/<name>_<variant>.png`.  
- Wolf textured entities now can optionally set textured entity enabled for a specific variant(s) of wolf.  
  - **Here's an example:**  
```
  {
    "entity": "minecraft:wolf",
    "name": "Herobrine",
    "entity_specific": {
      "variants": {
        "minecraft:ashen": {
          "enabled": true
        },
        "minecraft:black": {
          "enabled": true
        },
        "minecraft:chestnut": {
          "enabled": true
        },
        "minecraft:pale": {
          "enabled": true
        },
        "minecraft:rusty": {
          "enabled": true
        },
        "minecraft:snowy": {
          "enabled": true
        },
        "minecraft:spotted": {
          "enabled": true
        },
        "minecraft:striped": {
          "enabled": true
        },
        "minecraft:woods": {
          "enabled": true
        }
      }
    },
    "enabled": true
  }
```
- Frog textured entities will now require textures for each variant.
  - The variant textures should be located at:
    - `<variant_namespace>:textures/textured_entity/frog/<name>_<variant>.png`.  
- Frog textured entities now can optionally set textured entity enabled for a specific variant(s) of frog.
  - **Here's an example:**
```
  {
    "entity": "minecraft:frog",
    "name": "Herobrine",
    "entity_specific": {
      "variants": {
        "minecraft:temperate": {
          "enabled": true
        },
        "minecraft:warm": {
          "enabled": true
        },
        "minecraft:cold": {
          "enabled": true
        }
      }
    },
    "enabled": true
  }
```
- Cat textured entities will now require textures for each variant.
  - The variant textures should be located at:
    - `<variant_namespace>:textures/textured_entity/cat/<name>_<variant>.png`.
- Cat textured entities now can optionally set textured entity enabled for a specific variant(s) of cat.
  - **Here's an example:**
```
  {
    "entity": "minecraft:cat",
    "name": "Herobrine",
    "entity_specific": {
      "variants": {
        "minecraft:tabby": {
          "enabled": true
        },
        "minecraft:black": {
          "enabled": true
        },
        "minecraft:red": {
          "enabled": true
        },
        "minecraft:siamese": {
          "enabled": true
        },
        "minecraft:british_shorthair": {
          "enabled": true
        },
        "minecraft:calico": {
          "enabled": true
        },
        "minecraft:persian": {
          "enabled": true
        },
        "minecraft:ragdoll": {
          "enabled": true
        },
        "minecraft:white": {
          "enabled": true
        },
        "minecraft:jellie": {
          "enabled": true
        },
        "minecraft:all_black": {
          "enabled": true
        }
      }
    },
    "enabled": true
  }
```
- Axolotl textured entities will now require textures for each variant.
  - The variant textures should be located at:
    - `<variant_namespace>:textures/textured_entity/axolotl/<name>_<variant>.png`.
- Axolotl textured entities now can optionally set textured entity enabled for a specific variant(s) of axolotl.
  - **Here's an example:**
```
  {
    "entity": "minecraft:axolotl",
    "name": "Herobrine",
    "entity_specific": {
      "variants": {
        "lucy": {
          "enabled": true
        },
        "wild": {
          "enabled": true
        },
        "gold": {
          "enabled": true
        },
        "cyan": {
          "enabled": true
        },
        "blue": {
          "enabled": true
        }
      }
    },
    "enabled": true
  }
```
- Fox textured entities will now require textures for each variant.
  - The variant textures should be located at:
    - `<variant_namespace>:textures/textured_entity/fox/<name>_<variant>.png`.
- Fox textured entities now can optionally set textured entity enabled for a specific variant(s) of axolotl.
  - **Here's an example:**
```
  {
    "entity": "minecraft:fox",
    "name": "Herobrine",
    "entity_specific": {
      "variants": {
        "red": {
          "enabled": true
        },
        "snow": {
          "enabled": true
        }
      }
    },
    "enabled": true
  }
```
#### Super Secret Settings  
- Added **Entity Linked** Shaders    
  - You can set shaders to be linked to entities using the `entity_links` array in the shader dataloader.
    - This currently requires the **Improved Shader Renderer** experiment to be enabled.  
    - Here's an example:
##### Perspective Layout:  
```
  {
    "namespace": "perspective",
    "shader": "silhouette",
    "disable_screen_mode": true,
    "translatable": true,
    "disable_soup": false,
    "entity_links": [
      "entity.minecraft.warden"
    ],
    "custom": {},
    "enabled": true
  }
```
##### Soup Layout:
```
  {
      "namespaces": [
          {
              "replace": false,
              "namespace": "perspective",
              "translatable": true,
              "shaders": [
                  "silhouette"
              ],
              "disable_screen_mode": [
                  "silhouette"
              ],
              "disable_soup": [],
              "custom": {
                  "silhouette": {}
              }
          }
      ],
      "entity_links": {
          "entity.minecraft.warden": "perspective:silhouette"
      }
  }
```
*If no namespace is given, the first instance of a shader with that name will be used.*
#### Shaders  
- Added **Dynamic Uniforms**.
  - **Please Note:** These will be moved to a separate library mod (**Luminance**) in a future update, but will be included with Perspective.  
    - `lu_viewDistance`
      - Float value.
      - Outputs your render distance.
    - `lu_eyePosition`
      - Vec3 value.
      - Outputs your camera eye position.
      - Defaults to vec3(0, 0, 0).
    - `lu_pitch`
      - Float value ranging from 0 to 360.
      - Outputs your player pitch.
      - Defaults to 0.
    - `lu_yaw`
      - Float value ranging from 0 to 360.
      - Outputs your player yaw.
      - Defaults to 0.
    - `lu_currentHealth`
      - Float value.
      - Outputs your current health.
      - Defaults to 20.
    - `lu_maxHealth`
      - Float value.
      - Outputs your maximum health.
      - Defaults to 20.
    - `lu_currentAir`
      - Float value.
      - Outputs your current air.
      - Defaults to 10.
    - `lu_maxAir`
      - Float value.
      - Outputs your maximum air.
      - Defaults to 10.
  - `perspective_zoomMultiplier`
    - Float value that ranges from 0.0 to 1.0.
    - Outputs Perspective's zoom multiplier.
- Added **Render Phases**.
  - This should match the behaviour of the vanilla fabulous shader.
  - `translucent_target`
  - `item_entity_target`
  - `particles_target`
  - `weather_target`
  - `clouds_target`
#### Keybindings  
- The default values for **Hold Perspective Front** and **Hold Zoom** have been updated.
  - These changes were made so these keybindings wouldn't be conflicting by default.
    - **Hold Perspective Front** will now default to **V**.  
    - **Hold Zoom** will now default to **R**.
### Resource Packs  
#### Perspective: Default  
- Added **depth-based dither** shader.  
  - A dithering effect will render in the distance using depth.  
- Added **light-based dither** shader.  
  - A dithering effect will render over the lightest parts of the screen.  
- Added **Moobloom** mooshroom textured entity.  
  - Red Mooshrooms turn into regular Mooblooms.  
  - Brown Mooshrooms turn into Ancient Mooblooms.  
#### Super Secret Settings  
- Moved shaders that were used in previous versions of minecraft to this resource pack.  
  - This resource pack will be moved to **Luminance** in the near future.  
### Experimental  
- Added **Improved Shader Renderer** experiment.  
  - When enabled, Super Secret Settings will be rendered in the world renderer, the same place as fabulous graphics.   
    - This also makes the depth shaders be rendered under the player's hand.  
  - This experiment also enables Entity Linked shaders.
    - Entity Linked shaders render when the player is spectating the linked entity.
  - *If there are no issues found with this experiment, it will be added to 1.3.0.*  

## Development Build  
Please help us improve by submitting [bug reports](https://github.com/MCLegoMan/Perspective/issues) if you encounter any issues.  

### Want to support my work?  
If you'd like to donate, visit [BuyMeACoffee](https://www.buymeacoffee.com/mclegoman).  
Your support is appreciated, please be aware that donations are non-refundable.  