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
  - Added config option `debug`.
    - This option bypasses the panorama incompatibility list.  
  - The config version was updated to `12` in `1.2.0-beta.1`.  

- **Depth Shaders will now render properly when an entity with glowing is rendered.**  
  - If you experience any issues relating to this, try cycling your graphics options and report the issue.

- **Updated Take Panorama Screenshot.**  
  - The name format has been updated to match vanilla screenshots.  
  - Perspective Super Secret Settings will now be rendered on the panorama screenshots.  
  - Updated Panorama Incompatibility List.  
    - Sodium has been fixed and removed from the list.  
    - Iris and Canvas Renderer have been added to the incompatibility list.  
    - Fabulous Graphics Option has been added to the incompatibility list.  
    - Config Option `debug` will now bypass panorama incompatibilities.  
  - Set Perspective to First Person whilst rendering Panorama.  

- **Resetting Zoom will now update `zoomUpdated` instead of directly saving.**  
  - `zoomUpdated` will save the config after the player stops zooming.  

- **Textured Entity Bees now have all variants of their textures.**
  - These assets are stored in the same location as previous versions.
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
      - perspective/
        - textured_entity/
          - <bee_name>.json
  ```
  `*<bee_name>.json can be named anything, but it is recommended to follow the <bee_name> formatting in case of name conflicts with other resource packs.*`

- **Toggle Zoom has been updated to invert the output of Hold Zoom.**  

- **Updated Perspective: Default Resource Pack.**  
  - Added `Tee` Textured Entity Bee.  
  - Updated `perspective:silhouette` shader to be more visible.  

## Experimental  
Experiments are potential new features. You may experience bugs and/or issues.  
- Added new experiment:  
  - **Super Secret Settings Shader Selection Screen.**  
    - This experiment replaces the functionality of the shader button on the shader screen.  
      - Instead of cycling shaders, it will now open a new Shader Selection Screen.  

- Added `Perspective: Experimental` resource pack.  
  - This resource pack will contain experimental resources such as shaders and textured entities that are expected to be in `Perspective: Default` in the next version.  
  - Added `perspective:foggy` shader.

Let us know your feedback on these experimental features on [GitHub](https://github.com/MCLegoMan/Perspective/issues) or [Discord](https://discord.gg/vjbvjpFFPm) [`#modding-help\Perspective (Feedback)`](https://discord.com/channels/814560286664949811/1181316009592881182).  

### Development Build  
Please help us improve by submitting [bug reports](https://github.com/MCLegoMan/Perspective/issues) if you encounter any issues.  

### Want to support my work?  
If you'd like to donate, visit [BuyMeACoffee](https://www.buymeacoffee.com/mclegoman).
Your support is appreciated, please be aware that donations are non-refundable.