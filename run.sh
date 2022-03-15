#bin/bash

source .colors.sh;

SRC_ROOT="./src";
TEST_ROOT="./tests";

JAR_HOME="./lib";
JUNIT_JAR="$JAR_HOME/junit-platform-console-standalone-1.5.2.jar";
JSON_JAR="$JAR_HOME/json.jar";

				    # thumbs up		# thinking  #confused
declare -a EMOJIS=("👍"    "🤔"        "🤷‍♂️");

CLASS_PATH="$JUNIT_JAR:$JSON_JAR:$SRC_ROOT:$TEST_ROOT";

function compile () {
	java_files=$(find $1 -name $2 -print);
	if javac -Xlint -cp $CLASS_PATH $java_files; then
		if [[ $3 != "-s" ]]; then
			printf "${GREEN}compiled successfully ${EMOJIS[0]}${NOCOLOR}\n";
		fi;
	else
		printf "${RED}compilation failed ${EMOJIS[1]}${NOCOLOR}\n";
		exit 1;
	fi;
}

function runtests () {
	if [[ $1 == "-f" ]]; then
		java -jar $JUNIT_JAR -cp $CLASS_PATH -f $2;
	else
		java -jar $JUNIT_JAR -cp $CLASS_PATH -p $1;
	fi
}

case $1 in
	"compile" | "c")
		case $2 in
			"-t") compile $TEST_ROOT "*Test*.java";; 	# only compiles test files
			"-d") compile $3 "*.java";;					# specify a directory, eg. $ ./run c -d src/unsw/components
			"-m") compile $SRC_ROOT ${3:-"*.java"};;	# specify a pattern to (m)atch, eg. $ ./run r -m "*Component*.java"
			   *) compile $SRC_ROOT "*.java";;			# compile all files
		esac;;

	"tests" | "t")
		if ! compile $TEST_ROOT "*Test*.java" -s; then	# first compile all test files in /tests
			exit 1;
		fi;
		
		case $2 in
			"-p") runtests $3;;							# specify a package name, eg. $ ./run t -p unsw.components
			"-f") runtests -f $3;;						# specify a file name, eg. $ ./run -t -f test/unsw/components/TestTransformComponent.java
			   *) runtests "unsw";;						# run all tests in the global package unsw
		esac;;

	*)
		printf "${YELLOW}${EMOJIS[2]} unrecognised command: ${NOCOLOR}%s\n" $1;;
esac
