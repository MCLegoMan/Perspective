![](https://mclegoman.com/images/a/a7/Perspective_Development_Logo.png)  
## Perspective 1.3.0-alpha.3 for 23w51a/b
This update focuses on Zoom

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
- **Updated Zoom.**  
  - Added `logarithmic` zoom scaling.  
  - Mouse Movement, Damage Tilt, and View Bobbing are scaled when Zoom Mode is set to `Scaled`.  
  - Mouse Scroll Sensitivity is now properly accounted for.  
- **Super Secret Settings Config Screen now has a dedicated shader list button.**  
  - The shader button has been reverted to cycle shaders.  
- Pressing [ESC] on the Config Screen will now go back a page if not on page 1.  

### Development Build  
Please help us improve by submitting [bug reports](https://github.com/MCLegoMan/Perspective/issues) if you encounter any issues.  

### Want to support my work?  
If you'd like to donate, visit [BuyMeACoffee](https://www.buymeacoffee.com/mclegoman).  
Your support is appreciated, please be aware that donations are non-refundable.  