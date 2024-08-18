
## Changelog  
- Updated Config Version to `21`.  
### Overlays  
- Added `biome_overlay` boolean config option.  
  - Defaults to `false`.  
  - When enabled, the current biome's name will be added to the overlays.  
- Added `time_overlay` String config option.  
  - Defaults to `false`.  
  - Valid Options: `false`, `twelve_hour`, `twenty_four_hour`.  
  - When set to `twelve_hour` or `twenty_four_hour`, the current game time will be added to the overlays in the set format.  
### UI Background  
- Updated UI Background registration.
  - `new UIBackground()` will now return the fallback uiBackground.
  - `new UIBackground(...)` now has more options for registration, allowing developers to register ui backgrounds without having to add null to some options.  
  - Added `renderDarkening` option to `new UIBackground(...)`.  
- Added `none` UI Background.  
  - This will not render anything in menu backgrounds (except for the panorama).  
### Shaders  
- Updated `perspective:gaussian` shader.  
  - The shader was not blending correctly before.  