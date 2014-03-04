#!/bin/sh

set -eu
die(){ echo $2; exit $1; }

. /opt/ber211/current/profile
. /opt/jstdtools/current/profile

PATH="${M2_HOME}/bin:${ANT19_HOME}/bin:$PATH"; export PATH
MAVEN_OPTS="-Xmx512m -XX:MaxPermSize=128m -ea"; export MAVEN_OPTS

mvn --offline --fail-fast clean test

echo "all done"
