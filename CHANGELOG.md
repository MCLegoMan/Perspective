# Perspective 1.1.0  
**This release has so many new features that I hope you all enjoy!**  


## Features  

- Added Screen Mode for Super Secret Settings  
    - Screen Mode overlays the shader on the entire game, including screens.  
    - This also fixes the shader being removed when changing perspective, on both Screen and Game mode.  
- Added Textured Entity  
    - Named Textured Entity allows users to name an entity and have a custom texture using resource packs.  
    - Random Textured Entity chooses a random texture from the loaded textured entities.  
- Added Customisable Default Config  
    - You can customise what Perspective considers "default" for config options using resource packs.  
    - You can also now reset the zoom level via the mouse wheel.  
- Added Panorama Screenshots  
    - You can take a panorama screenshot which automatically turns into a resource pack.  
    - Note: Make sure all chunks are loaded before taking the panorama.  
- Added April Fools' Prank  
    - Perspective automatically turns on the April Fools' Prank if anywhere in the world is April 1st.  
    - This can be turned off in the config. You can also force the prank to be run at any point in the year.  
- Added Pride Logo  
    - During pride month, Perspective's logo changes its colours to support Pride!  
    - In this version, only Pride and Trans logos can be randomly chosen from. More will be added in future versions.  
    - You can also support Pride all year long with the "Force Pride" config option.  
- Added Version Overlay  
    - Reimplements the Version Overlay from early versions of Minecraft.  


## Technical  

- Zoom Level is now stored as the value of the percentage in the config, instead of using the (100 - value) method.  
- Perspective's config is now accessed via PerspectiveConfigDataLoader, this is to allow for multiple config files.  
- Depth Shaders are now compatible with Perspective's Super Secret Settings. (Forked from Souper Secret Settings by Nettakrim)  
- Added Perspective Overlay which doesn't get hidden when HideHUD is active.  
- Vanilla doesn't account for namespace for shaders, in both the shader program and shader textures, Perspective now fixes that. (Contributor: Nettakrim)  

## Experimental Features  
**These features aren't ready to be fully released yet, however you can try them out using the Experimental config.**  
**Note: Experiments can be removed at any point.**  

- Toggle Armour  
    - When active, armour will not be rendered on players.  
    - This can also be set separately for specific super secret settings.  
- Toggle Nametags  
    - When active, nametags will not be rendered.  
    - This can also be set separately for specific super secret settings.  

_Both of these experiments are planned to have a customisable data/resource pack dataloader for specific players, however this feature is not in the mod yet._  


## F.A.Q  
- How do I add a Textured Entity, Shader, or Custom Default Config Values?  
    - Tutorials will be added to [Perspective Documentation](mclegoman.github.io/perspective/) soon.  
    - I'll try to add some basic information asap.  
- Why are my Custom Shaders/Textured Entities aren't showing?  
  - Check if you have enabled the Resource Pack with the Custom Shaders/Textured Entities.  
  - Additionally, make sure the config options are enabled.  
  - If all that fails, Create a [Bug Report](https://github.com/MCLegoMan/Perspective/issues).  