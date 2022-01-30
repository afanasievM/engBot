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