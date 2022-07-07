@echo off

md %1\webapps\%2
md %1\webapps\%2\static
md %1\webapps\%2\Metadata
md %1\webapps\%2\Metadata\TableAccess
md %1\webapps\%2\WEB-INF
md %1\webapps\%2\META-INF
md %1\webapps\%2\WEB-INF\classes
md %1\webapps\%2\WEB-INF\lib
md %1\webapps\%2\static\devmenu
md %1\webapps\%2\static\bootstrap
md %1\webapps\%2\%7
md %1\webapps\%2\themes
md %1\webapps\%2\WEB-INF\gxusercontrols

md %1\conf\catalina\localhost
md %1\webapps\%2\WEB-INF\classes\dummy

if NOT %3 == "7" goto copyContextScanFilter
copy /Y contextGXJarScanner.xml context.xml
xcopy GXScanner.jar %1\lib /Y /D
goto :copyFiles

:copyContextScanFilter
copy /Y contextScanFilter.xml context.xml

:copyFiles
if %6 == "false" goto no_push_support
xcopy commons-lang-2.6.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy bcprov-jdk15-146.jar %1\webapps\%2\WEB-INF\lib /Y /D

xcopy web.xml %1\webapps\%2\WEB-INF /Y /D

xcopy wss4j-1.6.19.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy xalan-2.7.2.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy serializer-2.7.2.jar %1\webapps\%2\WEB-INF\lib /Y /D
if exist sun-jaxws.xml xcopy sun-jaxws.xml %1\webapps\%2\WEB-INF /Y /D

if exist %1\webapps\%2\WEB-INF\lib\log4j-api-2.3.jar del %1\webapps\%2\WEB-INF\lib\log4j-api-2.3.jar
if exist %1\webapps\%2\WEB-INF\lib\log4j-core-2.3.jar del %1\webapps\%2\WEB-INF\lib\log4j-core-2.3.jar
if exist %1\webapps\%2\WEB-INF\lib\log4j-api-2.11.2.jar del %1\webapps\%2\WEB-INF\lib\log4j-api-2.11.2.jar
if exist %1\webapps\%2\WEB-INF\lib\log4j-core-2.11.2.jar del %1\webapps\%2\WEB-INF\lib\log4j-core-2.11.2.jar
if exist %1\webapps\%2\WEB-INF\lib\log4j-api-2.13.3.jar del %1\webapps\%2\WEB-INF\lib\log4j-api-2.13.3.jar
if exist %1\webapps\%2\WEB-INF\lib\log4j-core-2.13.3.jar del %1\webapps\%2\WEB-INF\lib\log4j-core-2.13.3.jar
if exist %1\webapps\%2\WEB-INF\lib\log4j-api-2.16.0.jar del %1\webapps\%2\WEB-INF\lib\log4j-api-2.16.0.jar
if exist %1\webapps\%2\WEB-INF\lib\log4j-core-2.16.0.jar del %1\webapps\%2\WEB-INF\lib\log4j-core-2.16.0.jar
if exist %1\webapps\%2\WEB-INF\lib\log4j-1.2-api-2.16.0.jar del %1\webapps\%2\WEB-INF\lib\log4j-1.2-api-2.16.0.jar
if exist %1\webapps\%2\WEB-INF\lib\mail.jar del %1\webapps\%2\WEB-INF\lib\mail.jar
xcopy log4j-api-2.17.1.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy log4j-core-2.17.1.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy log4j-1.2-api-2.17.1.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy jakarta.mail-2.0.1.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy httpclient-4.5.13.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy httpcore-4.4.13.jar %1\webapps\%2\WEB-INF\lib /Y /D

if %9 == "false" goto :copy_jakarta_files
xcopy javax.jws-3.1.2.2.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy javax.annotation-api-1.3.2.jar %1\webapps\%2\WEB-INF\lib /Y /D

