# acs - Application Source Code Search #

*Pre-install software: Java, Maven, Tomcat*
	
You need to config your acs at com.moodys.acs.util.AcsContext, just like below:

1. 	WINDOWS_BAT_PATH/LINUX_BAT_PATH = "[Script Of Get/Update Source Codes From Git/CVS/SVN/P4]"
2. 	FILE_INDEX = "[Lucene Index Path]"
3. 	RO_FILE_TARGET_999 = "[Application Source Codes Path, like Main Version]"
	
You could also config your acs job time at applicationContext-job.xml, just like below:

1. 	deleteIndexFolder = <value>0 0 1 * * ?</value>
2. 	runBat = <value>0 5 1 * * ?</value>
3. 	createIndex = <value>0 10 1 * * ?</value>
