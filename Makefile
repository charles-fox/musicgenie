#sudo apt-get install default-jdk
#openjdk9 does not work, awt button add is broken, see Tests

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
