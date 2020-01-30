# musicgenie
Genetic Hierarchical Music Structures - human and automated music composition tool.

From the paper,
Fox, C. (2006). Genetic Hierarchical Music Structures. In FLAIRS Conference (pp. 243-247).

Audio demos from the paper are here:
https://www.youtube.com/playlist?list=PLjrJD5nSYNjoQebwV7TWPe83k7g2FjIO6

January 2020: I am teaching Team Software Engineering at the University of Lincoln.   As an example of software maintainance, I am experimenting to see how easy it will be to get my 20 year old undergrad project to run on a 2020 Ubuntu system under OpenJDK.     You can see the changes that have been needed in the git logs and issues.    Its taken a couple of evenings but it now runs again!   
The moral of this story, I think, is:   When I wrote this in 1999, it seemed that "doing software engineering" involved a whole load of massively over-engineered stuff which was there for the purposes of getting a good mark for a student project. Such as: use of a strongly typed language, rigourous unit tests, design patterns, modular separation,  rigourous namespaces, rigourous documentation via both comments and architecture overview docs.   Pulling out this 20 year old code and getting it to run on a modern system has utterly relied on all of those things being there and has actually made it a lot of fun!   Most of the changes are due to changes in Java's GUI and sound systems over time, plus build system, OS and IDE changes. Java gets a bad reputation for enforcing the programmer to have to do all this stuff but I am feeling extremely thankful for it all after not touching this code for a fifth of a century.   (Also editing Java in vim with jedi on Ubuntu and having git is much nicer than some of the horrific GUI IDE stuff used back in the day.)  Maybe Java is OK after all ... and it has generics now .. and still no header files!

To run MusicGenie on Ubuntu 16.04:

First install Fluidsynth, which is a MIDI synthesizer to make the sounds:
```
sudo apt install fludsynth

sudo apt install fluid-soundfont-gm
```
(Maybe need to restart your machine here so that fluid gets run).

Then clone, build, and run MusicGenie with:
```
git clone https://github.com/charles-fox/musicgenie.git

make

java -classpath build/ uk.ac.cam.cwf22.mg.MusicGenie

```


