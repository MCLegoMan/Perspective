![](https://mclegoman.com/images/a/a7/Perspective_Development_Logo.png)  
## Perspective 1.3.0-alpha.3 for 23w03a/b
This update adds support for logarithmic zoom, and scaling when zoomed for mouse movement, view bobbing, and damage tilt scaling.  

*Please Note: Perspective will remember your config options when updating, so if you want to check out some of these new features, you may need to (re)set these using the config. (You can access this via Mod Menu or in-game using the Open Config keybinding - by default this is set to [END].)*

### Changelog  
- **Config Version updated to `16`.**  
  - Renamed `zoom_camera_mode` config option to `zoom_scale_mode`.  
    - Removed `spyglass` config value.  
    - Added `scaled` config value.  
      - This option scales mouse movement, view bobbing, and damage tilt when zooming.  
  - Added `zoom_type` config option with `logarithmic` and `linear` config values.  
    - The default value is set to `logarithmic`.  
    - To use the previous functionality, set this to `linear`.  
  - Updated `zoom_level` default value to `40`.  
  - Updated `zoom_scale_mode` default value to `scaled`.  
  - Resetting the config to default will now enable shaders if the default is set to true.
- **Updated Zoom.**  
  - Added `logarithmic` zoom scaling.  
  - Mouse Movement, Damage Tilt, and View Bobbing are scaled when Zoom Mode is set to `Scaled`.  
  - Mouse Scroll Sensitivity is now properly accounted for.  
- **Super Secret Settings Config Screen now has a dedicated shader list button.**  
  - The shader button has been reverted to cycle shaders.  
- **Pressing [ESC] on the Config Screen will now go back a page if not on page 1.**    
- **[Souper Secret Settings](https://modrinth.com/mod/souper-secret-settings)' shader dataloader layout will now load from any namespace.**  
  - For example, shaders listed in `/assets/your_namespace/shaders.json` will now also get registered, not just `/assets/souper_secret_settings/shaders.json`.  
    - This matches the behaviour of Perspective's shader dataloader layout.  
  - This update also adds support for "replace".  
    - When inside a namespace, all shaders in that namespace currently registered will be removed.  
    - When outside the namespacelist, all shaders no matter the namespace will be removed.  
- **Added `Perspective: Developer Config` Resource Pack.**  
  - This resource pack changes the default config options to MCLegoMan's choices and is disabled by default.  

### Development Build  
Please help us improve by submitting [bug reports](https://github.com/MCLegoMan/Perspective/issues) if you encounter any issues.  

### Want to support my work?  
If you'd like to donate, visit [BuyMeACoffee](https://www.buymeacoffee.com/mclegoman).  
Your support is appreciated, please be aware that donations are non-refundable.  