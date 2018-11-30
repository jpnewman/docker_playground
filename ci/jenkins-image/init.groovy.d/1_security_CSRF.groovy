import jenkins.*
import jenkins.model.*
import hudson.*
import hudson.model.*

import hudson.security.csrf.DefaultCrumbIssuer

def instance = Jenkins.getInstance()
instance.setCrumbIssuer(new DefaultCrumbIssuer(false))

instance.save()