:copy_jakarta_files
if %4 == "false" goto :copy_javax_files
if not exist %1\webapps\%2\WEB-INF\lib\ha-api-3.1.12.jar del %1\webapps\%2\WEB-INF\lib\ha-api-*.jar
if not exist %1\webapps\%2\WEB-INF\lib\hk2-api-3.0.1.jar del %1\webapps\%2\WEB-INF\lib\hk2-api-*.jar
if not exist %1\webapps\%2\WEB-INF\lib\hk2-locator-3.0.1.jar del %1\webapps\%2\WEB-INF\lib\hk2-locator-*.jar
if not exist %1\webapps\%2\WEB-INF\lib\hk2-utils-3.0.1.jar del %1\webapps\%2\WEB-INF\lib\hk2-utils-*.jar
xcopy jackson-annotations-2.13.2.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy jackson-core-2.13.2.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy jackson-databind-2.13.2.1.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy jackson-jaxrs-base-2.12.2-jakarta.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy jackson-jaxrs-json-provider-2.12.2-jakarta.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy jackson-module-jaxb-annotations-2.12.2-jakarta.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy jersey-client-3.0.1.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy jersey-common-3.0.1.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy jersey-container-servlet-core-3.0.1.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy jersey-entity-filtering-3.0.1.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy jersey-hk2-3.0.1.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy jersey-media-json-jackson-3.0.1.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy jersey-server-3.0.1.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy hk2-api-3.0.1.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy hk2-3.0.1.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy hk2-locator-3.0.1.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy hk2-utils-3.0.1.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy jakarta.annotation-api-2.0.0.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy jakarta.inject-api-2.0.0.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy jakarta.validation-api-3.0.0.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy jakarta.ws.rs-api-3.0.0.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy jakarta.xml.bind-api-3.0.0.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy jaxws-rt-3.0.0.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy jakarta.xml.ws-api-3.0.0.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy jakarta.jws-api-3.0.0.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy jakarta.xml.soap-api-2.0.0.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy jakarta.activation-2.0.0.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy jaxb-core-3.0.0.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy jaxb-impl-3.0.0.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy saaj-impl-2.0.0.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy stax-ex-2.0.0.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy streambuffer-2.0.1.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy gmbal-api-only-4.0.2.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy management-api-3.2.3.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy ha-api-3.1.12.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy .\services\*.* %1\webapps\%2\WEB-INF\lib /Y /D
xcopy .\services\JakartaEE\*.* %1\webapps\%2\WEB-INF\lib /Y /D

if exist %1\webapps\%2\WEB-INF\lib\gxwebsocket.jar del %1\webapps\%2\WEB-INF\lib\gxwebsocket.jar
if exist %1\webapps\%2\WEB-INF\lib\jaxws-api-2.3.1.jar del %1\webapps\%2\WEB-INF\lib\jaxws-api-2.3.1.jar
if exist %1\webapps\%2\WEB-INF\lib\jaxb-api-2.3.1.jar del %1\webapps\%2\WEB-INF\lib\jaxb-api-2.3.1.jar
if exist %1\webapps\%2\WEB-INF\lib\javax.jws-3.1.2.2.jar del %1\webapps\%2\WEB-INF\lib\javax.jws-3.1.2.2.jar
if exist %1\webapps\%2\WEB-INF\lib\javax.annotation-api-1.3.2.jar del %1\webapps\%2\WEB-INF\lib\javax.annotation-api-1.3.2.jar
if exist %1\webapps\%2\WEB-INF\lib\gxwrapperjakarta.jar del %1\webapps\%2\WEB-INF\lib\gxwrapperjkarta.jar
if exist %1\webapps\%2\WEB-INF\lib\gmbal-api-only-3.1.0-b001.jar del %1\webapps\%2\WEB-INF\lib\gmbal-api-only-3.1.0-b001.jar
if exist %1\webapps\%2\WEB-INF\lib\ha-api-3.1.9.jar del %1\webapps\%2\WEB-INF\lib\ha-api-3.1.9.jar
if exist %1\webapps\%2\WEB-INF\lib\jaxb-core-2.3.0.jar del %1\webapps\%2\WEB-INF\lib\jaxb-core-2.3.0.jar
if exist %1\webapps\%2\WEB-INF\lib\jaxb-impl-2.3.1.jar del %1\webapps\%2\WEB-INF\lib\jaxb-impl-2.3.1.jar
if exist %1\webapps\%2\WEB-INF\lib\jaxws-rt-2.3.1.jar del %1\webapps\%2\WEB-INF\lib\jaxws-rt-2.3.1.jar
if exist %1\webapps\%2\WEB-INF\lib\management-api-3.2.1.jar del %1\webapps\%2\WEB-INF\lib\management-api-3.2.1.jar
if exist %1\webapps\%2\WEB-INF\lib\policy-2.7.5.jar del %1\webapps\%2\WEB-INF\lib\policy-2.7.5.jar
if exist %1\webapps\%2\WEB-INF\lib\stax-ex-1.8.jar del %1\webapps\%2\WEB-INF\lib\stax-ex-1.8.jar
if exist %1\webapps\%2\WEB-INF\lib\streambuffer-1.5.6.jar del %1\webapps\%2\WEB-INF\lib\streambuffer-1.5.6.jar
if exist %1\webapps\%2\WEB-INF\lib\saaj-impl-1.5.0.jar del %1\webapps\%2\WEB-INF\lib\saaj-impl-1.5.0.jar
if exist %1\webapps\%2\WEB-INF\lib\resolver-20050927.jar del %1\webapps\%2\WEB-INF\lib\resolver-20050927.jar
xcopy gxwrapperjakarta.jar %1\webapps\%2\WEB-INF\lib /Y /D
goto :copy_files

