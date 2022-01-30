# engBot
This bot can help you learn foreign words. You can add words with translation
to your vocabulary. This bot will send test every hour.

For run bot you need:
1. Create database. I use MySQL.
2. Change application.properties:
   spring.datasource.url=jdbc:mysql://example:3306/DB
   spring.datasource.username=username
   spring.datasource.password=password
   spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
mysql://example:3306/DB - url to your database
username - login to your database
password - password to your database
com.mysql.cj.jdbc.Driver - don't need to change
3. Change application profile:
In file EngBotApplication you need to change url to 
application.properties if row 36. 
".properties("spring.config.name:application-dev")"
For example use "application"
4. Add to enviroment variables bot's token and name:
   bot.botName=bot's name
   bot.token=your token
   And you can run it from IDE by
   spring-boot:run -f pom.xml
5. You can package and run jar application:
   package -f pom.xml
   and then
   java -jar -Dbot.botName="bot's name" -Dbot.token="your token" path-to-Project/target/engBot-*-spring-boot.jar 
