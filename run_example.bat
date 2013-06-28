ECHO OFF
SET BASE_PATH=.
SET CLASS_PATH=%CLASS_PATH%;%BASE_PATH%\lib\commons-io-1.4.jar
SET CLASS_PATH=%CLASS_PATH%;%BASE_PATH%\lib\commons-lang-2.5.jar
SET CLASS_PATH=%CLASS_PATH%;%BASE_PATH%\lib\in2dorserver.jar
SET CLASS_PATH=%CLASS_PATH%;%BASE_PATH%\lib\stopword_filter.jar
SET CLASS_PATH=%CLASS_PATH%;%BASE_PATH%\lib\setvect.jar
SET CLASS_PATH=%CLASS_PATH%;%BASE_PATH%\lib\tms3003.jar
SET CLASS_PATH=%CLASS_PATH%;%BASE_PATH%\lib\xerces.jar
SET JVM_EXE="java.exe"

ECHO ON
%JVM_EXE% -Xms128m -Xmx256m -classpath %CLASS_PATH% sample.SimilaritySearcherSample
pause

