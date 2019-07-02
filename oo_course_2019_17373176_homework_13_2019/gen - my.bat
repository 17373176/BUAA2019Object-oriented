@echo off
@echo Start execute...

set/p input1=请输入需要打印的mdj文件名并按回车

java -jar uml-homework-1.0.0-raw-jar-with-dependencies.jar list -s "%input1%.mdj"

set/p input2=请输入需要打印的数据容器并按回车

java -jar uml-homework-1.0.0-raw-jar-with-dependencies.jar dump -s "%input1%.mdj" -n %input2% > data.txt

echo succeed!
@pause
