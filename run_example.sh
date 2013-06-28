#! /bin/ksh

export BASE_PATH=.
export CLASS_PATH=$BASE_PATH/bin
export CLASS_PATH=$CLASS_PATH:$BASE_PATH/lib/commons-lang-2.5.jar
export CLASS_PATH=$CLASS_PATH:$BASE_PATH/lib/in2dorserver.jar
export CLASS_PATH=$CLASS_PATH:$BASE_PATH/lib/in2tmsapi.jar
export CLASS_PATH=$CLASS_PATH:$BASE_PATH/lib/nrf_saltlux.jar
export CLASS_PATH=$CLASS_PATH:$BASE_PATH/lib/setvect.jar
export CLASS_PATH=$CLASS_PATH:$BASE_PATH/lib/xerces.jar
export JVM_EXE="java"

$JVM_EXE -Xms128m -Xmx256m -classpath $CLASS_PATH sample.ReviewerRecommendSample

