# Crumbling Mod - Build and Installation Guide

## Prerequisites
- Java 17 or higher
- Minecraft Launcher (for testing)
- Git (optional, for cloning)

## Building the Mod

### Step 1: Navigate to Project Directory
```bash
cd c:\Users\arrow\OneDrive\Desktop\box_bur\forge_build_pipeline
```

### Step 2: Build the JAR
```bash
./gradlew.bat build
```

This will:
- Download Minecraft 1.20.1 source
- Apply Forge mappings
- Compile all Java code
- Package the JAR file

### Step 3: Locate Output JAR
The compiled JAR will be at:
```
build/libs/crumblingmod-1.0.0.jar
```

## Installation in Minecraft

### Setup Forge (First Time Only)
1. Install Forge 47.4.10+ for Minecraft 1.20.1 from [minecraftforge.net](https://minecraftforge.net)
2. Launch Minecraft with Forge profile to generate `/mods` directory

### Install the Mod
1. Copy `crumblingmod-1.0.0.jar` to your mods folder:
   - Windows: `%APPDATA%\.minecraft\mods\`
   - Linux: `~/.minecraft/mods/`
   - macOS: `~/Library/Application Support/minecraft/mods/`

2. Launch Minecraft with Forge

## Usage in Game

### Placing Mushroom Support
1. Find the "Mushroom Support" item in creative mode or via `/give @s crumblingmod:mushroom_support`
2. Place the block - it will automatically create a 2-block tall pillar
3. The structure creates a 3×3 safe zone (1 block in each direction)

### Testing Crumbling Mechanics
1. Create an area with dirt/stone blocks around the support
2. Break blocks outside the 3×3 safe zone
3. Watch adjacent crumbleable blocks fall like sand
4. Break support and watch the safe zone disappear

## Configuration

### Mod Configuration File
Location: `.minecraft/config/crumblingmod-common.toml`

Available settings:
- `supportBlockSafeZoneRadius=1` - Safe zone size (1 = 3×3)
- `enableCrumblingLogging=false` - Debug logging
- `visualizeSafeZones=false` - Visual debug (future feature)

## Troubleshooting

### Build Fails
- Ensure you have Java 17+: `java -version`
- Try cleaning: `./gradlew.bat clean`
- Try rebuilding: `./gradlew.bat build --refresh-dependencies`

### Mod Appears in Mods Menu but Doesn't Work
1. Check Minecraft log for errors
2. Verify you're in 1.20.1 version
3. Verify Forge is properly installed
4. Check that crumblingmod.jar is in correct mods folder

### Mushroom Support Block Missing
1. Verify mod is loaded (check mods list)
2. Try `/reload` command
3. Restart Minecraft

## Testing Checklist

- [ ] Mod loads without errors
- [ ] Mushroom Support item appears in creative
- [ ] Block can be placed
- [ ] Two-block pillar created automatically
- [ ] Safe zone prevents crumbling of nearby blocks
- [ ] Blocks outside safe zone crumble when adjacent block breaks
- [ ] Mushroom Support destruction triggers crumbling
- [ ] Falling blocks use proper physics
- [ ] English and Russian translations visible in-game

## Development

### Running in IDE
For IntelliJ IDEA:
1. Run Gradle task: `Tasks > fg > genIntellijRuns`
2. Run configuration: "runClient" for single-player
3. Run configuration: "runServer" for server testing

For Eclipse:
1. Run Gradle task: `Tasks > fg > genEclipseRuns`
2. Run Eclipse run configurations

### Modifying Code
After code changes:
1. Rebuild: `./gradlew.bat build`
2. Copy new JAR to mods folder
3. Restart Minecraft

## Support

For issues or questions, refer to:
- README_MOD.md - Feature documentation
- IMPLEMENTATION_SUMMARY.md - Technical details
- COMPLETION_CHECKLIST.md - Implementation status
- [Minecraft Forge Docs](https://docs.minecraftforge.net/)

## Version Info
- **Minecraft Version**: 1.20.1
- **Forge Version**: 47.4.10+
- **Mod Version**: 1.0.0
- **Java Version**: 17+
