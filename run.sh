#!/bin/sh

set -eu
die(){ echo $2; exit $1; }

. /opt/ber211/current/profile
. /opt/jstdtools/current/profile

test -f mdep.classpath || die 1 "run build.sh first"

CLASSPATH=target/classes
CLASSPATH=${CLASSPATH}:`cat mdep.classpath`
export CLASSPATH
echo -n "$CLASSPATH" | perl -072 -l012 -pe1

java -ea org.eclipse.jetty.start.Main

echo "all done"
