#!/bin/sh

##
## Build Step - Evaluate Change and Close
##
## $1 - Change Number
## $2 - Actual_Start_Date			2016-07-17T18:15:00.000Z
## $3 - Actual_End_Date				2016-07-18T02:00:00.000Z
## $4 - Closure_Comments			"RFC Implemented Successfully."
##


# bash move-rfc-evaluation-closed.sh '2016-06-20T10:22:53.319Z' '2016-06-20T14:22:53.320Z' '"Closing the RFC comment 1","Closing the RFC comment 2"'


changeNumber=$1
# startDate=$2
# endDate=$3
# closureComments=$4

#if [ "$#" -ne 4 ]; then
#	echo "Enter correct RFC#, Actual Start, Actual End dates and Closure comments."
#else
	#set -e
	if [ -n "$changeNumber" ]; then
		CURL_CMD="curl -s -f --cacert $PWD/ssl/issuing1.pem --cacert $PWD/ssl/policy.pem --cacert $PWD/ssl/root.pem -X POST -H 'Content-Type: application/json' -H 'Accept: application/json' -H 'APIKey: ${API_KEY}' -H 'APIKeySecret: ${API_KEY_SECRET}' -d '{ \"keys\": { \"changeNumber\": \"$changeNumber\"}, \"instance\": { \"middle\": { \"ActualOutageStart\": \"$startDate\", \"ActualOutageEnd\": \"$endDate\" }, \"ClosureComments\": { \"ClosureComments\": [$closureComments]}}}' '${RFC_EVALUATION_CLOSED_API_URL}' ";

		echo $CURL_CMD
		eval $CURL_CMD >> /var/app/rfcdetails.txt
		sed -i -e '$a\' /var/app/rfcdetails.txt
	else
		echo "RFC Number not present in property file."
        exit 1
	fi
#fi

