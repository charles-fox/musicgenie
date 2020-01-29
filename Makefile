#sudo apt-get install default-jdk
#openjdk9 does not work, awt button add is broken, see Tests

#possibly we need to install fluidsynth and a soudfont, and reboot the machine, for MIDI to work?  (reboot so that fluid autostarts and is picked up by the alsa MIDI system?)
#sudo apt install fludsynth
#sudo apt install fluid-soundfont-gm

#to play a MIDI file in fluid from the command line:
#fluidsynth --audio-driver=alsa -o audio.alsa.device=hw:1 /usr/share/sounds/sf2/FluidR3_GM.sf2 mid.mid



JFLAGS = -g -d build/ -classpath build/
JC = javac $(JFLAGS)
MG = src/uk/ac/cam/cwf22/mg/

all: $(MG)/*/*.java
	mkdir -p build/uk/ac/cam/cwf22/mg/default
	cp $(MG)/default/*.gen build/uk/ac/cam/cwf22/mg/default/
	$(JC) $(MG)/core/*.java  $(MG)/compiler/*.java $(MG)/gui/*.java $(MG)/graphics/*.java $(MG)/midi/*.java $(MG)/*.java

#to run:
#	java -classpath build/ uk.ac.cam.cwf22.mg.MusicGenie

#to run unit tests:
#	java -classpath build/ uk.ac.cam.cwf22.mg.Test_MusicGenie
