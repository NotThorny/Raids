# Raids Basics

This is a plugin which allows you to set up raid bosses with a single command.

There will probably be issues if you don't use the commands as intended so follow the usage.

Don't blame me if you spawn 100000 mobs and crash your game/server.

## Installation

Get latest release from [releases](https://github.com/NotThorny/Raids/releases)

Place Raids.jar into your `\grasscutter\plugins` folder and restart the server if it was already running

## Usage

 All bosses come with a boss bar at the top of the screen.

 `/rd new <mobId> x[amount] lv[level] hp[hp] atk[attack] def[defense] maxhp[maxhp] hx[hp multiplier]`
  
   - Creates the specified boss exactly where you are standing and facing
   - Boss will spawn when a player gets close enough (in the nearby map areas, same as normal mob spawns)
   - Bosses save to config file and will respawn on relog, or when defeated and then the area is unloaded and reloaded (teleport, change scene, etc)
   - ALL FIELDS ARE OPTIONAL - Do not include fields you do not wish to set custom values for! Level will default to 200, the rest to the mob's default values.
   
 `/mw start`

   - Force spawns all bosses in config. Any that are already spawned will be skipped.

   ### Things to note:
    - Only one boss entry can exist for any given mob id. Use alternate mob ids if you want multiple of the same monster (check the GM Handbook for ids typically 1 digit off)

## Version

Built for Grasscutter 1.7.4, should work on similar versions.

### Issues

#### Any suggestions or issues are welcomed in [issues](https://github.com/NotThorny/Raids/issues)
