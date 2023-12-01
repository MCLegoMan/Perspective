![](https://mclegoman.com/images/a/a7/Perspective_Development_Logo.png)

Continuing the bug fixes!

### Perspective 1.2.0-beta.2 for 1.20.2.  
**Hosting Update:** *1.2.0-release.1 will be the final version to be released on Curseforge.*
More information on this change will be posted as we get closer to 1.2.0-release.1.  

*I am planning to backport to 1.20, 1.20.1 for this release cycle.*  

## Changelog  
- **Updated Config Version to `13`.**  
  - Added config option `hide_show_message`.  
    - This will toggle whether the overlay message is shown when toggling/cycling hide options using keybindings.  
  - The config version was updated to `12` in `1.2.0-beta.1`.  
- **Depth Shaders will now render properly when an entity with glowing is rendered.**  
  - If you experience any issues relating to this, try cycling your graphics options and report the issue.  
- **Updated Take Panorama Screenshot**  
  - The name format has been updated to match vanilla screenshots.  
  - Perspective Super Secret Settings will now be rendered on the panorama screenshots.  
- **Resetting Zoom will now update `zoomUpdated` instead of directly saving.**  
  - `zoomUpdated` will save the config after the player stops zooming.  
- **Textured Entity Bees now have all variants of their textures.**
  - These textures are stored in the same location as previous versions.
  ```
    - assets/
      - minecraft/
        - textures/
          - textured_entity/
            - bee/
              - <name>.png
              - <name>_nectar.png
              - <name>_angry.png
              - <name>_angry_nectar.png
  ```
- **Toggle Zoom has been updated to invert the output of Hold Zoom.**  
- **Updated Perspective: Default Resource Pack**  
  - Added `Tee` Textured Entity Bee.  
  - Updated `perspective:silhouette` shader to be more visible.  

### Development Build  
Please help us improve by submitting [bug reports](https://github.com/MCLegoMan/Perspective/issues) if you encounter any issues.  

### Want to support my work?  
If you'd like to donate, visit [BuyMeACoffee](https://www.buymeacoffee.com/mclegoman).
Your support is appreciated, please be aware that donations are non-refundable.