
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