:copy_javax_files
if not exist %1\webapps\%2\WEB-INF\lib\ha-api-3.1.12.jar del %1\webapps\%2\WEB-INF\lib\ha-api-*.jar
if not exist %1\webapps\%2\WEB-INF\lib\hk2-api-2.6.1.jar del %1\webapps\%2\WEB-INF\lib\hk2-api-*.jar
if not exist %1\webapps\%2\WEB-INF\lib\hk2-locator-2.6.1.jar del %1\webapps\%2\WEB-INF\lib\hk2-locator-*.jar
if not exist %1\webapps\%2\WEB-INF\lib\hk2-utils-2.6.1.jar del %1\webapps\%2\WEB-INF\lib\hk2-utils-*.jar

xcopy commons-fileupload-1.3.3.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy jackson-annotations-2.13.2.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy jackson-core-2.13.2.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy jackson-databind-2.13.2.1.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy jackson-jaxrs-base-2.13.2.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy jackson-jaxrs-json-provider-2.13.2.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy jackson-module-jaxb-annotations-2.13.2.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy jersey-client-2.34.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy jersey-server-2.34.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy jersey-common-2.34.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy jersey-hk2-2.34.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy jersey-container-servlet-core-2.34.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy jersey-entity-filtering-2.34.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy jersey-media-json-jackson-2.34.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy hk2-api-2.6.1.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy hk2-locator-2.6.1.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy hk2-utils-2.6.1.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy javax.inject-2.4.0-b34.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy javax.jms-3.1.2.2.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy javax.ws.rs-api-2.1.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy validation-api-1.1.0.Final.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy gmbal-api-only-3.1.0-b001.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy ha-api-3.1.12.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy jaxb-api-2.3.1.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy jaxb-core-2.3.0.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy jaxb-impl-2.3.1.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy jaxws-api-2.3.1.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy jaxws-rt-2.3.1.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy management-api-3.2.1.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy policy-2.7.5.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy stax-ex-1.8.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy streambuffer-1.5.6.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy saaj-impl-1.5.0.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy resolver-20050927.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy .\services\*.* %1\webapps\%2\WEB-INF\lib /Y /D
xcopy .\services\JavaEE\*.* %1\webapps\%2\WEB-INF\lib /Y /D

