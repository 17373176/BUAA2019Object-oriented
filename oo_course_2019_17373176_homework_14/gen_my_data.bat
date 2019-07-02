@echo off
@echo Start execute...

set/p inputMdj=请输入需要打印的mdj文件名并按回车

java -jar uml-homework-2.1.0-raw-jar-with-dependencies.jar list -s "%inputMdj%.mdj"



set/p inputType=请输入需要查看的数据类型并按回车
set/p inputModeName=请输入需要查看的数据模型名称并按回车

java -jar uml-homework-2.1.0-raw-jar-with-dependencies.jar list -s "%inputMdj%.mdj" -t %inputType% -n %inputModeName%

java -jar uml-homework-2.1.0-raw-jar-with-dependencies.jar dump -s "%inputMdj%.mdj" -n %inputModeName% -t %inputType% > data.txt

echo succeed!
@pause
