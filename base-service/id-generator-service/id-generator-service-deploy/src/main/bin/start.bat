@echo off
rem ======================================================================
rem windows startup script
rem
rem author: chenck
rem date: 2018-12-28
rem ======================================================================

rem 项目名称
set APPLICATION="id-generator-service-deploy"

TITLE %APPLICATION%

set CONFIG=-Dlogging.path=../logs -Dlogging.config=../conf/logback-spring.xml -Dspring.config.location=../conf/
set JAVA_OPT=-server -Xms256m -Xmx512m -Xmn512m -XX:MetaspaceSize=64m -XX:MaxMetaspaceSize=128m

echo "Starting the %APPLICATION%"
java %JAVA_OPT% %DEBUG_OPTS% %JMX_OPTS% %CONFIG% -jar ../boot/%APPLICATION%.jar

pause
