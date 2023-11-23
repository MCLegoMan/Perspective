![](https://mclegoman.com/images/a/a7/Perspective_Development_Logo.png)

Wait, where did that go?!
This update introduces hide armour and hide nametag dataloaders!
That means you can now hide armour or nametags of specific players using resource packs!
This update also fixes the issue in the previous update where the super secret settings config screen wouldn't render.  

We should be moving into beta soon, as this update is now feature-complete.
During beta and release candidate, expect bug fixes,
if any new features are added, they will be listed under the experimental options.

### Perspective 1.2.0-alpha.6 for 1.20.2 and 1.20.3.  
**Hosting Update:** *1.2.0-release.1 will be the final version to be released on Curseforge.*
More information on this change will be posted as we get closer to 1.2.0-release.1.  

## Changelog  
- **Fixed Super Secret Settings Config Screen.**  
  - The show name option wasn't updated in the previous version.
- **Added Hide Armor and Hide Nametags dataloaders.**  
  - This allows the user to hide nametags or armour of specific users.  
  - This can be configured within resource packs at the following files: `/assets/perspective/hide_armor.json` and `/assets/perspective/hide_nametags.json`.  
    - The list uses the players UUID.
    - > **Hide Armor/Nametag dataloader layout example.**
      > ```
      > {
      >     "values": [
      >         "772eb47b-a24e-4d43-a685-6ca9e9e132f7"
      >     ]
      > }
      > ```
      > This would hide the nametag or armour of MCLegoMan.

### Development Build  
Please help us improve by submitting [bug reports](https://github.com/MCLegoMan/Perspective/issues) if you encounter any issues.  

### Want to support my work?  
If you'd like to donate, visit [BuyMeACoffee](https://www.buymeacoffee.com/mclegoman).
Your support is appreciated, please be aware that donations are non-refundable.