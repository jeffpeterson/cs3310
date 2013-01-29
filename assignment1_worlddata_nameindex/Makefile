build: src/*.scala
	fsc -d bin/ src/*.scala

run_setup:
	scala -classpath bin SetupProgram

run_user:
	scala -classpath bin UserApp

make run: run_user

all: build run

.PHONY: all run
