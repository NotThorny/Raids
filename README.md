# Raids Basics

This is a plugin which allows you to set up raid bosses with a single command.

All bosses will spawn a reward flower on being defeated! To edit what these rewards are, make a new entry in `RewardPreviewExcelConfigData.json` with the mob's id as the entry id. More info in [Reward Example](https://github.com/NotThorny/Raids?tab=readme-ov-file#reward-example)

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

  ### Reward Example:
  This is an example for a reward entry for a basic hilichurl, will reward between 50 and 500 primos, and one stella fortuna. You can have as many items as you'd like, just continue adding to the previewItems.
  Desc doesn't actually matter, you can just leave it as whatever you copied another entry from.
   ```
{
    "id": 21010101,
    "Desc": "\u79bb\u7ebf\u91c7\u96c6\u5956\u52b11_4\u7ea7",
    "previewItems": [
      {
        "id": 201,
        "count": 50;500"
      },
      {
        "id": 1131,
        "count": "1"
      }
    ]
  },
```

## Version

Built for Grasscutter 1.7.4, should work on similar versions.

### Issues

#### Any suggestions or issues are welcomed in [issues](https://github.com/NotThorny/Raids/issues)
