# Perspective 1.2.0-alpha.3 for 1.20.2
Did somebody say **TOAST**? No? Well, this is awkward... But fear not! The latest update is here to butter you up with some toasty goodness. It has spiced things up by changing the way Perspective serves information—it now arrives piping hot through Minecraft's toast system. And don't worry, not all the toast is stale; there's some fresh and exciting content too! Get ready for a Perspective like no other!    

## Changelog:  
- **License Change: Transitioned from `CC-BY 4.0` to `LGPL-3.0-or-later`.**  
  - `LGPL-3.0` allows for more flexibility in how the project can be used, while still ensuring that any modifications to the code are shared back with the community.  
  - Please note that versions older than `1.2.0-alpha.3` of Perspective continue to be licensed under `CC-BY-4.0`.  
- **Updated to Config Version `8`.**  
  - Removed `show_development_warning` config option.  
  - Added `detect_update_channel` config option with values `none`, `alpha`, `beta`, and `release`.  
  - Added `tutorials` boolean config option.
    - This option toggles Perspective's tutorial toasts.
  - Added Tutorial Config.  
  - Separated `getConfig()` and `setConfig()` into `getConfig()`/`setConfig()`, `getExperimentalConfig()`/`setExperimentalConfig()`, and `getTutorialConfig()`/`setTutorialConfig()`.  
- **Added Tutorials.**  
    - Added Super Secret Settings tutorial.  
      - This tutorial is triggered when super secret settings has been enabled for the first time.
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
- **Updated Shaders**  
  - The Shader Registry now stores both the `NAMESPACE` and `SHADER_NAME`.  
    - This allows Perspective to use only the shader name, unless there is two shaders using the same name.  
- **Updated Panoramas.**  
  - Panoramas now use the toast system for both success and failures.  
    - Successful panoramas will still get a message in chat with a link to open in File Explorer.  
- **Updated Resource Packs.**  
  - `Perspective: Default` now uses the new `supported_formats` feature.  
  - Added `sixteen_colors` shader to `Perspective: Default`.  

### Development Build  
Please help us improve by submitting [bug reports](https://github.com/MCLegoMan/Perspective/issues) if you encounter any issues.  