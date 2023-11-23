![](https://mclegoman.com/images/a/a7/Perspective_Development_Logo.png)

It's bug fixing season! This update fixes bugs related to zoom and depth based shaders.

### Perspective 1.2.0-beta.1 for 1.20.2 and 1.20.3.  
**Hosting Update:** *1.2.0-release.1 will be the final version to be released on Curseforge.*
More information on this change will be posted as we get closer to 1.2.0-release.1.  

## Changelog  
- **Removed 'hide' related options for Super Secret Settings.**  
  - This was removed to help simplify registering shaders.  
    - The player still can toggle these using the config screen or their keybindings.  
- **Reverted scroll wheel zoom changes.**  
  - This fixes spectators not being able to zoom in and out.  
- **Depth Shaders will now automatically disable screen mode.**  
  - Depth information is only available when the player is in-game. if the player restarted the game with the depth shader enabled on screen mode it would cause the screen to render blank.  
    - `disable_screen_mode` is still recommended to be set for depth shaders in case of use with older versions.  

**Experimental Changelog**  
- Added Hide Players Experiment.  
  - This includes two config options and a resource pack dataloader.  
    - Experimental Config Option: `allow_hide_players`  
      - Enables the experiment, allows the dataloader and the `hide_players` options to work.  
    - Experimental Config Option: `hide_players`  
      - Hides all players expect for the user. Requires `allow_hide_players` to also be enabled.
    - Dataloader: `/assets/perspective/hide_players.json`
      - Uses the same format as the other hide dataloaders.  

### Development Build  
Please help us improve by submitting [bug reports](https://github.com/MCLegoMan/Perspective/issues) if you encounter any issues.  

### Want to support my work?  
If you'd like to donate, visit [BuyMeACoffee](https://www.buymeacoffee.com/mclegoman).
Your support is appreciated, please be aware that donations are non-refundable.