build: src/*.scala
	fsc -d bin/ src/*.scala ~/code/scala/library/*.scala

clean:
	rm tmp/*

SetupProgram:
	scala -classpath bin SetupProgram

UserApp:
	scala -classpath bin UserApp

PrettyPrintUtility:
	scala -classpath bin PrettyPrintUtility

run: clean SetupProgram PrettyPrintUtility UserApp
	scala -classpath bin PrettyPrintUtility

all: build run

.PHONY: all run
