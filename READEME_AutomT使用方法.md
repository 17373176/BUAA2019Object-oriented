# 自动化测试工具
----------
	/**  
	 * 从文本文件中读取输入测试并执行  
	 * 自动获取程序控制台输出结果  
	 */

----------

## 读取输入，获得输出
	将java文件全部打包为jar文件，命名为Myjar.jar.  
	将测试数据的文件命名为input.txt  
	执行Test.bat文件生成输出结果文件output.txt.  

bat批处理文件编写：  

	用输入输出重定向解决输入文本和输出文本  
	java -jar Myjar.jar  

## 比较多个输出文件结果是否相同
	首先将多个需要比较的文件命名为outFile1.txt、outFile2.txt，形式为outFile%d.txt，%d为文件数  
	运行compare_file程序，输入比较文件的数量，会输出compareFile文件，文件内容为difference.

## 随机生成测试数据
	运行generate_test_data程序会自动输出随机数据文件generate_test_data.txt
