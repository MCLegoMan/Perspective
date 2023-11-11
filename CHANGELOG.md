![](https://mclegoman.com/images/a/a7/Perspective_Development_Logo.png)

What's with the _breeze_ today? We've gone and trapped 17 of these cunning Breeze into our dungeon, allowing us to harness their wind, so we can inject it right into Perspective's code!  

### Perspective 1.2.0-alpha.5 for 1.20.2 and 1.20.3 snapshots.  
**Hosting Update:** *1.2.0-release.1 will be the final version to be released on Curseforge. [Learn more here](https://mclegoman.com/Perspective/Moving_Away_from_Curseforge).*  
## Changelog  
- **Updated Config Version to `11`.**  
  - Added `show_death_coordinates` config option.  
    - This option toggles the visibility of your coordinates on the death screen.
  - Fixed Textured Entity config options.
    - There was an inconsistency of config option naming that prevented the feature from working as intended.
- **Updated Textured Entity.**  
  - Added `minecraft:breeze`.
    - Main Texture Location: `perspective:textures/textured_entity/breeze/<name>.png`  
    - Wind Texture Location: `perspective:textures/textured_entity/breeze/<name>_wind.png`
    - This entity will only be enabled for Minecraft 22w45a and later.
      - Note: You will need to have the `update_1_21` experiment enabled to spawn the Breeze.
  - Added `minecraft:wind_charge`.
    - Texture Location: `perspective:textures/textured_entity/wind_charge/<name>.png`
      - Note: You will need to have the `update_1_21` experiment enabled to spawn the Wind Charge.
  - Updated Pig Overlay/Outer Layer.
    - Added `minecraft:pig#outer` model layer.
    - Replaced `PIG_SADDLE` model with `PIG_OUTER` model.
      - This makes the `outer_layer` get rendered under the saddle.
    - Renamed `pig_overlay.png` to `pig_outer_layer.png`.
      - Resource Packs will need to be updated to display the outer layer.
      - This naming scheme also applies to Textured Entity.
  - Removed `minecraft:ender_dragon`.
    - This entity did not work in named or random mode, even after attempting to fix.
- **Updated Vanilla Super Secret Settings Shaders.**
  - Added `minecraft:love` from the `15w14a` April Fools' snapshot.
    - This shader was only available in the above-mentioned snapshot.
  - Updated `minecraft:creeper` to not use Screen Mode.
    - Due to the difficulty to read the text on some resolutions.
- **Updated `Perspective: Default` Resource Pack.**
  - Turned down the strength of the `perspective:outlined` shader outline.
  - Added `Cheeze` Breeze Textured Entity.
  - Added `Mossy` Skeleton Textured Entity.

### Development Build  
Please help us improve by submitting [bug reports](https://github.com/MCLegoMan/Perspective/issues) if you encounter any issues.  

### Want to support my work?  
If you'd like to donate, visit [BuyMeACoffee](https://www.buymeacoffee.com/mclegoman).
Your support is appreciated, please be aware that donations are non-refundable.