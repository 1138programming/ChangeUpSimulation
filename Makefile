#
# A makefile for compiling a java project
#

# define a makefile variable for the java compiler
JCC := javac

# jvm
JVM := java

# The main directory of the project
FROM := src/

# The output directory of the project
OUT := build

# define a makefile variable for compilation flags
JFLAGS := -Xlint -d ./$(OUT)

# Project name
PROJECT := ChangeUpSimulation

# Recursive wildcard function to search a directory
rwildcard = $(wildcard $(addsuffix $2, $1)) $(foreach d, $(wildcard $(addsuffix *, $1)), $(call rwildcard, $d/, $2))

# Constructs source file locations
SRCFILES := $(filter %.java, $(call rwildcard, $(FROM), *.java))

.PHONY : classes clean run

# typing 'make' will invoke the first target entry in the makefile 
# (the default one in this case)
default : $(PROJECT).jar

# this target entry builds the project jar file
# the project jar file is dependent on the class files
$(PROJECT).jar : classes
	$(info creating jar file...)
	jar cvfm $(PROJECT).jar manifest.txt -C $(OUT) *

# this target entry builds the project class files
# the class files are dependent on the java files in the subdirectories
# this is the rule to create them
classes : | $(OUT)
	$(info creating class files...)
	$(JCC) $(JFLAGS) $(SRCFILES)

# this target entry creates the output directory
$(OUT) :
	$(info creating output directory...)
	mkdir $(OUT)

# To start over from scratch, type 'make clean'.  
# Removes all .class files, so that the next make rebuilds them
clean :
	$(info cleaning project...)
	$(RM) -rf $(OUT)
	$(RM) *.class
	$(RM) *.jar

run :
	$(JVM) -jar $(PROJECT).jar