if exist %1\webapps\%2\WEB-INF\lib\gxwebsocketjakarta.jar del %1\webapps\%2\WEB-INF\lib\gxwebsocketjakarta.jar
if exist %1\webapps\%2\WEB-INF\lib\gxwrapperjavax.jar del %1\webapps\%2\WEB-INF\lib\gxwrapperjavax.jar
if exist %1\webapps\%2\WEB-INF\lib\jaxws-rt-3.0.0.jar del %1\webapps\%2\WEB-INF\lib\jaxws-rt-3.0.0.jar
if exist %1\webapps\%2\WEB-INF\lib\jakarta.xml.ws-api-3.0.0.jar del %1\webapps\%2\WEB-INF\lib\jakarta.xml.ws-api-3.0.0.jar
if exist %1\webapps\%2\WEB-INF\lib\jakarta.jws-api-3.0.0.jar del %1\webapps\%2\WEB-INF\lib\jakarta.jws-api-3.0.0.jar
if exist %1\webapps\%2\WEB-INF\lib\jakarta.xml.soap-api-2.0.0.jar del %1\webapps\%2\WEB-INF\lib\jakarta.xml.soap-api-2.0.0.jar
if exist %1\webapps\%2\WEB-INF\lib\jakarta.activation-2.0.0.jar del %1\webapps\%2\WEB-INF\lib\jakarta.activation-2.0.0.jar
if exist %1\webapps\%2\WEB-INF\lib\jaxb-core-3.0.0.jar del %1\webapps\%2\WEB-INF\lib\jaxb-core-3.0.0.jar
if exist %1\webapps\%2\WEB-INF\lib\jaxb-impl-3.0.0.jar del %1\webapps\%2\WEB-INF\lib\jaxb-impl-3.0.0.jar
if exist %1\webapps\%2\WEB-INF\lib\saaj-impl-2.0.0.jar del %1\webapps\%2\WEB-INF\lib\saaj-impl-2.0.0.jar
if exist %1\webapps\%2\WEB-INF\lib\stax-ex-2.0.0.jar del %1\webapps\%2\WEB-INF\lib\stax-ex-2.0.0.jar
if exist %1\webapps\%2\WEB-INF\lib\streambuffer-2.0.1.jar del %1\webapps\%2\WEB-INF\lib\streambuffer-2.0.1.jar
if exist %1\webapps\%2\WEB-INF\lib\gmbal-api-only-4.0.2.jar del %1\webapps\%2\WEB-INF\lib\gmbal-api-only-4.0.2.jar
if exist %1\webapps\%2\WEB-INF\lib\management-api-3.2.3.jar del %1\webapps\%2\WEB-INF\lib\management-api-3.2.3.jar
if exist %1\webapps\%2\WEB-INF\lib\ha-api-3.1.9.jar del %1\webapps\%2\WEB-INF\lib\ha-api-3.1.9.jar
xcopy gxwrapperjavax.jar %1\webapps\%2\WEB-INF\lib /Y /D

:copy_files
if not exist %1\webapps\%2\WEB-INF\lib\guava-30.1-jre.jar del %1\webapps\%2\WEB-INF\lib\guava-*jre.jar 
if not exist %1\webapps\%2\WEB-INF\lib\compiler-0.9.7.jar del %1\webapps\%2\WEB-INF\lib\compiler-*.jar
xcopy CloudServices.config %1\webapps\%2\WEB-INF /Y /D
xcopy GXCF_Chatbots.config %1\webapps\%2\WEB-INF /Y /D
xcopy rewrite.config %1\webapps\%2\WEB-INF /Y /D
if exist PDFReport.template xcopy PDFReport.template %1\webapps\%2\WEB-INF /Y /D
xcopy .\modules\*.* %1\webapps\%2\WEB-INF\lib /Y /D
xcopy .\datasources\*.* %1\webapps\%2\WEB-INF\lib /Y /D
xcopy context.xml %1\webapps\%2\META-INF /Y /D
xcopy .\drivers\*.* %1\webapps\%2\WEB-INF\lib /Y /D
xcopy itext-2.1.7.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy iTextAsian.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy lucene-core-2.2.0.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy Tidy.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy lucene-highlighter-2.2.0.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy lucene-spellchecker-2.2.0.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy poi-4.1.2.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy commons-collections4-4.1.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy joda-time-2.10.4.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy poi-ooxml-4.1.2.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy poi-ooxml-schemas-4.1.2.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy commons-compress-1.21.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy commons-math3-3.6.1.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy curvesapi-1.06.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy SparseBitSet-1.2.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy xmlbeans-3.1.0.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy poi-scratchpad-4.1.2.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy printingappletsigned.jar %1\webapps\%2 /Y /D
xcopy rbuildj.dll %1\webapps\%2 /Y /D
xcopy GXDIB32.DLL %1\webapps\%2 /Y /D
xcopy developermenu.html %1\webapps\%2\static /Y /D
xcopy .\%7\*.rpt %1\webapps\%2\%7 /Y /D
if exist .\private\Interfaces md %1\webapps\%2\WEB-INF\private\Interfaces & xcopy .\private\Interfaces\*.* %1\webapps\%2\WEB-INF\private\Interfaces /Y /D
xcopy .\Metadata\TableAccess\*.* %1\webapps\%2\Metadata\TableAccess /Y /D
xcopy .\devmenu\*.* %1\webapps\%2\static\devmenu /Y /D
xcopy .\bootstrap\*.* %1\webapps\%2\static\bootstrap /Y /D /S
xcopy .\static\*.* %1\webapps\%2\static /Y /D /S
xcopy annotations-api.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy json-20180813.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy spatial4j-0.6.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy GeographicLib-Java-1.49.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy jts-1.14.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy jtsio-1.14.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy noggit-0.5.jar  %1\webapps\%2\WEB-INF\lib /Y /D
xcopy grpc-core-1.37.0.jar  %1\webapps\%2\WEB-INF\lib /Y /D
xcopy grpc-all-1.37.0.jar  %1\webapps\%2\WEB-INF\lib /Y /D
xcopy grpc-api-1.37.0.jar  %1\webapps\%2\WEB-INF\lib /Y /D
xcopy grpc-stub-1.37.0.jar  %1\webapps\%2\WEB-INF\lib /Y /D
xcopy grpc-protobuf-1.37.0.jar  %1\webapps\%2\WEB-INF\lib /Y /D
xcopy grpc-services-1.37.0.jar  %1\webapps\%2\WEB-INF\lib /Y /D
xcopy simple-xml-2.7.1.jar  %1\webapps\%2\WEB-INF\lib /Y /D
xcopy asm-3.1.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy bcprov-jdk15on-1.69.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy bcpkix-jdk15on-1.69.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy bcutil-jdk15on-1.69.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy commons-logging-1.0.4.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy commons-io-2.11.0.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy commons-net-3.3.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy commons-codec-1.9.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy xmlsec-2.2.3.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy xml-apis-1.4.01.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy xercesImpl-2.12.0.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy activation-1.1.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy jakarta.activation-2.0.0.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy slf4j-api-1.7.32.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy slf4j-nop-1.7.7.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy saaj-api-1.3.5.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy .\themes\*.* %1\webapps\%2\themes /Y /D
xcopy .\gxusercontrols\*.* %1\webapps\%2\WEB-INF\gxusercontrols /Y /D
xcopy compiler-0.9.7.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy guava-30.1-jre.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy gxgeospatial.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy gxexternalproviders.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy encoder-1.2.2.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy encoder-jsp-1.2.2.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy ..\..\CSSLibraries  %1\webapps\%2\static\ /Y /D /S
xcopy manifest.json %1\webapps\%2\static /Y /D
xcopy service-worker.js %1\webapps\%2\static /Y /D
xcopy *.manifest.json %1\webapps\%2\static /Y /D
xcopy *.service-worker.js %1\webapps\%2\static /Y /D
xcopy .\*.yaml %1\webapps\%2\static\ /Y /D

