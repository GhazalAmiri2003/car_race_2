# Java Swing Car Racing Simulator

An interactive Java racing simulation with a Swing desktop interface, a player-controlled car and a computer-controlled opponent.

## Features
- Step-based race simulation and countdown.
- Vehicle and driver abstractions with multiple implementations.
- Track geometry, curves, obstacles, fuel consumption and race events.
- Opponent generation and player keyboard input.
- Separate renderers for cars, road, obstacles, race information and countdown.
- Race event history and finish-state tracking.

## Architecture
| Package | Purpose |
| --- | --- |
| `car` | Vehicle state, behaviour and configuration |
| `driver` | Player and opponent behaviour |
| `event` | Race events |
| `track` | Track geometry and obstacles |
| `race` | Simulation loop and race state |
| `gui` | Swing rendering and keyboard bindings |

## Run locally
Use a modern JDK (17 or later recommended) with a graphical desktop environment. Open `src` as a Java source root in an IDE and run `Main.java`.

Alternatively, on a Unix-like shell:
```bash
mkdir -p out
javac -d out $(find src -name '*.java')
java -cp out Main
```

## Controls
| Key | Action |
| --- | --- |
| F | Fast |
| S | Safe |
| J | Jump |
| Space | Turn |

## Status
Source-code portfolio project demonstrating object-oriented design, simulation and desktop GUI development. The launch steps are based on the repository structure; compilation and runtime have not been independently verified.
