![](https://mclegoman.com/images/a/a7/Perspective_Development_Logo.png)  

## Perspective 1.2.1 Release Candidate 1 for 1.20.3/4  
### Changelog  
- Updated Config Version to `14`.  
  - Removed `super_secret_settings` int config option, added `super_secret_settings_shader` string option.  
    - Resource Packs changing the default value for this will need to be updated.  
  - Added `position_overlay` boolean config option.  
- **Added Position Overlay.**  
- **Changed uses of `Math.max(Math.min(value, min), max);` to `MathHelper.clamp(value, min, max);`.**  
- **Fixed `Hide Player` keybinding category.**  
- **Added Horse Markings texture entity support.**  
- **Updated Options Screen Super Secret Settings Button.**  
  - This button will now only show when the player is in-game.  
  - This button will now cycle shaders.   
- **The shader will remain the same after reloading resource packs.**  

### Development Build  
Please help us improve by submitting [bug reports](https://github.com/MCLegoMan/Perspective/issues) if you encounter any issues.

### Want to support my work?  
If you'd like to donate, visit [BuyMeACoffee](https://www.buymeacoffee.com/mclegoman).  
Your support is appreciated, please be aware that donations are non-refundable.  