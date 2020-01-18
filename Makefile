JFLAGS = -g -d build/ -cp build/
JC = javac $(JFLAGS)
MG = src/uk/ac/cam/cwf22/mg/

#to run:  java -cp build/ uk.ac.cam.cwf22.mg.MusicGenie

core: $(MG)/core/*.java
	$(JC) $(MG)/core/*.java  $(MG)/compiler/*.java $(MG)/gui/*.java $(MG)/graphics/*.java $(MG)/midi/*.java $(MG)/*.java



#all: $(MG)/core/Rational.class $(MG)/core/Key.class  $(MG)/core/Note.class 

$(MG)/core/Rational.class: $(MG)/core/Rational.java
	$(JC) $(MG)/core/Rational.java

$(MG)/core/Key.class: $(MG)/core/Key.java
	$(JC) $(MG)/core/Key.java

$(MG)/core/Note.class: $(MG)/core/Note.java
	$(JC) $(MG)/core/Note.java


