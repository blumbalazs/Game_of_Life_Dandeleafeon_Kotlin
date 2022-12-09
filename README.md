# Dandelifeon Kotlin simulator

JavaFX project that draws graphics on a canvas, based on JDK 17.

Run the application with `./gradlew run`.


With clicking on canvas the cells state can be changed, white = Dead Cell, blue = Live Cell, black = Wall.

Blue cells show their age, it can not be growing above 100.

Start button start simulation. Simulation can be stopped with Stop button (which is at the start buttons position when simulation is running)

Out of the simulation the game can be stepped. 

After a live cell is generated on red fields, the game end and calculates the generated mana, which is then show at the top of the screen.

