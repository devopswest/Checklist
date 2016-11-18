FROM pwcusadc/app:base

COPY build/libs/*.war /var/app/app.war
