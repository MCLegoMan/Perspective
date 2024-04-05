![](https://mclegoman.com/images/a/a7/Perspective_Development_Logo.png)  
## Perspective 1.3.0-alpha.4 for 24w14a
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
      "red": {
        "enabled": true,
        "mushroom": {
          "identifier": "minecraft:white_candle"
        }
      },
      "brown": {
        "enabled": true,
        "mushroom": {
          "identifier": "minecraft:white_candle"
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
    },
    "enabled": true
  }
```
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
### Experimental  
- Added **Improved Shader Renderer** experiment.  
  - When enabled, Super Secret Settings will be rendered in the world renderer, the same place as fabulous graphics.   
    - This also makes the depth shaders be rendered under the player's hand.  
  - *If there are no issues found with this experiment, it will be added to 1.3.0.*  

## Development Build  
Please help us improve by submitting [bug reports](https://github.com/MCLegoMan/Perspective/issues) if you encounter any issues.  

### Want to support my work?  
If you'd like to donate, visit [BuyMeACoffee](https://www.buymeacoffee.com/mclegoman).  
Your support is appreciated, please be aware that donations are non-refundable.  