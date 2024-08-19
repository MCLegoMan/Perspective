This alpha release revamps overlays, adds more ui background customizability, improves textured entity, and adds a new Halloween event!  

**Important: If you use the Hide Nametags option, this release fixes a bug that would set Hide Players to true alongside Hide Nametags on startup. Reset your config or adjust Hide Players if desired.**  

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
- Merged UI Background and Title Screen Background.  
  - **Developer Note: Whilst this does remove the option to only have a dirt screen, this functionality can be re-added through use of third-party mods.**  
- Added `none` UI Background.  
  - This will not render anything in menu backgrounds (except for the panorama).  
- Added `classic` UI Background.  
  - This ui background is almost identical to `legacy`, but also changes the title screen to have a dirt background.  
- Added `UIBackground.registerUIBackground(UIBackgroundData data)` function.
  - This function was added to allow third-party mods to add UI Backgrounds without having to use mixins.  
- Replaced `new UIBackgroundData(...);` with `new UIBackgroundData.Builder(Identifier identifier).build();`.  
  - ***NOTE: This feature is for third-party mod developers.***  
  - This new builder simplifies the process of making new UI Backgrounds.    
  - This builder contains the following functions:  
    - `.renderWorld(UIBackground.Runnable renderWorld)`  
      - This optional runnable will be executed when a menu is opened if in a world.
    - `.renderMenu(UIBackground.Runnable renderMenu)`
      - This optional runnable will be executed when a menu is opened if not in a world.  
    - `.renderTitleScreen(UIBackground.Runnable renderTitleScreen)`  
      - This optional runnable will be executed when on the title screen.  
    - `.renderTitleScreenPanorama(boolean renderPanorama)`  
      - If set to false, the panorama will not be rendered on the title screen.  
      - Defaults to true.  
    - `.renderPanorama(boolean renderPanorama)`  
      - If set to false, the panorama will not be rendered on menu screens. (NOTE: This doesn't include the Title Screen.)
      - Defaults to true.
    - `.renderShader(boolean renderShader)`
      - If set to false, the shaderId shader will not be rendered on menu screens.  
      - Defaults to true.
    - `.shaderId(Identifier shaderId)`  
      - This sets the shader that replaces the menu blur shader.  
      - Defaults to null.
        - When set to null, the game uses the default blur shader.  
    - `.renderDarkening(boolean renderDarkening)`
      - If set to false, menu darkening is disabled.  
      - Defaults to true.  
  - Make sure to finish with `.build();`.  
  - Don't forget to add it to the registry: `UIBackground.registerUIBackground(new UIBackgroundData.Builder("example").build());`  
  - You should also name your ui background in the translation files with this key: `"gui.perspective.config.ui_background.type.example"`  
### Shaders  
- Updated `perspective:gaussian` shader.  
  - The shader was not blending correctly before.  
### Entities  
- Added capes to the following entities: `minecraft:armor_stand`, `minecraft:zombie`, `minecraft:drowned`, `minecraft:husk`, `minecraft:zombie_villager`, `minecraft:skeleton`, `minecraft:wither_skeleton`, `minecraft:bogged`, `minecraft:stray`, `minecraft:witch`, `minecraft:villager`, `minecraft:giant`, `minecraft:pillager`, `minecraft:evoker`, `minecraft:illusioner`, `minecraft:vindicator`, `minecraft:wandering_trader`, `minecraft:cow`, `minecraft:mooshroom`, `minecraft:enderman`, `minecraft:chicken`.  
  - By default, their texture is blank.  
  - This can also be set via the textured entity system.  
  - This will be rendered separately from elytras.  
- Entities named `MCLegoMan`, `MCLegoBlock`, `DanielTayden`, or `DannyTaylor` will be flipped upside down.  
  - This has been linked to the "contributors" system, so any contributor that is upside down's id will flip mobs.  
- #### Textured Entity  
  - Textured Entities will now get their type from their entity.  
  - This means the entity type provided in the dataloader must match that of what the game loads the entity as. (You can check these using the `/summon` command.)  
  - The default texture location has changed to `namespace:textures/textured_entity/entity_namespace/entity_type/texture.png`  
    - Whilst this will break backwards compatibility, this allows us to replace textures in different namespaces.  
### Events  
- Added Halloween Event.  
  - Renders a witch hat on all players if it is Halloween or Force Halloween is enabled.  
    - Requires Allow Halloween to be enabled.  
### Resource Packs  
- #### Perspective: Default
  - Added `Dummy` Armor Stand Textured Entity.  
  - Added `Cloak` Pillager, Evoker, Illusioner, Vindicator, and Witch Textured Entities.  
### Bug Fixes  
- Hide Players config option would be set to true on startup if Hide Nametags was true.  
  - If you experienced this bug, make sure to reset your config (or turn Hide Players off) if desired.  