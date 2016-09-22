echo "******************************"
echo "**** CHANGE FOOTER ****"
echo "******************************"

sed -i "s|\"This is your footer\"|\"\&copy; 2015-2016 PwC. All rights reserved. PwC refers to the PwC network and/or one or more of its member firms, each of which is a separate legal entity.Please see www.pwc.com/structure  for further details. \"|" src/main/webapp/i18n/en/global.json
