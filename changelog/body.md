This update completely changes how shaders are rendered, making use of Luminance's shader rendering.

## Changelog
### Shaders
- Shaders are now both registered and rendered in Luminance.
- #### Luminance Shader Layout
  - Luminance's shader layout is based on Perspective's layout, with a couple of key differences.
    - `shader` has been renamed to `name`.
      - Luminance will also load `shader` for backwards-compatibility, but it is recommended to use `name` in your shaders.
    - `disable_screen_mode` has been renamed to `disable_game_rendertype`.
      - Luminance will also load `disable_screen_mode` for backwards-compatibility, but it is recommended to use `disable_game_rendertype` in your shaders.
    - `entity_links` has been moved to `"custom: {"perspective":{"entity_links": []}}`
      - Perspective will also load entity links from the `souper_secret_settings` namespace.
        - Entity Links will be disabled if Souper Secret Settings is detected so shaders aren't rendered twice.   