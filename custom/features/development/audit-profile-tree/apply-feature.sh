echo "****************************"
echo "**** AUDIT-PROFILE TREE ****"
echo "****************************"


#
# Copy entities
#
cp -r custom/features/development/audit-profile-tree/src/main/webapp/app/entities src/main/webapp/app


#
# Java Updates
#
cp -r custom/features/development/audit-profile-tree/src/main/java src/main
