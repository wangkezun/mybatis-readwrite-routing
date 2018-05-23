import ch.qos.logback.classic.encoder.PatternLayoutEncoder

import static ch.qos.logback.classic.Level.DEBUG
import static ch.qos.logback.classic.Level.ERROR

scan("30 seconds")
appender("STDOUT", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%d{yyyy-MM-dd HH:mm:ss.SSS} %-5level %logger{15} - %msg%n"
    }
}
logger("org.springframework", ERROR)
logger("java.sql.PreparedStatement", DEBUG)
logger("java.sql.Statement", DEBUG)
logger("java.sql.ResultSet", DEBUG)
logger("java.sql.Connection", DEBUG)
logger("com.zaxxer", ERROR)
logger("io.wkz", DEBUG)
root(DEBUG, ["STDOUT"])
