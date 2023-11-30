![](https://mclegoman.com/images/a/a7/Perspective_Development_Logo.png)

It's bug fixing season! This update fixes bugs related to zoom and depth based shaders.

### Perspective 1.2.0-beta.1 for 1.20.2.  
**Hosting Update:** *1.2.0-release.1 will be the final version to be released on Curseforge.*
More information on this change will be posted as we get closer to 1.2.0-release.1.  

*I am planning to backport to 1.20, 1.20.1 for this release cycle.*  

## Changelog  
- **Removed 'hide' related options for Super Secret Settings.**  
  - This was removed to help simplify registering shaders.  
    - The player still can toggle these using the config screen or their keybindings.  
- **Zoom HideHUD will now also hide the player's hands.**  
- **Zoom Level Scroll Wheel Adjustment has been reverted as it didn't work in spectator mode.**
- **Depth Shaders will now automatically disable screen mode.**  
  - Depth information is only available when the player is in-game. if the player restarted the game with the depth shader enabled on screen mode it would cause the screen to render blank.  
    - `disable_screen_mode` is still recommended to be set for depth shaders in case of use with older versions.  
- **Updated Hide Crosshair.**
  - This option has been changed to use a sting instead of a boolean.  
    - The valid options that can be used are: `false`, `dynamic`, and `true`.  
      - `true`/`false` use the same behaviour as previous versions.  
      - `dynamic` only shows the crosshair when the player is looking at an entity or block.  
- **Added Hide Players config option and dataloader.**  
    - Config Option: `hide_players`  
      - Hides all players expect for the user.  
    - Dataloader: `/assets/perspective/hide_players.json`  
      - Uses the same format as the other hide dataloaders.  

### Development Build  
Please help us improve by submitting [bug reports](https://github.com/MCLegoMan/Perspective/issues) if you encounter any issues.  

### Want to support my work?  
If you'd like to donate, visit [BuyMeACoffee](https://www.buymeacoffee.com/mclegoman).
Your support is appreciated, please be aware that donations are non-refundable.