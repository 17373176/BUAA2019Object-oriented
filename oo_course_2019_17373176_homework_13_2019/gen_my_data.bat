@echo off
@echo Start execute...

set/p input1=��������Ҫ��ӡ��mdj�ļ��������س�

java -jar uml-homework-1.0.0-raw-jar-with-dependencies.jar list -s "%input1%.mdj"

set/p input2=��������Ҫ��ӡ���������������س�

java -jar uml-homework-1.0.0-raw-jar-with-dependencies.jar dump -s "%input1%.mdj" -n %input2% > data.txt

echo succeed!
@pause
