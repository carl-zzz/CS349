# super simple makefile
# call it using 'make NAME=name_of_code_file_without_extension'
# (assumes a .java extension)
NAME = "A2Basic"

all:
	@echo "Compiling..."
	javac -cp vecmath.jar *.java

run: all
	@echo "Running..."
	# java $(NAME)
	java -cp "vecmath.jar:." $(NAME)

clean:
	rm -rf *.class
