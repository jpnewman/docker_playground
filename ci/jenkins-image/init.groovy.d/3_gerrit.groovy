import jenkins.*
import jenkins.model.*
import hudson.*
import hudson.model.*

import com.sonyericsson.hudson.plugins.gerrit.trigger.PluginImpl
import com.sonyericsson.hudson.plugins.gerrit.trigger.GerritServer
import com.sonyericsson.hudson.plugins.gerrit.trigger.config.Config
import net.sf.json.JSONObject
import net.sf.json.JSONArray

def env = System.getenv()

def gerrit_host_name = env['GERRIT_HOST_NAME'] ?: "gerrit"
def gerrit_front_end_url = env['GERRIT_FRONT_END_URL'] ?: "http://gerrit"

def gerrit_ssh_port = env['GERRIT_SSH_PORT'] ?: "29418"
gerrit_ssh_port = gerrit_ssh_port.toInteger()

def gerrit_username = env['GERRIT_USERNAME'] ?: "jenkins"
def gerrit_email = env['GERRIT_EMAIL'] ?: ""
def gerrit_ssh_key_file = env['GERRIT_SSH_KEY_FILE'] ?: "/var/jenkins_home/.ssh/id_rsa"
def gerrit_ssh_key_password = env['GERRIT_SSH_KEY_PASSWORD'] ?: null

def gerrit_http_user_name = env['GERRIT_HTTP_USER_NAME'] ?: "gerrit_jenkins"
def gerrit_http_password = env['GERRIT_HTTP_PASSWORD'] ?: "password123"

categories = new JSONArray()
[
    [
        'verdictValue': 'Code-Review',
        'verdictDescription': 'Code Review'
    ],
    [
        'verdictValue': 'Verified',
        'verdictDescription': 'Verified'
    ]
].each() { it ->
    jsonCat = new JSONObject()
    jsonCat.accumulateAll(it)
    categories.add(jsonCat)
}

restApi = new JSONObject()
restApi.accumulateAll([
    'gerritHttpUserName': gerrit_http_user_name,
    'gerritHttpPassword': gerrit_http_password
])

serverConfig = new JSONObject()
serverConfig.accumulateAll([
    'gerritHostName': gerrit_host_name,
    'gerritSshPort': gerrit_ssh_port,
    'gerritFrontEndUrl': gerrit_front_end_url,
    'gerritUserName': gerrit_username,
    'gerritEMail': gerrit_email,
    'gerritAuthKeyFile': System.getenv('JENKINS_HOME') + "/.ssh/id_rsa",
    'gerritBuildStartedVerifiedValue': 0,
    'gerritBuildSuccessfulVerifiedValue': 1,
    'gerritBuildFailedVerifiedValue': -1,
    'gerritBuildUnstableVerifiedValue': 0,
    'gerritBuildNotBuiltVerifiedValue': 0,
    'gerritBuildStartedCodeReviewValue': 0,
    'gerritBuildSuccessfulCodeReviewValue': 0,
    'gerritBuildFailedCodeReviewValue': 0,
    'gerritBuildUnstableCodeReviewValue': 0,
    'gerritBuildUnstableCodeReviewValue': 0,
    'gerritVerifiedCmdBuildStarted': "gerrit review <CHANGE>,<PATCHSET> --message 'Build Started <BUILDURL> <STARTED_STATS>' --verified <VERIFIED> --code-review <CODE_REVIEW>",
    'gerritVerifiedCmdBuildSuccessful': "gerrit review <CHANGE>,<PATCHSET> --message 'Build Successful <BUILDS_STATS>' --verified <VERIFIED> --code-review <CODE_REVIEW>",
    'gerritVerifiedCmdBuildFailed': "gerrit review <CHANGE>,<PATCHSET> --message 'Build Failed <BUILDS_STATS>' --verified <VERIFIED> --code-review <CODE_REVIEW>",
    'gerritVerifiedCmdBuildUnstable': "gerrit review <CHANGE>,<PATCHSET> --message 'Build Unstable <BUILDS_STATS>' --verified <VERIFIED> --code-review <CODE_REVIEW>",
    'gerritVerifiedCmdBuildNotBuilt': "gerrit review <CHANGE>,<PATCHSET> --message 'No Builds Executed <BUILDS_STATS>' --verified <VERIFIED> --code-review <CODE_REVIEW>",
    'enableManualTrigger': true,
    'enablePluginMessages': true,
    'verdictCategories': categories,
    'useRestApi': restApi
])
server = new GerritServer(gerrit_host_name)
server.setConfig(new Config(serverConfig))

plugin = PluginImpl.instance
plugin.setServers([server] as LinkedList)
plugin.save()
