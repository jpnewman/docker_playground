import jenkins.model.*
import hudson.security.*
import jenkins.security.s2m.AdminWhitelistRule

def env = System.getenv()

def jenkins_username = env['JENKINS_USERNAME'] ?: "admin"
def jenkins_password = env['JENKINS_PASSWORD'] ?: "admin"

def instance = Jenkins.getInstance()

def hudsonRealm = new HudsonPrivateSecurityRealm(false)
hudsonRealm.createAccount(jenkins_username, jenkins_password)
instance.setSecurityRealm(hudsonRealm)

def strategy = new FullControlOnceLoggedInAuthorizationStrategy()
instance.setAuthorizationStrategy(strategy)
instance.save()

Jenkins.instance.getInjector().getInstance(AdminWhitelistRule.class).setMasterKillSwitch(false)
