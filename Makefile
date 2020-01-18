JFLAGS = -g -d build/ -cp build/
JC = javac $(JFLAGS)
MG = src/uk/ac/cam/cwf22/mg/

all: $(MG)/*/*.java
	$(JC) $(MG)/core/*.java  $(MG)/compiler/*.java $(MG)/gui/*.java $(MG)/graphics/*.java $(MG)/midi/*.java $(MG)/*.java

#to run:  java -cp build/ uk.ac.cam.cwf22.mg.MusicGenie
