import java.util.logging.Logger

import jenkins.model.*
import hudson.logging.*

// LogRecorder : [ Logger : LogLevel ]
// LogLevel (UpperCase): ALL, FINEST, FINER, FINE, CONFIG, INFO, WARNING, SEVERE
loggers = [
  "Gerrit-Plugin": [
    "com.sonyericsson.hudson.plugins.gerrit": "ALL",
    "com.sonyericsson.hudson.plugins.gerrit.trigger.PluginImpl": "ALL",
  ]
]

Jenkins jenkins = Jenkins.getInstance()
LogRecorderManager manager = jenkins.getLog()

loggers.each { loggerName, loggerTiggers ->
  LogRecorder logRecorder = manager.logRecorders.find{ it.key.equalsIgnoreCase(loggerName) }?.value
  if (logRecorder == null) {
    manager.doNewLogRecorder(loggerName)
    logRecorder = manager.logRecorders.find{ it.key.equalsIgnoreCase(loggerName) }?.value
  }

  if (logRecorder != null) {
    loggerTiggers.each { logger, level ->
      LogRecorder.Target loggerTarget = logRecorder.targets.find{ it.name.equalsIgnoreCase(logger) }
      if (loggerTarget == null) {
        loggerTarget = new LogRecorder.Target(logger, level)
        logRecorder.targets.add(loggerTarget)
      }

      if (loggerTarget.getLevel() != level) {
        logRecorder.targets.remove(loggerTarget)
        loggerTarget = new LogRecorder.Target(logger, level)
        logRecorder.targets.add(loggerTarget)
      }
    }
  }
}

return
