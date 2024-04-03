![](https://mclegoman.com/images/a/a7/Perspective_Development_Logo.png)  
## Perspective 1.3.0-alpha.4 for 24w06a


### Changelog  
- **Updated Config Version to `17`.**  
  - `zoom_type` is now stored as namespace:type.  
- **Addon mods can now make custom zoom multiplier types.**  
  - New zoom multiplier types can be registered using `Zoom.addZoomType(String name, boolean shouldLimitFOV, Runnable setZoomTypeMultiplierRunnable);` on client initialisation.  
    - `name` is used for both the config value, and for translation.  
      - `gui.perspective.config.zoom.type.<name>` is the translation key.  
    - `shouldLimitFOV` limits the fov from `0.01` to `179.99` when set to true.  
      - This may change in a future version.  
    - `setZoomTypeMultiplierRunnable` should be run from a class that extends `ZoomTypeMultiplier`.  
      - This function can use `setMultiplier()` to set the multiplier of the zoom.  
        - You can use `Zoom.getZoomLevel()` to get the current zoom percentage.  
        - This example shows how you can make the zoom multiplier type linear: `setMultiplier(1 - ((float) Zoom.getZoomLevel() / 100))`  
- **Invalid config values are reset after client initialization.**  

**Experimental:**  
- **Added Shader Hand Renderer experiment.**  
  - When enabled, the player's hand will render over the shader.
    - This will be updated in future to render the hand in the world to allow depth shaders to use hand depth.  

### Development Build  
Please help us improve by submitting [bug reports](https://github.com/MCLegoMan/Perspective/issues) if you encounter any issues.  

### Want to support my work?  
If you'd like to donate, visit [BuyMeACoffee](https://www.buymeacoffee.com/mclegoman).  
Your support is appreciated, please be aware that donations are non-refundable.  