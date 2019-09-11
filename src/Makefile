JC = javac
JFLAGS = -g

BIN=bin
SRC=src
DOC=doc

.SUFFIXES: .java .class

.java.class:
	$(JC) ${JFLAGS} *.java

CLASSES = \
	CloudData.java \
	 CloudDataP.java \
	 Vector.java \
	TestSeq.java \
	TestPar.java
CLASS_FILES: $(CLASSES:.java=*.class)

default:
	CLASS_FILES

clean:
	rm *.class

docs:
	javadoc -classpath ${BIN} -d ${DOC} ${SRC}/*.java

cleandocs:
	rm /*.html

