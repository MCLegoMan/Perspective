# Perspective 1.2.0-alpha.3 for 1.20.2

## Changelog:  
- **License Change: Transitioned from `CC-BY 4.0` to `LGPL-3.0`.**  
  - `LGPL-3.0` allows for more flexibility in how the project can be used, while still ensuring that any modifications to the code are shared back with the community.  
  - Please note that versions older than `1.2.0-alpha.3` of Perspective continue to be licensed under `CC-BY-4.0`.
- **Updated to Config Version `8`.**
  - Removed `show_development_warning` config option.  
  - Added `detect_update_channel` config option with values `none`, `alpha`, `beta`, and `release`.  
- **Updated Internal Versioning.**  
  - Added `PerspectiveVersion`.  
    - Perspective now stores its data at `PerspectiveData.PERSPECTIVE_VERSION`.  
    - This replaces the versioning data from `PerspectiveData`.  
    - This will break mods which depend on Perspective's internal versioning.  
- **Added Update Checker.**
  - A toast will appear on startup if a newer version of Perspective is available.  
  - This notice will also appear on the config screen.
- **Added Warning Toasts.**  
  - Updated `Development Warning` and `Downgrade Warning` to use Toasts.  
  - Added `Keybind Conflict Warning`  
- **Updated Language Assets.**  
  - Updated the `Development Warning` and `Downgrade Warning` messages to be easier to understand.  
- **Updated Resource Packs.**  
  - `Perspective: Default` now uses the new `supported_formats` feature.  

### Development Build  
Please help us improve by submitting [bug reports](https://github.com/MCLegoMan/Perspective/issues) if you encounter any issues.  