# Untitled stealth factory game
This is an entry for the 8th 3m5.GameJam.
The game is built with LibGDX and Kotlin using LibKTX. 

### Gameplay
You control a factory that is drilling itself down through the earth, starting from a holy crater. 
This is highly illegal, so you need to manage the public's awareness of your operation by bribing anyone that finds out what you are doing.
Your factory mines resource deposits that you happen to come by.
You can sell these resources directly, or buy machines to create more advanced items that sell for a better price.
However, your machines will make noise, which increases the public's awareness of your operation.

Currently, there is no ending. Press `ESC` to quit the game whenever you got your fill.

### Things to build
- **Smelters** smelt iron and copper ore into ingots.
- **Furnaces** smelt iron ingots and coal into steel beams.
- **Wire makers** produce copper wire from copper ingots.
- **Chem labs** convert coal into oil.
- **Mining modules** enable your factory to mine resource deposits faster.
- **Drilling modules** make your factory drill down faster.

### Known issues / things to improve
- The only supported resolution is 1920x1080.
- Performance becomes a problem later on, when the factory is full of working buildings.
The current UI implementation is not very performant, but we needed to hack this in pretty quickly.
- Building icons might not make a lot of sense. Oops!
- The balancing may be off. We were rushing out "content" in the last hours of the jam and as always, 
we did not have the time to fully play-test the game in its "full glory".
- There are a few cheat hotkeys left in the game. Use them at your own risk:
  - `1` adds 100 iron ore to your inventory
  - `2` sells 1 iron ore
  - `Q` builds a smelter in the bottom left corner
  - `W` upgrades the smelter in the bottom left corner
  - `SPACE` speeds up the game significantly while held down

### Credits (in alphabetical order)
| Who                               | What                                                        |
|:---------------------------------:|:-----------------------------------------------------------:|
| Ivan Ananev                       | Game logic code, sprite sheet research, sprite modification |
| [JakoD](https://github.com/JakoD) | UI code, game logic code                                    |
| Stefan Beyer                      | Game design, game logic code, sprite creation, balancing    |
| Susann Beyer                      | Sound design                                                |

### Used assets
We used parts of those free sprite sheets:
- ["tech dungeon roguelite" by trevor pupkin](https://trevor-pupkin.itch.io/tech-dungeon-roguelite?download)
- ["rpg worlds ca" by szadiart](https://szadiart.itch.io/rpg-worlds-ca?download)
- ["generic dungeon pack" by bakudas](https://bakudas.itch.io/generic-dungeon-pack?download)
- ["ashlands tileset" by finalbossblues](https://finalbossblues.itch.io/ashlands-tileset?download)
- ["blocky life" by admurin](https://admurin.itch.io/blocky-life)
- ["grab hand" by kicked in teeth](https://kicked-in-teeth.itch.io/grab-hand)
- ["industrialscifi top down tileset" by helleworld](https://helleworld.itch.io/industrialscifi-top-down-tileset?download)
- ["lots of free 2d tiles and sprites" by hyptosis](https://opengameart.org/content/lots-of-free-2d-tiles-and-sprites-by-hyptosis)
