
## Changelog  
- Updated Config Version to `22`.  
### Overlays  
- Added Positional Data Obfuscation.  
  - Other mods can tell Perspective to obfuscate co-ords (in the Position Overlay, and the Death Screen Co-Ordinates) using `com.mclegoman.perspective.client.util.Position.register("modId");`.  
  - When one or more mods have done this, Perspective rendered co-ords will be replaced with "?".  
- Added `cps_overlay` boolean config option.  
  - Defaults to `false`.  
  - When enabled, the cps will be added to the overlays. (using the format Left:Middle:Right)  
### Hold Perspective  
- Added `hold_perspective_back_multiplier` double config option.  
  - Defaults to `1.0`.  
  - Set how far away the camera is when using Hold Perspective: Back.  
- Added `hold_perspective_front_multiplier` double config option.  
  - Defaults to `1.0`.  
  - Set how far away the camera is when using Hold Perspective: Front.  
### Zoom  
- Added `zoom_smooth_speed_out` double config option.  
  - Defaults to `1.0`.  
  - Set how fast the smooth zoom transition is when zooming out.  
- Added `zoom_smooth_speed_in` double config option.  
  - Defaults to `1.0`.  
  - Set how fast the smooth zoom transition is when zooming in.  
### Bug Fixes  
- Fixed compatibility with [Remove Reloading Screen](https://modrinth.com/mod/rrls).  
  - If the camera entity doesn't exist, we shouldn't be zooming.  

### Experimental   
#### Ambience  
- Added `ambience` boolean experimental config option.  
  - When enabled, ambience features made for Perspective 1.4 will be enabled.  
  - Defaults to `false`.  
- Added `ripple_density` int config option.  
  - Requires `ambience` experimental config option to be set to `true`.    
  - Defaults to `0`.  
- Added `falling_leaves` int config option.  
  - Requires `ambience` experimental config option to be set to `true`.  
  - Defaults to `0`.  