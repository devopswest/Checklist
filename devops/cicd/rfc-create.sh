#!/bin/sh

##
## Build Step - Create RFC and Move To Change Implementation
##
## $1 - Brief_Description				Deployment of CMA BSL component in BSL Stage environment
## $2 - Category						Application Infrastructure Management
## $3 - Subcategory						US Lombardi
## $4 - Assignment_Group				US IT APP SPT - ENVOY
## $5 - Initiated_By					ssingh145
## $6 - Change_Coordinator				rmaheshwar008
## $7 - Reason 							business
## $8 - Service							Envoy
## $9 - Requested_End_Date				2016-07-18T04:12:32.065Z
## $10- Backout_Complexity				Low
## $11- Impact							3
## $12- Urgency							3
## $13- Risk_Assessment 				3
## $14- Description 					"This RFC has been created for the purpose of deploying CMA BSL component on BSL Stage environment.","The deployment is required to deploy the latest CMA BSL code on Stage environment so that instance migration testing can be done on Stage environment for next CMA release."
## $15- Affected_CI						"matlkgsswwhs011","matlkgsswwhs012"
## $16- Lifecycle_Status				Live
## $17- Environment 					Stage
## $18- Plan 							Test deployment plan.
## $19- Backout_Plan 					Test backout plan.
## $20- Planned_Start_Date 				2016-07-17T18:12:32.065Z
## $21- Planned_End_Date				2016-07-18T03:12:32.065Z
## $22- Change_Implementer				ssingh145
## $23- Template						Standard RFC Template
## $24- Risk_Assessment_Mitigation		"Sample Risk Assessment and Mitigation comments."
##


# bash create-rfc-change-implementation.sh 'Curl testing' 'Application Infrastructure Management' 'US Lombardi' 'US IT APP SPT - ENVOY' 'ssingh145' 'rmaheshwar008' 'business' 'Envoy' '2016-06-10T04:12:32.065Z' 'Low' '3' '3' '3' '"Desc1 This is \"Description\" 1","Desc2 This is Description 2","Desc3 This is Description 3"' '"matlkgsswwhs011","Envoy (Development)"' 'Live' 'Stage' 'Sample plan for curl' 'Sample backout plan' '2016-06-10T05:12:32.065Z' '2016-06-15T05:12:32.065Z' 'ssingh145' 'Standard Create Template' '"Risk Assessment Mitigation 1","Risk Assessment Mitigation 2"'

# briefDescription=$1
# category=$2
# subcategory=$3
# assignmentGroup=$4
# initiatedBy=$5
# changeCoordinator=$6
# reason=$7
# service=$8
# requestedEndDate=$9
# backoutComplexity=$10
# impact=$11
# urgency=$12
# riskAssessment=$13
# description=$14
# affectedCI=$15
# lifecycleStatus=$16
# environment=$17
# plan=$18
# backoutPlan=$19
# plannedStartDate=$20
# plannedEndDate=$21
# changeImplementer=$22
# template=$23
# riskAssessmentMitigation=$24



#if [ "$#" -ne 24 ]; then
#	echo "Enter the correct number of arguments"
#else
	#echo ${24}
	#set -e
	CURL_CMD="curl -s -f --cacert ../devops/ssl/issuing1.pem --cacert ../devops/ssl/policy.pem --cacert ../devops/ssl/root.pem -X POST -H 'Content-Type: application/json' -H 'Accept: application/json' -H 'APIKey: ${API_KEY}' -H 'APIKeySecret: ${API_KEY_SECRET}' -d '{  \"keys\": {},  \"instance\": {\"Service\": \"$service\", \"LifeCycleStatus\": \"${lifeCycleStatus}\", \"middle\": {}, \"Urgency\": \"${urgency}\",\"Impact\": \"${impact}\",\"BackoutComplex\": \"${backoutComplexity}\",\"description.structure\": {\"Description\": {\"Description\": [${description}]},\"AffectedCI\": {\"AffectedCI\": [${affectedCI}]}},\"RequestedEndDate\": \"$requestedEndDate\",\"Environment\": \"${environment}\", \"Plan\": \"${plan}\", \"BackoutPlan\": \"${backoutPlan}\", \"StandardChange\": \"true\", \"ChangeImplementer\": \"${changeImplementer}\", \"Template\": \"${template}\", \"RiskAssessmentMitigation\": {\"RiskAssessmentMitigation\": [${riskAssessmentMitigation}]} , \"header\": {\"RiskAssessment\": \"${riskAssessment}\", \"InitiatedBy\":\"$initiatedBy\", \"briefDescription\": \"$briefDescription\",\"Category\": \"$category\",\"ChangeCoordinator\": \"$changeCoordinator\",\"AssignmentGroup\": \"$assignmentGroup\",\"Reason\": \"$reason\",\"Subcategory\": \"$subcategory\",\"PlannedStart\": \"${plannedStartDate}\",\"PlannedEnd\": \"${plannedEndDate}\"}}}' '${RFC_CREATE_IMPLEMENTATION_API_URL}' ";

	#echo $CURL_CMD
	response=$(eval $CURL_CMD);
	if [ -n "$response" ]; then
		#echo $response
		echo $response >> /var/app/rfcdetails.txt
		sed -i -e '$a\' /var/app/rfcdetails.txt
		changeNumber=`echo $response | sed -e 's/^.*"changeNumber"[ ]*:[ ]*"//' -e 's/".*//'`
		#echo $changeNumber
		if [ -n "$changeNumber" ]; then
			echo $changeNumber
		else
			echo "RFC Number not present in property file."
            exit 1
		fi
	else
		echo "Response not found."
		exit 1
	fi
#fi
