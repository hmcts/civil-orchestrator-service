 # renovate: datasource=github-releases depName=microsoft/ApplicationInsights-Java
ARG APP_INSIGHTS_AGENT_VERSION=3.4.14
FROM hmctspublic.azurecr.io/base/java:17-distroless

COPY lib/applicationinsights.json /opt/app/
COPY build/libs/civil-orchestrator-service.jar /opt/app/

EXPOSE 9090
CMD [ "civil-orchestrator-service.jar" ]
