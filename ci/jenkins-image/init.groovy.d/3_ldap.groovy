import jenkins.model.*
import hudson.security.*
import org.jenkinsci.plugins.*

import hudson.model.*
import jenkins.security.plugins.ldap.*
import hudson.util.Secret

import hudson.security.LDAPSecurityRealm.EnvironmentProperty
import hudson.security.LDAPSecurityRealm.CacheConfiguration

import hudson.security.LDAPSecurityRealm
import hudson.util.Secret
import jenkins.model.IdStrategy
import jenkins.security.plugins.ldap.LDAPConfiguration
import net.sf.json.JSONObject

def env = System.getenv()

String server = env['LDAP_SERVER'] ?: 'ldap://ldap'
String rootDN = env['LDAP_ROOT_DN'] ?: 'dc=example,dc=org'
String userSearchBase = env['LDAP_USE_SEARCH_BASE'] ?: 'ou=People'
String userSearch = env['LDAP_USER_SEARCH'] ?: 'uid={0}'
String groupSearchBase = env['LDAP_GROUP_SEARCH_BASE'] ?: 'ou=Groups'
String groupSearchFilter = env['LDAP_GROUP_SEARCH_FILTER'] ?: ''
LDAPGroupMembershipStrategy groupMembershipStrategy
String managerDN = env['LDAP_MANAGER_DN'] ?: 'cn=admin,dc=example,dc=org'
Secret managerPasswordSecret = Secret.fromString(env['LDAP_MANAGER_PASSWORD_SECRET'] ?: 'admin')
boolean inhibitInferRootDN = env['LDAP_INHIBITINFER_ROOT_DN'] ?: false
EnvironmentProperty[] environmentProperties = []
String displayNameAttributeName = env['LDAP_DISPLAYNAME_ATTRIBUTE_NAME'] ?: 'cn'
String mailAddressAttributeName = env['LDAP_MAILADDRESS_ATTRIBUTE_NAME'] ?: 'mail'

boolean disableMailAddressResolver = false
CacheConfiguration cache = null
IdStrategy userIdStrategy = IdStrategy.CASE_INSENSITIVE
IdStrategy groupIdStrategy = IdStrategy.CASE_INSENSITIVE

LDAPConfiguration config = new LDAPConfiguration(server,
                                                 rootDN,
                                                 inhibitInferRootDN,
                                                 managerDN,
                                                 managerPasswordSecret) 

config.setUserSearchBase(userSearchBase)
config.setUserSearch(userSearch)

config.setGroupSearchBase(groupSearchBase)
config.setGroupSearchFilter(groupSearchFilter)

// config.setGroupMembershipStrategy(groupMembershipStrategy)
config.setEnvironmentProperties(environmentProperties)

config.setDisplayNameAttributeName(displayNameAttributeName)

config.setMailAddressAttributeName(mailAddressAttributeName)

List<LDAPConfiguration> configurations = [config]

SecurityRealm ldapRealm = new LDAPSecurityRealm(configurations,
                                                disableMailAddressResolver,
                                                cache,
                                                userIdStrategy,
                                                groupIdStrategy)

Thread.start {
    sleep 10000

    Jenkins.instance.setSecurityRealm(ldapRealm)
    Jenkins.instance.save()
}
