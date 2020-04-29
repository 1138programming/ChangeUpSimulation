# ChangeUpSimulation

This is a simulation of the 2020-2021 Vex game Change Up.
In order to build this project, you need gnu make, which you should have if you have pros. You'll also need java obviously.
To build it, just use the command: make.
To run it, either start the jar file from the command line, or use the command: make run.

I'll give a bit of a description of what I think this simulation should be:
I'm implementing a tiny physics engine (one that will just handle collisions between circles) because I think a big part of this game is going to be defense, and blocking other robots from reaching certain parts of the field. As for actions such as collecting balls and scoring/descoring, they will be based on the distance to a ball or goal. 

Each robot will be able to hold 3 balls max, and will be able to collect and score/descore. In giving each robot the same abilities (and there aren't many that are needed to play the whole game), I'm hoping to emphasize more the strategy, since that is the biggest part of this game imo.

The objects that represent the physical field, such as the goal, field walls, robots, and balls, will be instantiated once when the program starts, and the field will be reset whenever a new match starts.
