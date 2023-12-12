![](https://mclegoman.com/images/7/70/Perspective_Logo.png)  

## Perspective 1.2.0 for 1.20.3/4
**Licence Update:**
Perspective is now licensed under [LGPL-v3](https://www.gnu.org/licenses/lgpl-3.0.en.html#license-text).  
**Hosting Update:**
This update is the final version to be uploaded to Curseforge.
More information about this change can be found [here](https://mclegoman.com/Perspective/Moving_Away_from_Curseforge).  

### Changelog  
- Added Update Checker.  
- Added Warning, Information, and Tutorial Toasts.  
  - Development and Downgrade warning screens have been replaced by warning toasts.
  - When Super Secret Settings have been enabled for the first time, the player will see the following two toasts:  
    - Super Secret Settings Tutorial Toast.  
    - Photosensitivity Warning Toast.  
  - When the April Fools' Prank is first enabled (per year), the player will see a toast explaining how to disable it.  
- Shaders will now only show their namespace when another shader shares the same name.  
- Depth Shaders will now automatically disable screen mode.  
  - It is still recommended to set `disable_screen_mode` to true for depth shaders.  
- Renamed `pig_overlay` to `pig_outer_layer` to match vanilla.  
  - Any resource packs that use this will need to be updated.  
- Fixed End Crystal Textured Entity.  
- Bee Textured Entity now has nectar, angry, and angry nectar textures.  
- Added Breeze and Wind Charge Textured Entities.  
- Added `hide_armor`, `hide_nametags` and `hide_players` dataloaders.  
  - This feature allows players to hide armour/nametags of specific players using resource packs.  
- Removed Ender Dragon Textured Entity.  
  - This entity didn't work, even after various attempts to fix it.  
    - This *may* get added back in a later update.
- Added Hide Block Outline.  
  - If this config option is enabled, the outline shown when targeting a block will not be rendered.  
- Added Hide Crosshair.  
  - This config option has three values, `false`, `dynamic`, and `true`.  
    - `false`: never hides the crosshair. (vanilla behaviour)  
    - `dynamic`: only hides the crosshair when you are not targeting a block or entity.  
    - `true`: always hides the crosshair.   
- Added Hide Players.  
  - If this config option is enabled, all other players will not be rendered.  
- Added Show Death Co-Ordinates.  
  - If this config option is enabled, your current co-ordinates will be shown on the death screen.  
- Removed Negative Zoom.  
  - If the player was in Creative Flight, *or* had the speed effect, the camera could sometimes flip upside down.  
- Added `minecraft:love` shader.
  - This shader was used in the [Love and Hugs](https://minecraft.wiki/w/Java_Edition_15w14a) april fools snapshot, and was not part of the original super secret settings.  
- Added Debug Overlay.  
  - This overlay displays debug information about various Perspective features.  
  - It also shows the current contents of the config.  
- Updated `Perspective: Default` Resource Pack.  
  - Added `sixteen_colors` shader.  
  - Revamped `outlined` shader.  
  - Added `silhouette` shader.  
  - Added `crt` shader.  
  - Added `rainbow` shader.  
  - Added `pixelated` shader.  
  - Added `mirror` shader.  
  - Added `Cheeze` Breeze Textured Entity.  
  - Added `Mossy` Skeleton Textured Entity.
  - Added `Tee` Bee Textured Entity.
  - Added `Strawberry` Fox Textured Entity.  

#### Experimental  
- Added Super Secret Settings Shader Selection Screen.  
- Added `perspective:foggy` shader.  

### Want to support my work?  
If you'd like to donate, visit [BuyMeACoffee](https://www.buymeacoffee.com/mclegoman).  
Your support is appreciated, please be aware that donations are non-refundable.  