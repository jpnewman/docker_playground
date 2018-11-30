import jenkins.*
import jenkins.model.*
import hudson.*
import hudson.model.*

def instance = Jenkins.getInstance()
instance.getAdministrativeMonitor('jenkins.diagnostics.RootUrlNotSetMonitor').disable(true)
