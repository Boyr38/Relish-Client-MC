# Relish Client v2.1

A modular PvP/utility HUD client for Fabric on Minecraft 26.2 (Java 25).

**Modules:** FPS, CPS, Ping, Coords, Keystrokes, Armor status, Potion status, Zoom (hold C).
**Menu:** Right Shift (rebind in Options > Controls). Click cards to toggle modules, **HUD Editor** to drag things where you want them, **Reset Layout** to undo (both in the sidebar).
Toggles and positions save to `config/relishclient.json`.

## Run / build
Needs JDK 25.

    ./gradlew runClient     # dev client (Windows: gradlew.bat runClient)
    ./gradlew build         # jar in build/libs/relishclient-2.1.1.jar

To play with it: install Fabric Loader for 26.2 + Fabric API, drop the jar in `mods/`.

## Changelog
- 2.1.1: Relish logo as mod icon and in the menu sidebar.
- 2.1.0: Lunar-style UI: sidebar mod menu with toggle cards, rounded HUD with blue accent.
- 2.0.0: renamed to Relish Client; drag-to-move HUD editor; armor and potion status; two-column menu.
- 0.1.0: first build (as Nova Client).

## Versions
Pinned in `gradle.properties`. Check newer ones with `npx @fabricmc/cli versions 26.2`.

## Roadmap
Proper CPS via mixin, scroll-to-adjust zoom, module scale/colour options.
Stick to cosmetic/QoL features; gameplay-advantage features get you banned.

## Making the installer exe (for other people)
The repo has a GitHub Actions workflow (`.github/workflows/build.yml`) that compiles the mod and builds
`RelishClient-Setup.exe` with Inno Setup (`installer/relish.iss`). The installer installs Fabric Loader,
Fabric API and Relish into the normal `.minecraft` folder using the launcher's own Java.

1. Create a GitHub repo and push this folder to it (branch `main`).
2. Open the repo's **Actions** tab. When the run finishes, download **RelishClient-Setup** from the run page.
3. To publish a public download, tag a release: `git tag v2.1.1 && git push --tags`. The exe is attached to the release.

The exe is unsigned, so Windows SmartScreen will warn ("More info" > "Run anyway").
