@echo off
@echo Start execute...

set/p inputMdj=��������Ҫ��ӡ��mdj�ļ��������س�

java -jar uml-homework-2.1.0-raw-jar-with-dependencies.jar list -s "%inputMdj%.mdj"



set/p inputType=��������Ҫ�鿴���������Ͳ����س�
set/p inputModeName=��������Ҫ�鿴������ģ�����Ʋ����س�

java -jar uml-homework-2.1.0-raw-jar-with-dependencies.jar list -s "%inputMdj%.mdj" -t %inputType% -n %inputModeName%

java -jar uml-homework-2.1.0-raw-jar-with-dependencies.jar dump -s "%inputMdj%.mdj" -n %inputModeName% -t %inputType% > data.txt

echo succeed!
@pause
