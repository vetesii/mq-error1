
:: UTF-8
chcp 65001

SET CURRENT_DIR=%~dp0
echo %CURRENT_DIR:~0,-1%

:: Build

call mvn clean install -Dfile.encoding=UTF-8 -Dmaven.test.skip=true -Dmaven.javadoc.skip=true

set CLIENT_MODULE=%CURRENT_DIR%
set JAVA_OPTS= -Xms128m -Xmx256m -XX:+HeapDumpOnOutOfMemoryError -XX:MaxMetaspaceSize=128m
set JAVA_OPTS=%JAVA_OPTS% -Dspring.config.location=file:%CLIENT_MODULE%\src\main\env\local\mq-error.properties
::set JAVA_OPTS=%JAVA_OPTS% -Dspring.config.location=file:%CLIENT_MODULE%..\mq-error.properties

start java %JAVA_OPTS% -jar %CLIENT_MODULE%\target\mq-error.jar
