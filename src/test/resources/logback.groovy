// See to: https://logback.qos.ch/manual/groovy_ja.html

// always a good idea to add an on console status listener
statusListener(OnConsoleStatusListener)

def CONSOLE_APPENDER = "CONSOLE"

appender(CONSOLE_APPENDER, ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "TEST %d [%thread] %-5level %logger{36} - %msg%n"
    }
}

root(INFO, [CONSOLE_APPENDER])
