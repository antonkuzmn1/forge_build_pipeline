# Crumbling Mod

A Minecraft Forge mod for version 1.20.1 that implements crumbling mechanics with support structures.

## Features

### Block Crumbling System
- **Safe Zones**: Mushroom support blocks create a 3×3 safe zone where blocks won't crumble
- **Falling Blocks**: When blocks outside safe zones are destroyed, certain block types fall like sand
- **Supported Blocks**: Dirt, coarse dirt, grass block, podzol, all ice types, and various stone types

### Mushroom Support Block
- Two-block tall pillar structure
- Uses vanilla mushroom stem texture
- Upper block has slight visual expansion
- Automatically links lower and upper blocks
- Creates a 3×3 safe zone (1 block radius from center)
- When one block of the pillar is destroyed, the entire structure collapses

## Mechanics

### Safe Zones
- 3×3 area (horizontally) centered on support blocks
- Blocks within safe zones don't crumble when adjacent blocks are broken
- Multiple support blocks can overlap their safe zones

### Crumbling
- Triggered when a cube is broken outside a safe zone
- Affects all crumbleable blocks in a 3×3 area at the break point
- Uses Minecraft's native FallingBlockEntity for physics

### Support Block Behavior
- Placing a mushroom support block automatically creates both upper and lower parts
- Destroying either part destroys the entire structure
- Triggers crumbling in the 3×3 area when destroyed

## Supported Crumbleable Blocks

### Soil
- Dirt
- Coarse Dirt
- Grass Block
- Podzol

### Ice
- Ice
- Packed Ice
- Blue Ice
- Snow Block
- Frosted Ice

### Stone
- Stone
- Granite (normal, polished)
- Andesite (normal, polished)
- Diorite (normal, polished)
- Sandstone (normal, red)

## Installation

1. Ensure you have Forge 47.4.10 or compatible version installed
2. Place the mod JAR in your mods folder
3. Launch Minecraft with Forge

## Configuration

Configuration options can be modified in `config/crumblingmod-common.toml`

## Technical Details

- Uses local block event checking (no world ticking)
- Efficient safe zone detection
- Leverages Minecraft's built-in falling block entities
- Client-side only features disabled

## Building from Source

```bash
./gradlew build
```

The compiled JAR will be in `build/libs/`

## License

All Rights Reserved

## Author

Development Team