if exist .\private\*.json md %1\webapps\%2\WEB-INF\private
if exist .\private\*.json xcopy .\private\*.grp.json %1\webapps\%2\WEB-INF\private /Y /D

if exist .\GAM_Backend xcopy .\GAM_Backend\static %1\webapps\%2\static /Y /D /S
if exist .\GAM_Backend xcopy .\GAM_Backend\themes\CarmineGAM.json %1\webapps\%2\themes /Y /D
if exist .\GAM_Backend xcopy .\GAM_Backend\WEB-INF\classes %1\webapps\%2\WEB-INF\classes /Y /D /S

if exist .\blackberry md %1\webapps\%2\blackberry
if exist .\blackberry xcopy .\blackberry\*.* %1\webapps\%2\blackberry /Y /D

if exist %1\webapps\%2\WEB-INF\lib\commons-io-2.2.jar del %1\webapps\%2\WEB-INF\lib\commons-io-2.2.jar

if exist %1\webapps\%2\WEB-INF\lib\gxclassR.jar del %1\webapps\%2\WEB-INF\lib\gxclassR.jar
if exist %1\webapps\%2\WEB-INF\lib\gxcommon.jar del %1\webapps\%2\WEB-INF\lib\gxcommon.jar
if exist %1\webapps\%2\WEB-INF\lib\gxwrappercommon.jar del %1\webapps\%2\WEB-INF\lib\gxwrappercommon.jar

xcopy gxclassR.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy gxcommon.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy gxwrappercommon.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy gxcryptocommon.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy gxmail.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy javapns.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy gxsearch.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy gxoffice.jar %1\webapps\%2\WEB-INF\lib /Y /D
xcopy gxmaps.jar %1\webapps\%2\WEB-INF\lib /Y /D

if exist %1\conf\catalina\localhost\%2.xml goto xcopyCONTEXT
copy  context.xml %1\conf\catalina\localhost\%2.xml
goto :ENDxcopyCONTEXT

:xcopyCONTEXT
xcopy  context.xml %1\conf\catalina\localhost\%2.xml /Y /D
:ENDxcopyCONTEXT

if exist .\private\notifications.json md %1\webapps\%2\WEB-INF\private
if exist .\private\notifications.json xcopy .\private\*.* %1\webapps\%2\WEB-INF\private /Y /D