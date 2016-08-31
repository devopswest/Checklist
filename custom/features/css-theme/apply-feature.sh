#
# CSS - Add Custom styles
#
cp -r custom/features/css-theme/src/main/webapp/scss src/main/webapp
cp -r custom/features/css-theme/src/main/webapp/content src/main/webapp
cp -r custom/features/css-theme/src/main/webapp/app/home src/main/webapp/app


sed -i "s|</head>|\t<\!-- build:css content/css/custom.css -->\n\t<link rel="stylesheet" href="content/css/custom.css">\n\t<\!-- endbuild -->\n</head>|" src/main/webapp/index.html


#
# Navigator: Change Logo
#
sed -i "s|logo-jhipster|logo|" src/main/webapp/app/layouts/navbar/navbar.html
