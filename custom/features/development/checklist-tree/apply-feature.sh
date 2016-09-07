echo "************************"
echo "**** CHECKLIST TREE ****"
echo "************************"


#
# Source Code  Updates
#
cp -r custom/features/development/checklist-tree/src/main src


#
# Update Code
#
sed -i "s|uses = {}|uses = {ChecklistQuestionMapper.class}|" src/main/java/com/pwc/assurance/adc/service/mapper/ChecklistMapper.java

