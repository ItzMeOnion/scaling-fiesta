# Butterfly Knife Spin

A client-side Fabric mod for Minecraft 1.21.8. Press **G** to spin the item in your main hand, butterfly-knife style.

## Building

1. Install JDK 21.
2. Check the versions in `gradle.properties` against https://fabricmc.net/develop/.
3. Run `./gradlew build` (generate the wrapper with `gradle wrapper` first if it is missing).
4. The jar is written to `build/libs/`. Put it in your `mods` folder with Fabric API.

## Notes

- Client-side only. It does not change anything on the server.
- Only the held item spins, not the arm.
