![](https://mclegoman.com/images/a/a7/Perspective_Development_Logo.png)  
## Perspective 1.3.0-alpha.4 for 24w14a


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
### Resource Packs  
#### Perspective: Default  
- Added **depth-based dither** shader.  
- Added **light-based dither** shader.  
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