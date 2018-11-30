import jenkins.*
import jenkins.model.*
import hudson.*
import hudson.model.*

def instance = Jenkins.getInstance()
instance.getDescriptor("jenkins.CLI").get().setEnabled(false)
